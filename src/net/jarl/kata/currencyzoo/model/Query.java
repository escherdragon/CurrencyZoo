package net.jarl.kata.currencyzoo.model;

import static javax.persistence.GenerationType.AUTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.*;

import lombok.*;

@Entity
@Table(name="Queries")
@NoArgsConstructor
@RequiredArgsConstructor
public class Query {

    @Id
    @GeneratedValue(strategy=AUTO)
    @Getter @Setter
    private long id;

    @ManyToOne(optional=false)
    @NonNull @Getter @Setter
    private User user;

    @Column(name="from_symbol", nullable=false)
    @NonNull @Getter @Setter
    private String from;

    @Column(name="to_symbol", nullable=false)
    @NonNull @Getter @Setter
    private String to;

    @Column(name="amount", nullable=false)
    @NonNull @Getter @Setter
    private BigDecimal amount;

    @Column(name="result", nullable=false)
    @NonNull @Getter @Setter
    private BigDecimal result;

    @Column(name="timestamp", nullable=false)
    @Getter
    private LocalDateTime timestamp;

    @PrePersist
    private void prePersist() {
        timestamp = LocalDateTime.now();
    }
}
