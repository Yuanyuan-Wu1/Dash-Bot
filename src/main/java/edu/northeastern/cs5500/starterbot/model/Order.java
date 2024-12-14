package edu.northeastern.cs5500.starterbot.model;

import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class Order implements Model {

    protected ObjectId id;

    // dish name
    protected String name;

    // dish quantity
    protected Integer quantity;

    // dish price
    protected Integer price;
}
