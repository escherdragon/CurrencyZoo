package net.jarl.kata.currencyzoo.data;

public class ConversionException extends Exception {
    private static final long serialVersionUID = 1L;

    public ConversionException( String message ) {
        super( message );
    }
}
