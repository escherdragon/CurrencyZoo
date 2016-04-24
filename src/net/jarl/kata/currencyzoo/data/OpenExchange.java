package net.jarl.kata.currencyzoo.data;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
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
public class OpenExchange implements CurrencyDataProvider {

    private final Logger LOG = LoggerFactory.getLogger( OpenExchange.class );

    private static final String BASE_URL = "https://openexchangerates.org/api";
    private static final String CURRENCIES = "/currencies.json";
    private static final String CONVERT = "/convert";
    private static final String APP_ID = "07e17948ed0c4798b5bca2fd8baf8a8e";

    private static final ObjectMapper mapper = new ObjectMapper();
    private AtomicReference<List<Currency>> currencies = new AtomicReference<>();

    @Override
    public List<Currency> availableCurrencies() {
        return Optional.ofNullable( currencies.get() ).orElse( Collections.emptyList() );
    }

    @Override
    public BigDecimal convert( String from, String to, BigDecimal amount )
        throws UnavailableProviderException
    {
        String urlString = String.format( "%s%s/%s/%s/%s?app_id=%s",
            BASE_URL, CONVERT, amount.toPlainString(), from, to, APP_ID );

        try( InputStream is = submit( urlString ) ) {
            JsonNode node = mapper.readTree( is );
            String response = node.get( "response" ).asText();
            return new BigDecimal( response );
        }
        catch( IOException e ) {
            throw new UnavailableProviderException( e.getMessage(), e );
        }
    }

    @Scheduled(initialDelay=0, fixedDelay=1800000) // 30 minutes
    public void refreshCurrencies() {

        LOG.info( "Refreshing list of currencies now" );

        String urlString = BASE_URL + CURRENCIES + "?app_id=" + APP_ID;
        try( InputStream is = submit( urlString ) ) {
            JsonNode node = mapper.readTree( is );
            Iterable<Entry<String, JsonNode>> fields = () -> node.getFields();
            List<Currency> freshCurrencies =
                StreamSupport.stream( fields.spliterator(), false ).
                map( this::toCurrency ).
                collect( Collectors.toList() );
            currencies.set( freshCurrencies );
        }
        catch( IOException e ) {
            LOG.error( "Unable to refresh list of currencies", e );
        }
    }

    private InputStream submit( String urlString ) throws IOException {
        LOG.info( "Submitting API query: " + urlString );
        URL url = new URL( urlString );
        URLConnection conn = url.openConnection();
        return conn.getInputStream();

    }

    private Currency toCurrency( Entry<String, JsonNode> entry ) {
        return new Currency( entry.getKey(), entry.getValue().getTextValue() );
    }
}
