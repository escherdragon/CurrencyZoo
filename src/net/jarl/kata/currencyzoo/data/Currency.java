package net.jarl.kata.currencyzoo.data;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode
public class Currency {

    @Getter
    private final String symbol;

    @Getter
    private final String description;
}
