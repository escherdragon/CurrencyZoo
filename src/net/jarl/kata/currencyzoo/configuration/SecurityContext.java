package net.jarl.kata.currencyzoo.configuration;

import static net.jarl.kata.currencyzoo.view.Views.ACCESS_DENIED;
import static net.jarl.kata.currencyzoo.view.Views.CALCULATOR;
import static net.jarl.kata.currencyzoo.view.Views.LOGIN;
import static net.jarl.kata.currencyzoo.view.Views.SIGNUP;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Configuration
public class SecurityContext extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService userDetailsService() {
        return new JpaUserDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Autowired
    public void configureGlobalSecurity( AuthenticationManagerBuilder auth ) throws Exception {
        auth.
            userDetailsService( userDetailsService() ).
            passwordEncoder( passwordEncoder() );
    }

    @Override
    protected void configure( HttpSecurity http ) throws Exception {
        http.authorizeRequests().
            antMatchers( SIGNUP ).permitAll().
            antMatchers( "/", CALCULATOR + "*" ).access( hasRole(Roles.USER) ).
            and().formLogin().loginPage( LOGIN ).
            and().csrf().
            and().exceptionHandling().accessDeniedPage( ACCESS_DENIED );
    }
 
    private String hasRole( String roleName ) {
        return String.format( "hasRole('%s')", roleName );
    }
}
