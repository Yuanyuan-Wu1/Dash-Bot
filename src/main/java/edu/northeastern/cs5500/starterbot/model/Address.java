package edu.northeastern.cs5500.starterbot.model;

import javax.annotation.Nullable;
import lombok.Data;

@Data
public class Address {
    // user delivery address
    protected String address;

    // user delivery city
    protected String city;

    // user delivery state
    protected String state;

    // user delivery message
    @Nullable protected String message;

    @Override
    public String toString() {
        if (message == null) {
            return String.format("\nAddress: %s\nCity: %s\nState: %s", address, city, state);
        }
        return String.format(
                "\nAddress: %s\nCity: %s\nState: %s\nMessage: %s", address, city, state, message);
    }
}
