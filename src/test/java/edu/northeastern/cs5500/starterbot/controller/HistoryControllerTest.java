package edu.northeastern.cs5500.starterbot.controller;

import static com.google.common.truth.Truth.assertThat;

import edu.northeastern.cs5500.starterbot.model.Address;
import edu.northeastern.cs5500.starterbot.model.Cart;
import edu.northeastern.cs5500.starterbot.model.History;
import edu.northeastern.cs5500.starterbot.model.HistoryOrder;
import edu.northeastern.cs5500.starterbot.model.Order;
import edu.northeastern.cs5500.starterbot.model.Payment;
import edu.northeastern.cs5500.starterbot.model.Status;
import edu.northeastern.cs5500.starterbot.repository.InMemoryRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class HistoryControllerTest {
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

    private HistoryController getHistoryController() {

        InMemoryRepository<History> historyRepository = new InMemoryRepository<>();

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

        HistoryController historyController = new HistoryController(historyRepository);
        return historyController;
    }

    @Test
    void testSetHistoryForUser1() {

        HistoryController historyController = getHistoryController();
        ArrayList<HistoryOrder> historyOrder = new ArrayList<>();
        historyOrder.add(historyOrder1);
        historyController.setHistoryForUser(DISCORD_ID_1, historyOrder1);

        assertThat(
                historyController
                        .getHistoryForUser(DISCORD_ID_1)
                        .getHistoryOrder()
                        .equals(historyOrder));
    }

    @Test
    void testSetHistoryForUser2() {

        HistoryController historyController = getHistoryController();
        ArrayList<HistoryOrder> historyOrder = new ArrayList<>();
        historyOrder.add(historyOrder1);

        HistoryOrder historyOrder2 = new HistoryOrder();
        historyOrder2.setAddress(address_1);
        historyOrder2.setPayment(payment_1);
        historyOrder2.setShoppingCart(shoppingCart2);
        historyOrder2.setStatus(Status.DELIVERED);
        historyOrder2.setTotal(25.99);
        historyOrder.add(historyOrder2);

        historyController.setHistoryForUser(DISCORD_ID_1, historyOrder1);
        historyController.setHistoryForUser(DISCORD_ID_1, historyOrder2);

        assertThat(historyController.getHistoryForUser(DISCORD_ID_1).getHistoryOrder().size() == 2);
    }

    @Test
    void testGetHistoryForUser() {

        HistoryController historyController = getHistoryController();

        assertThat(historyController.getHistoryForUser(DISCORD_ID_1).getHistoryOrder().size() == 0);
    }

    @Test
    void testSetHistoryOrderForUser() {
        HistoryController historyController = getHistoryController();
        HistoryOrder historyOrder1 =
                historyController.setHistoryOrderForUser(
                        shoppingCart1,
                        address_1,
                        payment_1,
                        55.55,
                        LocalDateTime.now(),
                        Status.IN_PROGRESS);
        assertThat(historyOrder1.getAddress().equals(address_1));
        assertThat(historyOrder1.getPayment().equals(payment_1));
        assertThat(historyOrder1.getShoppingCart().equals(shoppingCart1));
    }
}
