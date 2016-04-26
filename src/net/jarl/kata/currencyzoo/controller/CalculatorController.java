package net.jarl.kata.currencyzoo.controller;

import static net.jarl.kata.currencyzoo.view.Views.CALCULATOR;

import java.math.BigDecimal;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.jarl.kata.currencyzoo.data.ConversionException;
import net.jarl.kata.currencyzoo.data.Currency;
import net.jarl.kata.currencyzoo.data.CurrencyConverter;
import net.jarl.kata.currencyzoo.form.CalculationForm;
import net.jarl.kata.currencyzoo.model.Query;
import net.jarl.kata.currencyzoo.model.QueryRepository;
import net.jarl.kata.currencyzoo.model.UserRepository;

/**
 * Handles all requests involving the calculation and retrieval of currency
 * conversion data.
 *
 * @author José A. Romero L.
 */
@Controller
public class CalculatorController {

    private static final int MAX_QUERIES_TO_SHOW = 10;
    private Pageable page = new PageRequest( 0, MAX_QUERIES_TO_SHOW );

    @Autowired
    private CurrencyConverter provider;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private QueryRepository queryRepo;

    @RequestMapping(value={ "/", CALCULATOR }, method=RequestMethod.GET)
    public String getCalculator( ModelMap model ) {
        model.addAttribute( "currencies", provider.availableCurrencies() );
        addQueries( model );
        return CALCULATOR;
    }

    @RequestMapping(value=CALCULATOR, method=RequestMethod.POST)
    public String postCalculator( CalculationForm form, ModelMap model ) {

        String from = form.getFrom();
        String to = form.getTo();
        String amountStr = form.getAmount();

        getCalculator( model );

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

            addQueries( model );

            save( from, to, amount, result );
        }
        catch( ConversionException e ) {
            model.addAttribute( "error", e.getMessage() );
        }
        return CALCULATOR;
    }

    private void addQueries( ModelMap model ) {
        List<Query> queries = getCurrentUserQueries();
        model.addAttribute( "queries", queries );
        model.addAttribute( "queriedCurrencies", currenciesFor( queries ) );
    }

    private Map<String, Currency> currenciesFor( List<Query> queries ) {
        Map<String, Currency> index = new HashMap<>();
        for( Query query : queries ) {
            String from = query.getFrom();
            String to = query.getTo();
            provider.getCurrency( from ).ifPresent( c -> index.put( from, c ) );
            provider.getCurrency( to ).ifPresent( c -> index.put( to, c ) );
        }
        return index;
    }

    private List<Query> getCurrentUserQueries() {
        return getCurrentUserDetails().
            map( UserDetails::getUsername ).
            map( login ->
                queryRepo.findAllByUserLoginOrderByTimestampDesc( login, page ) ).
            orElse( Collections.emptyList() );
    }

    private void save( String from, String to, BigDecimal amount, BigDecimal result ) {
        getCurrentUserDetails().
            map( UserDetails::getUsername ).
            map( userRepo::findByLogin ).
            map( Optional::get ).
            map( u -> new Query( u, from, to, amount, result ) ).
            ifPresent( query -> {
                queryRepo.save( query );
                queryRepo.flush();
            });
    }

    private Optional<UserDetails> getCurrentUserDetails() {
        Object principal =
            SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Optional.ofNullable( (UserDetails) principal );
    }
}