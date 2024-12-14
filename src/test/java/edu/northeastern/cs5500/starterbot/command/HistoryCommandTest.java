package edu.northeastern.cs5500.starterbot.command;

import static com.google.common.truth.Truth.assertThat;

import edu.northeastern.cs5500.starterbot.model.Address;
import edu.northeastern.cs5500.starterbot.model.Cart;
import edu.northeastern.cs5500.starterbot.model.History;
import edu.northeastern.cs5500.starterbot.model.HistoryOrder;
import edu.northeastern.cs5500.starterbot.model.Order;
import edu.northeastern.cs5500.starterbot.model.Payment;
import edu.northeastern.cs5500.starterbot.model.Status;
import java.awt.Color;
import java.time.LocalDateTime;
import java.util.ArrayList;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

public class HistoryCommandTest {

    static final String DISCORD_ID_1 = "935049484717678613";
    static final String DISCORD_ID_2 = "900203549529628722";
    static final String DISCORD_ID_3 = "1234";

    static final String RESTAURANT_1 = "Toulouse Petit";
    static final String RESTAURANT_2 = "Panera Bread";
    static final String RESTAURANT_3 = "first_restaurant";

    static final ArrayList<Order> cart1 = new ArrayList<>();
    static final ArrayList<Order> cart2 = new ArrayList<>();
    static final ArrayList<Order> cart3 = new ArrayList<>();

    static final Order order1 = new Order();
    static final Order order2 = new Order();

    static final Cart shoppingCart1 = new Cart();
    static final Cart shoppingCart2 = new Cart();
    static final Cart shoppingCart3 = new Cart();

    static final Address address_1 = new Address();

    static final Payment payment_1 = new Payment();

    static final HistoryOrder historyOrder1 = new HistoryOrder();

    @Test
    void testNameMatchesData() {
        HistoryCommand historyCommand = new HistoryCommand();

        String name = historyCommand.getName();
        CommandData commandData = historyCommand.getCommandData();

        assertThat(name).isEqualTo(commandData.getName());
    }

    @Test
    void testEmbedMessageWithNoHistoryOrders() {
        HistoryCommand historyCommand = new HistoryCommand();

        History history = new History();

        MessageEmbed embed1 = historyCommand.reviewHistory(history);
        assertThat(embed1.getTitle()).isNotEmpty();
        assertThat(embed1.getTitle()).isEqualTo("Hi, this is a summary of your history order: ");
        assertThat(embed1.getFields().size()).isEqualTo(0);
        assertThat(embed1.getColor()).isEqualTo(Color.YELLOW);
        assertThat(embed1.getFooter()).isNotNull();
    }

    @Test
    void testEmbedMessageWithHistoryOrder() {
        order1.setName("dish1");
        order1.setPrice(1366);
        order1.setQuantity(1);

        order2.setName("dish2");
        order2.setQuantity(2);
        order2.setPrice(1099);

        cart1.add(order1);
        cart2.add(order1);
        cart2.add(order2);

        shoppingCart1.setDiscordUserId(DISCORD_ID_1);
        shoppingCart1.setRestaurantName(RESTAURANT_1);
        shoppingCart1.setShoppingCart(cart1);
        shoppingCart1.setId(new ObjectId());

        shoppingCart2.setDiscordUserId(DISCORD_ID_2);
        shoppingCart2.setRestaurantName(RESTAURANT_2);
        shoppingCart2.setShoppingCart(cart2);

        shoppingCart3.setDiscordUserId(DISCORD_ID_3);
        shoppingCart3.setRestaurantName(RESTAURANT_3);
        shoppingCart3.setShoppingCart(cart3);

        address_1.setAddress("111");
        address_1.setCity("city");
        address_1.setState("state");
        address_1.setMessage("message");

        payment_1.setCardNumber("111222333444");
        payment_1.setExpireDate("1234");
        payment_1.setSecurityCode("666");

        historyOrder1.setAddress(address_1);
        historyOrder1.setPayment(payment_1);
        historyOrder1.setShoppingCart(shoppingCart1);
        historyOrder1.setStatus(Status.DELIVERED);
        historyOrder1.setTotal(13.66);
        historyOrder1.setDate(LocalDateTime.now());

        HistoryCommand historyCommand = new HistoryCommand();

        History history = new History();

        history.setHistoryOrder(
                new ArrayList<HistoryOrder>() {
                    {
                        add(historyOrder1);
                    }
                });

        MessageEmbed embed1 = historyCommand.reviewHistory(history);
        assertThat(embed1.getTitle()).isNotEmpty();
        assertThat(embed1.getTitle()).isEqualTo("Hi, this is a summary of your history order: ");
        assertThat(embed1.getFields().size()).isEqualTo(1);
        assertThat(embed1.getColor()).isEqualTo(Color.YELLOW);
        assertThat(embed1.getFooter()).isNotNull();
    }
}
