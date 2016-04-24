package net.jarl.kata.currencyzoo.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.*;

@Entity
@Table(name="Roles", indexes={
    @Index(columnList="user_id, role", unique=true, name="idx_Roles")
})
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString(exclude={"user"})
public class Role implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @ManyToOne
    @JoinColumn(name="user_id")
    @Getter
    private User user;

    @Id
    @Column(name="role", nullable=false)
    @Getter
    private String roleName;
}
