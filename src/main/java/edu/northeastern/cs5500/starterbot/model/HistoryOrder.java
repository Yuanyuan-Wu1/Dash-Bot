package edu.northeastern.cs5500.starterbot.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class HistoryOrder implements Comparable<HistoryOrder> {
    protected ObjectId id;

    // the shopping cart
    protected Cart shoppingCart;

    // the delivered address of this order
    protected Address address;

    // the payment method of this order
    protected Payment payment;

    // total price of the order
    protected Double total;

    // time to place the order
    protected LocalDateTime date;

    // the order status
    protected Status status;

    @Override
    public String toString() {
        return String.format(
                "Order Number: %s\nDate: %s\n %s\nAddress:truck:: %s\nPayment:credit_card: : %s\nStatus: %s\n",
                shoppingCart.id.toString(),
                DateTimeFormatter.ofPattern("MM/dd/yyyy 'at' hh:mm a").format(this.date),
                shoppingCart.toString(),
                address.toString(),
                payment.toString(),
                status.toString());
    }

    @Override
    public int compareTo(HistoryOrder compareToOrder) {
        if (this.date.isBefore(compareToOrder.getDate())) return 1;
        return -1;
    }
}
