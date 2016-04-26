package net.jarl.kata.currencyzoo.data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CurrencyConverter {

    List<Currency> availableCurrencies();

    Optional<Currency> getCurrency( String symbol );

    BigDecimal convert( String from, String to, BigDecimal amount )
        throws ConversionException;
}
