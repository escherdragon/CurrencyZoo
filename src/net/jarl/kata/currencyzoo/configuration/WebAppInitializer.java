package net.jarl.kata.currencyzoo.configuration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

@Order(2)
public class WebAppInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup( ServletContext ctx ) throws ServletException {

        AnnotationConfigWebApplicationContext dispatcherServlet =
            new AnnotationConfigWebApplicationContext();
        dispatcherServlet.register( MVCConfig.class );
 
        ServletRegistration.Dynamic dispatcher = ctx.addServlet( "dispatcher",
            new DispatcherServlet( dispatcherServlet ) );
        dispatcher.setLoadOnStartup( 1 );
        dispatcher.addMapping( "/" );
    }

}
