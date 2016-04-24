package net.jarl.kata.currencyzoo.controller;

import static net.jarl.kata.currencyzoo.view.Views.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests involving the authentication and authorization of users.
 *
 * @author José A. Romero L.
 */
@Controller
public class AuthenticationController {

    @RequestMapping(value=LOGIN)
    public String loginPage() {
        return LOGIN;
    }

    @RequestMapping(value=ACCESS_DENIED, method=RequestMethod.GET)
    public String accessDenied( ModelMap model ) {
        model.addAttribute( "user", getUsername() );
        return ACCESS_DENIED;
    }

    @RequestMapping(value=LOGOUT, method=RequestMethod.GET)
    public String logoutPage( HttpServletRequest request, HttpServletResponse response ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if( auth != null ) {
            new SecurityContextLogoutHandler().logout( request, response, auth );
        }
        return LOGGED_OUT;
    }

    private String getUsername(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails)principal).getUsername();
        }
        return principal.toString();
    }
}
