package net.jarl.kata.currencyzoo.form;

import java.util.Date;

import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;

import lombok.Data;
import net.jarl.kata.currencyzoo.configuration.Roles;
import net.jarl.kata.currencyzoo.model.User;

@Data
@Validated
public class UserForm {

    @Size(min=1, max=100)
    private String username;

    @Size(min=8, max=100)
    private String password;

    private String passwordCopy;

    @Size(max=100)
    private String email;

    @Past
    private Date dateOfBirth;

    @Size(min=1, max=255)
    private String street;

    @Size(min=1, max=255)
    private String zipCode;

    @Size(min=1, max=255)
    private String city;

    @Size(min=1, max=255)
    private String country;

    public User toModelUser() {
        User user = new User();
        PasswordEncoder encoder = new BCryptPasswordEncoder();

        user.setLogin( username );
        user.setPassword( encoder.encode( password ) );
        user.assignRole( Roles.USER );
        return user;
    }
    
    public void validate( Errors errors, boolean loginUsed ) {
        Validator validator = new UserFormValidator( loginUsed );
        validator.validate( this, errors );
    }
}
