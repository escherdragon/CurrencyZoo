package net.jarl.kata.currencyzoo.data;

import java.math.BigDecimal;
import java.util.List;

public interface CurrencyConverter {

    List<Currency> availableCurrencies();

    BigDecimal convert( String from, String to, BigDecimal amount )
        throws ConversionException;
}
