package net.jarl.kata.currencyzoo.controller;

import static net.jarl.kata.currencyzoo.view.Views.ACCESS_DENIED;
import static net.jarl.kata.currencyzoo.view.Views.LOGGED_OUT;
import static net.jarl.kata.currencyzoo.view.Views.LOGIN;
import static net.jarl.kata.currencyzoo.view.Views.LOGOUT;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.test.context.ContextConfiguration;

import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import net.jarl.kata.currencyzoo.configuration.MVCConfig;

/**
 * Unit tests for the {@link AuthenticationController}
 *
 * @author José A. Romero L.
 */
@RunWith(JMockit.class)
@ContextConfiguration(classes={MVCConfig.class})
public class AuthenticationControllerTest extends AuthenticatedTestContext {

    @Tested
    private AuthenticationController controller;

    @Test
    public void loginViewIsDisplayedOnRequest()
        throws Exception
    {
        // given:
        assumeAuthenticatedUser();

        // when, then:
        MVC.mock( controller ).
            perform( get( LOGIN ) ).
            andExpect( status().isOk() ).
            andExpect( view().name( LOGIN ) );
    }

    @Test
    public void accessDeniedIsDisplayedOnRequest()
        throws Exception
    {
        // given:
        final String username = "zooplus";
        new NonStrictExpectations() {{
            userDetails.getUsername(); result = username;
        }};
        assumeAuthenticatedUser();

        // when, then:
        MVC.mock( controller ).
            perform( get( ACCESS_DENIED ) ).
            andExpect( status().isOk() ).
            andExpect( view().name( ACCESS_DENIED ) ).
            andExpect( model().attribute( "user", equalTo( username ) ) );
    }

    @Test
    public void logoutRequestLogsOutAndRedirectsToLogin(
        @Mocked SecurityContextLogoutHandler secHandler )
        throws Exception
    {
        // given:
        String redirectionUrl = LOGGED_OUT.replaceFirst( "redirect:", "" );
        assumeAuthenticatedUser();

        // when, then:
        MVC.mock( controller ).
            perform( get( LOGOUT ) ).
            andExpect( status().isFound() ).
            andExpect( redirectedUrl( redirectionUrl ) );

        new Verifications() {{
            secHandler.logout( (HttpServletRequest) any, (HttpServletResponse) any, authentication );
                times = 1;
        }};
    }
}
