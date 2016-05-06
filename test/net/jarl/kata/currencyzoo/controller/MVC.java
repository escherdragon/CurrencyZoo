package net.jarl.kata.currencyzoo.controller;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

public class MVC {
    private MVC() {}
    
    public static MockMvc mock( Object controller ) {
        return MockMvcBuilders.
            standaloneSetup( controller ).
            setViewResolvers( viewResolver() ).
            build();
    }

    private static ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver =
            new InternalResourceViewResolver();

        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");

        return viewResolver;
    }
}
