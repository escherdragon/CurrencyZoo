package net.jarl.kata.currencyzoo.configuration;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.filter.CharacterEncodingFilter;

@Order(1)
public class CharacterEncodingInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup( ServletContext ctx ) throws ServletException {

        FilterRegistration.Dynamic encodingFilter =
            ctx.addFilter( "encoding-filter", new CharacterEncodingFilter() );
        encodingFilter.setInitParameter( "encoding", "UTF-8" );
        encodingFilter.setInitParameter( "forceEncoding", "yes" );
        encodingFilter.addMappingForUrlPatterns( null, false, "/*" );
    }

}
