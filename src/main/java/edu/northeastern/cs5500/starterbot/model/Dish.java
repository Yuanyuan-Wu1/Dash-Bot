package edu.northeastern.cs5500.starterbot.model;

import lombok.Data;

@Data
public class Dish {

    // dish id
    protected String uuid;

    // dish name
    protected String title;

    // dish price, shows in interger for the system to calculate
    protected Integer price;

    // dish price that shows on the menu for user to check
    protected String text;
}
