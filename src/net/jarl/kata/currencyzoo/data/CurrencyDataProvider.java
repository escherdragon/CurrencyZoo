package net.jarl.kata.currencyzoo.data;

import java.math.BigDecimal;
import java.util.List;

public interface CurrencyDataProvider {

    List<Currency> availableCurrencies()
        throws UnavailableProviderException;

    BigDecimal convert( String from, String to, BigDecimal amount )
        throws UnavailableProviderException;
}
