package net.jarl.kata.currencyzoo.data;

import static java.util.Collections.emptySet;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OpenExchange implements CurrencyConverter {

    private static final long REFRESH_PERIOD = 3600000; // 1h
    private static final int SCALE = 6;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    private final Logger LOG = LoggerFactory.getLogger( OpenExchange.class );

    private static final String BASE_URL = "https://openexchangerates.org/api";
    private static final String CURRENCIES = "/currencies.json";
    private static final String RATES = "/latest.json";
    private static final String APP_ID = "07e17948ed0c4798b5bca2fd8baf8a8e";

    private static final ObjectMapper mapper = new ObjectMapper();
    private AtomicReference<Map<String, Currency>> currencies = new AtomicReference<>();

    @Override
    public List<Currency> availableCurrencies() {
        Collection<Currency> values = Optional.ofNullable( currencies.get() ).
            map( Map::values ).
            orElse( emptySet() );
        return new ArrayList<>( values );
    }

    @Override
    public BigDecimal convert( String from, String to, BigDecimal amount )
        throws ConversionException
    {
        Map<String, Currency> data = getConversionData();
        Currency fromCurrency = getCurrency( data, from );
        Currency toCurrency = getCurrency( data, to );

        return amount.
            multiply( toCurrency.getRate() ).
            divide( fromCurrency.getRate(), SCALE, ROUNDING_MODE );
    }

    @Scheduled(initialDelay=0, fixedDelay=REFRESH_PERIOD)
    public void refreshCurrencies() {

        LOG.info( "Refreshing list of currencies now" );

        String currenciesUrl = BASE_URL + CURRENCIES + "?app_id=" + APP_ID;
        String ratesUrl = BASE_URL + RATES + "?app_id=" + APP_ID;

        try(
            InputStream currenciesStream = submit( currenciesUrl );
            InputStream ratesStream = submit( ratesUrl );
        ){
            JsonNode currenciesNode = mapper.readTree( currenciesStream );
            Map<String, String> descriptions = asMap( currenciesNode );

            JsonNode ratesNode = mapper.readTree( ratesStream ).get( "rates" );
            Map<String, String> conversions = asMap( ratesNode );

            Map<String, Currency> freshCurrencies = new TreeMap<>();
            conversions.forEach( (symbol, rate) ->
                makeCurrency( symbol, rate, descriptions ).
                    ifPresent( c -> freshCurrencies.put( symbol, c ) ) );

            currencies.set( freshCurrencies );
        }
        catch( IOException e ) {
            LOG.error( "Unable to refresh list of currencies", e );
        }
    }

    private Map<String, Currency> getConversionData() throws ConversionException {
        return Optional.ofNullable( currencies.get() ).
            orElseThrow( () -> new ConversionException( "No data available" ) );
    }

    private Currency getCurrency( Map<String, Currency> data, String symbol )
        throws ConversionException
    {
        if( !data.containsKey( symbol ) ) {
            throw new ConversionException( "no data available on symbol " + symbol );
        }
        return data.get( symbol );
    }

    private InputStream submit( String urlString ) throws IOException {
        LOG.info( "Submitting API query: " + urlString );
        URL url = new URL( urlString );
        URLConnection conn = url.openConnection();
        return conn.getInputStream();

    }

    private Map<String, String> asMap( JsonNode node ) {
        Iterable<Entry<String, JsonNode>> fields = () -> node.getFields();
        return StreamSupport.stream( fields.spliterator(), false ).
            collect( Collectors.toMap(
                entry -> entry.getKey().toString(),
                entry -> entry.getValue().toString() ) );
    }

    private Optional<Currency> makeCurrency(
        String symbol,
        String rate,
        Map<String, String> descriptions )
    {
        try {
            String description = descriptions.get( symbol );
            if( description != null ) {
                BigDecimal value = new BigDecimal( rate );
                return Optional.of( new Currency( symbol, description, value ) );
            }
        }
        catch( NumberFormatException e ) {
            LOG.warn( String.format(
                "Received malformed rate for symbol '%s': '%s'", symbol, rate ) );
        }
        return Optional.empty();
    }
}
