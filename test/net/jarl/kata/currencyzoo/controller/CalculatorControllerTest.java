package net.jarl.kata.currencyzoo.controller;

import static net.jarl.kata.currencyzoo.view.Views.CALCULATOR;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

import mockit.Injectable;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import net.jarl.kata.currencyzoo.configuration.MVCConfig;
import net.jarl.kata.currencyzoo.data.ConversionException;
import net.jarl.kata.currencyzoo.data.Currency;
import net.jarl.kata.currencyzoo.data.CurrencyConverter;
import net.jarl.kata.currencyzoo.form.CalculationForm;
import net.jarl.kata.currencyzoo.model.Query;
import net.jarl.kata.currencyzoo.model.QueryRepository;
import net.jarl.kata.currencyzoo.model.User;
import net.jarl.kata.currencyzoo.model.UserRepository;

/**
 * Unit tests for the {@link CalculatorController}.
 *
 * @author José A. Romero L.
 */
@RunWith(JMockit.class)
@ContextConfiguration(classes={MVCConfig.class})
public class CalculatorControllerTest extends AuthenticatedTestContext {

    @Tested
    private CalculatorController controller;

    @Injectable
    private CurrencyConverter provider;

    @Injectable
    private UserRepository userRepo;

    @Injectable
    private QueryRepository queryRepo;

    @Mocked
    private CalculationForm form;

    @Test
    public void defaultPageIsCalculator() throws Exception {
        MVC.mock( controller ).
            perform( get( "/" ) ).
            andExpect( status().isOk() ).
            andExpect( view().name( CALCULATOR ) );
    }

    @Test
    public void calculatorPageIsDisplayedOnRequest() throws Exception {
        MVC.mock( controller ).
            perform( get( CALCULATOR ) ).
            andExpect( status().isOk() ).
            andExpect( view().name( CALCULATOR ) );
    }

    @Test
    public void calculatorPageProvidesAvailableCurrencies() throws Exception {
        // given:
        final List<Currency> currencies = new ArrayList<>();
        new NonStrictExpectations() {{
            provider.availableCurrencies(); result = currencies;
        }};

        // when: then:
        MVC.mock( controller ).
            perform( get( CALCULATOR ) ).
            andExpect( model().attribute( "currencies", sameInstance( currencies ) ) );

        MVC.mock( controller ).
            perform( post( CALCULATOR ) ).
            andExpect( model().attribute( "currencies", sameInstance( currencies ) ) );
    }

    @Test
    public void postCalculatorPerformsCalculation() throws Exception {
        // given:
        final String from   = "AAA";
        final String to     = "BBB";
        final String amount = "10.50";

        final BigDecimal total = new BigDecimal( "88.44" );

        prepareCalculationExpectations(from, to, amount, total);

        // when: then:
        MVC.mock( controller ).
            perform( post( CALCULATOR ) ).
            andExpect( model().attribute( "from",   equalTo( from ) ) ).
            andExpect( model().attribute( "to",     equalTo( to ) ) ).
            andExpect( model().attribute( "amount", equalTo( amount ) ) ).
            andExpect( model().attribute( "result", equalTo( total ) ) );
    }

    @Test
    public void postCalculatorWithMissingDataRaisesError() throws Exception {
        // given:
        final String from   = "AAA";
        final String amount = "10.50";

        final BigDecimal total = new BigDecimal( "88.44" );

        prepareCalculationExpectations( from, null, amount, total );

        // when: then:
        MVC.mock( controller ).
            perform( post( CALCULATOR ) ).
            andExpect( model().attribute( "error", notNullValue() ) );
    }

    @Test
    public void postCalculatorWithInvalidDataRaisesError() throws Exception {
        // given:
        final String from   = "AAA";
        final String to     = "BBB";
        final String amount = "Anything but a number";

        final BigDecimal total = new BigDecimal( "88.44" );

        prepareCalculationExpectations( from, to, amount, total );

        // when: then:
        MVC.mock( controller ).
            perform( post( CALCULATOR ) ).
            andExpect( model().attribute( "error", notNullValue() ) );
    }

    @Test
    public void successfulCalculationsAreSaved() throws Exception {
        // given:
        final String from   = "AAA";
        final String to     = "BBB";
        final String amount = "10.50";

        final BigDecimal total = new BigDecimal( "88.44" );

        prepareCalculationExpectations( from, to, amount, total );
        
        final String username = "username";
        new NonStrictExpectations() {{
            userDetails.getUsername(); result = username;
            userRepo.findByLogin( username ); result = Optional.of( new User() );
        }};

        // when:
        MVC.mock( controller ).perform( post( CALCULATOR ) );
 
        // then:
        new Verifications() {{
            queryRepo.save( (Query) any ); times = 1;
            // TODO capture query and verify all values are passed correctly.
        }};
    }

    private void prepareCalculationExpectations(
        final String from,
        final String to,
        final String amount,
        final BigDecimal total )
        throws ConversionException
    {
        new NonStrictExpectations() {{
            form.getFrom();   result = from;
            form.getTo();     result = to;
            form.getAmount(); result = amount;

            provider.convert( from, to, (BigDecimal) any );
                result = total;
        }};
    }
}
