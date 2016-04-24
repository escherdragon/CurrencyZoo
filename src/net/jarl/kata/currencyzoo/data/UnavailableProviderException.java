package net.jarl.kata.currencyzoo.data;

public class UnavailableProviderException extends Exception {
    private static final long serialVersionUID = 1L;
 
    public UnavailableProviderException( String message, Throwable reason ) {
        super( message, reason );
    }
}
