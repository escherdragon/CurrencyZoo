package net.jarl.kata.currencyzoo.form;

import lombok.Data;

@Data
public class CalculationForm {
    private String from;
    private String to;
    private String amount;
}
