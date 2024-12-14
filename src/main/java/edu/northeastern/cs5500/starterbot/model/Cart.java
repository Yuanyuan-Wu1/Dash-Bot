package edu.northeastern.cs5500.starterbot.model;

import java.util.ArrayList;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class Cart implements Model {
    protected ObjectId id;

    // user's discordId
    protected String discordUserId;

    // restaurant name
    protected String restaurantName;

    // shopping cart object to store infos
    protected ArrayList<Order> shoppingCart;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        ArrayList<Order> shoppingCartInfo = shoppingCart;
        Double total = 0.00;
        sb.append(String.format("Restaurant: %s\n", restaurantName));
        for (Order dish : shoppingCartInfo) {
            Integer quantity = dish.getQuantity();
            String dishName = dish.getName();
            Double subTotal = (double) quantity * dish.getPrice() / 100;
            total += subTotal;
            sb.append(" :fork_knife_plate:  x");
            sb.append(quantity);
            sb.append("   ");
            sb.append(dishName);
            sb.append("$");
            sb.append(subTotal);
            sb.append("\n");
        }

        sb.append("Total price: " + "$" + String.format("%.2f", total) + ".");

        return sb.toString();
    }
}
