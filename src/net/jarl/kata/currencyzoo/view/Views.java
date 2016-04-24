package net.jarl.kata.currencyzoo.view;

public final class Views {
    public static final String LOGIN = "/login";
    public static final String LOGOUT = "/logout";
    public static final String LOGGED_OUT = "redirect:/login?logout";
    public static final String BAD_CREDENTIALS = "/login?error";
    public static final String ACCESS_DENIED = "/accessDenied";

    public static final String SIGNUP = "/signUp";
    public static final String SIGNED_UP = "redirect:/login?signed_up";
    public static final String BAD_SIGNUP_DATA = "redirect:/signUp?error";

    public static final String CALCULATOR = "/calculator";
}
