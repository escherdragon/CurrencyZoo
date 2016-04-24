package net.jarl.kata.currencyzoo.controller;

import static net.jarl.kata.currencyzoo.view.Views.CALCULATOR;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.jarl.kata.currencyzoo.data.Currency;
import net.jarl.kata.currencyzoo.data.CurrencyDataProvider;
import net.jarl.kata.currencyzoo.data.UnavailableProviderException;
import net.jarl.kata.currencyzoo.form.CalculationForm;

/**
 * Handles all requests involving the calculation and retrieval of currency
 * conversion data.
 *
 * @author José A. Romero L.
 */
@Controller
public class CalculatorController {

    @Autowired
    private CurrencyDataProvider provider;

    @RequestMapping(value={ "/", CALCULATOR }, method=RequestMethod.GET)
    public String getCalculator( ModelMap model ) {
        List<Currency> currencies;
        try {
            currencies = provider.availableCurrencies();
            model.addAttribute( "currencies", currencies );
        }
        catch( UnavailableProviderException e ) {
            model.addAttribute( "error", "alert.provider.unavailable" );
        }
        return CALCULATOR;
    }

    @RequestMapping(value=CALCULATOR, method=RequestMethod.POST)
    public String postCalculator( CalculationForm form, ModelMap model ) {

        String from = form.getFrom();
        String to = form.getTo();
        String amountStr = form.getAmount();

        if( from == null || to == null || amountStr == null ) {
            model.addAttribute( "error", "alert.invalid.input" );
            return CALCULATOR;
        }
        BigDecimal amount;
        try {
            amount = new BigDecimal( amountStr );
        }
        catch( NumberFormatException e ) {
            model.addAttribute( "error", "alert.invalid.amount" );
            return CALCULATOR;
        }
        try {
            BigDecimal result = provider.convert( from, to, amount );
            model.addAttribute( "from", from );
            model.addAttribute( "to", to );
            model.addAttribute( "amount", amountStr );
            model.addAttribute( "result", result );
        }
        catch( UnavailableProviderException e ) {
            model.addAttribute( "error", "alert.provider.unavailable" );
        }
        return CALCULATOR;
    }
}
