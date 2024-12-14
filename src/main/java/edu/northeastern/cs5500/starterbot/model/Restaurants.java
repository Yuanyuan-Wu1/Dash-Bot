package edu.northeastern.cs5500.starterbot.model;

import java.util.ArrayList;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class Restaurants implements Model {

    protected ObjectId id;

    // restaurant name
    protected String title;

    // restaurant id
    protected String uuid;

    // restaurant address
    protected String streetAddress;

    // the city that the restaurant located
    protected String city;

    // restaurant zip code
    protected String postalCode;

    // the state that the restaurant located
    protected String region;

    // the related keyword that helps user to find the restaurant
    protected ArrayList<String> categories;

    // restaurant menu
    protected ArrayList<Dish> menu;
}
