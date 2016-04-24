package net.jarl.kata.currencyzoo.controller;

import static net.jarl.kata.currencyzoo.view.Views.SIGNED_UP;
import static net.jarl.kata.currencyzoo.view.Views.SIGNUP;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.jarl.kata.currencyzoo.form.UserForm;
import net.jarl.kata.currencyzoo.model.User;
import net.jarl.kata.currencyzoo.model.UserRepository;

/**
 * Handles requests involving the registration of new users.
 *
 * @author José A. Romero L.
 */
@Controller
public class RegistrationController {

    @Autowired
    UserRepository userRepo;

    @RequestMapping(value=SIGNUP, method=RequestMethod.GET)
    public String signUpPage() {
        return SIGNUP;
    }

    @RequestMapping(value=SIGNUP, method=RequestMethod.POST)
    public String signUp( @Valid UserForm form, BindingResult result, ModelMap model )
    {
        form.validate( result );

        if( result.hasErrors() ) {
            model.addAttribute( "form", form );
            model.addAttribute( "errors", result );
            return SIGNUP;
        }

        User newUser = form.toModelUser();
        userRepo.saveAndFlush( newUser );

        return SIGNED_UP;
    }

    @InitBinder
    public void initBinder( WebDataBinder binder ) {
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
        sdf.setLenient( true );
        binder.registerCustomEditor( Date.class, new SilentDateEditor( sdf, true ) );
    }
 
    private class SilentDateEditor extends CustomDateEditor {
        public SilentDateEditor( DateFormat dateFormat, boolean allowEmpty ) {
            super( dateFormat, allowEmpty );
        }
        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            try {
                super.setAsText( text );
            }
            catch( IllegalArgumentException e ) {}
        }
    } 
}
