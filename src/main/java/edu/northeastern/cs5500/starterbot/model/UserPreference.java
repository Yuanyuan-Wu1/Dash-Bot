package edu.northeastern.cs5500.starterbot.model;

import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class UserPreference implements Model {
    protected ObjectId id;

    // This is the "snowflake id" of the user
    // e.g. event.getUser().getId()
    protected String discordUserId;

    // This is the "discord name" of the user
    // e.g. event.getUser().getName()
    protected String discordUserName;

    // user postal code
    protected String postalCode;

    // user's address info
    protected Address address;

    // user's payment info
    protected Payment payment;
}
