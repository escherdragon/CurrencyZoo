package net.jarl.kata.currencyzoo.model;

import static javax.persistence.GenerationType.AUTO;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="Users")
@ToString
public class User {

    @Id
    @GeneratedValue(strategy=AUTO)
    @Getter @Setter
    private Long id;

    @Column(name="login", nullable=false, unique=true)
    @Getter @Setter
    private String login;

    @Column(name="password", nullable=false)
    @Getter @Setter
    private String password;

    @Column(name="creation_time", nullable=false)
    @Getter
    private LocalDateTime creationTime;

    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name="user_id")
    @Getter
    private Set<Role> roles;

    @PrePersist
    private void prePersist() {
        creationTime = LocalDateTime.now();
    }

    public synchronized void assignRole( String roleName ) {
        if( roles == null ) {
            roles = new HashSet<Role>();
        }
        roles.add( new Role( this, roleName ) );
    }
}
