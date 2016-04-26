package net.jarl.kata.currencyzoo.model;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QueryRepository extends JpaRepository<Query, Long> {

    List<Query> findAllByUserLoginOrderByTimestampDesc( String login, Pageable page );
}
