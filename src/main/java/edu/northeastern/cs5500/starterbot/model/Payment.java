package edu.northeastern.cs5500.starterbot.model;

import lombok.Data;

@Data
public class Payment {
    // user card info.
    protected String cardNumber;

    // user card's expire date
    protected String expireDate;

    // user card's security code
    protected String securityCode;

    @Override
    public String toString() {
        return String.format(
                "\n card number: %s\n expiration date: %s\n security code: %s",
                cardNumber, expireDate, securityCode);
    }
}
