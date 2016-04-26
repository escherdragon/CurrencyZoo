package net.jarl.kata.currencyzoo.form;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Complements the JSR-303 declarative validation of {@link UserForm} objects.
 * This validator checks only those fields that cannot be validated using the
 * default (annotations-based) one.
 *
 * @author José A. Romero L.
 */
public class UserFormValidator implements Validator {

    private boolean isLoginUsed = true;

    public UserFormValidator( boolean isLoginUsed ) {
        this.isLoginUsed = isLoginUsed;
    }

    @Override
    public boolean supports( Class<?> clazz ) {
        return UserForm.class.equals( clazz );
    }

    @Override
    public void validate( Object target, Errors errors ) {
        UserForm form = (UserForm) target;
        validateLogin( errors );
        validateEmail( form.getEmail(), errors );
        validateDateOfBirth( form.getDateOfBirth(), errors );
        validatePasswordCopy( form.getPassword(), form.getPasswordCopy(), errors );
    }

    private void validateLogin( Errors errors ) {
        if( isLoginUsed ) {
            errors.rejectValue( "username", "error", "username already in use" );
        }
    }

    private void validateEmail( String email, Errors errors ) {
        EmailValidator validator = EmailValidator.getInstance();
        if( !validator.isValid( email ) ) {
            errors.rejectValue( "email", "error", "invalid e-mail address" );
        }
    }

    private void validateDateOfBirth( Date dateOfBirth, Errors errors ) {
        if( dateOfBirth == null ) {
            errors.rejectValue( "dateOfBirth", "error", "malformed date" );
            return;
        }
        LocalDate local = dateOfBirth.
            toInstant().
            atZone( ZoneId.systemDefault() ).
            toLocalDate();
        int age = Period.between( local, LocalDate.now() ).getYears();
        if( age <= 5 || age >= 120 ) {
            errors.rejectValue( "dateOfBirth", "error", "unreasonable date of birth" );
        }
    }

    private void validatePasswordCopy( String password, String passwordCopy, Errors errors ) {
        if( passwordCopy == null || !passwordCopy.equals( password ) ) {
            errors.rejectValue( "passwordCopy", "error", "passwords don't match" );
        }
    }
}
