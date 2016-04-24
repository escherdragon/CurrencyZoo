package net.jarl.kata.currencyzoo.configuration;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import net.jarl.kata.currencyzoo.model.Role;
import net.jarl.kata.currencyzoo.model.User;
import net.jarl.kata.currencyzoo.model.UserRepository;

/**
 * Allows to retrieve details on registered users for authentication.
 *
 * @author José A. Romero L.
 */
public class JpaUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername( String username )
        throws UsernameNotFoundException
    {
        return userRepository.findByLogin( username ).
            map( this::toUserDetails ).
            orElseThrow( () -> new UsernameNotFoundException( username ) );
    }
    
    private UserDetails toUserDetails( User user ) {
        Set<GrantedAuthority> authorities = user.getRoles().stream().
            map( Role::getRoleName ).
            map( SimpleGrantedAuthority::new ).
            collect( Collectors.toSet() );

        return new org.springframework.security.core.userdetails.User(
            user.getLogin(),
            user.getPassword(),
            authorities );
    }
}
