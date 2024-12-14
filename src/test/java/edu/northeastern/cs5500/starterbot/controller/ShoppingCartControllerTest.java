package edu.northeastern.cs5500.starterbot.controller;

import static com.google.common.truth.Truth.assertThat;

import edu.northeastern.cs5500.starterbot.model.Cart;
import edu.northeastern.cs5500.starterbot.model.Order;
import edu.northeastern.cs5500.starterbot.repository.InMemoryRepository;
import java.util.ArrayList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ShoppingCartControllerTest {
    static final String USER_ID_1 = "23h5ikoqaehokljhaoe";
    static final String RESTAURANT_1 = "Toulouse Petit";
    static final String DISH_NAME_1 = "Brussels Sprouts Fritti with Piquillo Sriracha Rouille";
    static final Integer DISH_QUANTITY_1 = 1;
    static final Integer DISH_PRICE_1 = 1200;
    static final String DISH_NAME_2 = "Large Housemade Beignets (10 pieces)";
    static final Integer DISH_QUANTITY_2 = 1;
    static final Integer DISH_PRICE_2 = 1600;
    static final Integer DISH_QUANTITY_3 = 2;

    static final String USER_ID_2 = "2kjfksdjdkhokljhaoe";
    static final String RESTAURANT_2 = "Panera Bread";

    private ShoppingCartController getShoppingCartController() {

        ShoppingCartController shoppingCartController =
                new ShoppingCartController(new InMemoryRepository<>());
        return shoppingCartController;
    }

    @Test
    void testSetRestaurantNameForCart() {
        ShoppingCartController shoppingCartController = getShoppingCartController();

        shoppingCartController.setRestaurantNameForCart(USER_ID_1, RESTAURANT_1);
        assertThat(shoppingCartController.getRestaurantNameForCart(USER_ID_1))
                .isEqualTo(RESTAURANT_1);

        shoppingCartController.setRestaurantNameForCart(USER_ID_1, RESTAURANT_2);
        assertThat(shoppingCartController.getRestaurantNameForCart(USER_ID_1))
                .isEqualTo(RESTAURANT_2);
    }

    @Test
    void testGetShoppingCartForMemberId() {
        ShoppingCartController shoppingCartController = getShoppingCartController();

        shoppingCartController.setRestaurantNameForCart(USER_ID_1, RESTAURANT_1);
        assertThat(shoppingCartController.getRestaurantNameForCart(USER_ID_1))
                .isEqualTo(RESTAURANT_1);

        Assertions.assertNull(shoppingCartController.getShoppingCartForMemberId(USER_ID_2));
    }

    @Test
    void testSetOrderToShoppingCart1() {
        ShoppingCartController shoppingCartController = getShoppingCartController();

        shoppingCartController.setRestaurantNameForCart(USER_ID_1, RESTAURANT_1);
        assertThat(shoppingCartController.getRestaurantNameForCart(USER_ID_1))
                .isEqualTo(RESTAURANT_1);
        assertThat(
                        shoppingCartController
                                .getShoppingCartForMemberId(USER_ID_1)
                                .getShoppingCart()
                                .size())
                .isEqualTo(0);

        shoppingCartController.setRestaurantNameForCart(USER_ID_1, RESTAURANT_2);
        assertThat(shoppingCartController.getRestaurantNameForCart(USER_ID_1))
                .isEqualTo(RESTAURANT_2);

        shoppingCartController.setOrderToShoppingCart(
                USER_ID_1, DISH_NAME_1, DISH_QUANTITY_1, DISH_PRICE_1);
        shoppingCartController.setOrderToShoppingCart(
                USER_ID_1, DISH_NAME_2, DISH_QUANTITY_2, DISH_PRICE_2);

        assertThat(
                        shoppingCartController
                                .getShoppingCartForMemberId(USER_ID_1)
                                .getShoppingCart()
                                .size())
                .isEqualTo(2);
    }

    @Test
    void testSetOrderToShoppingCart2() {
        ShoppingCartController shoppingCartController = getShoppingCartController();

        shoppingCartController.setRestaurantNameForCart(USER_ID_1, RESTAURANT_1);

        shoppingCartController.setOrderToShoppingCart(
                USER_ID_1, DISH_NAME_1, DISH_QUANTITY_1, DISH_PRICE_1);
        shoppingCartController.setOrderToShoppingCart(
                USER_ID_1, DISH_NAME_2, DISH_QUANTITY_2, DISH_PRICE_2);
        shoppingCartController.setOrderToShoppingCart(
                USER_ID_1, DISH_NAME_1, DISH_QUANTITY_1, DISH_PRICE_1);
        shoppingCartController.setOrderToShoppingCart(
                USER_ID_1, DISH_NAME_1, DISH_QUANTITY_1, DISH_PRICE_1);

        assertThat(
                        shoppingCartController
                                .getShoppingCartForMemberId(USER_ID_1)
                                .getShoppingCart()
                                .get(0)
                                .getQuantity())
                .isEqualTo(3);
    }

    @Test
    void testResetShoppingCartByDiscordId() {

        InMemoryRepository<Cart> shoppingCartRepository = new InMemoryRepository<>();
        Cart cart1 = new Cart();
        cart1.setDiscordUserId(USER_ID_1);
        cart1.setRestaurantName(RESTAURANT_1);
        cart1.setShoppingCart(new ArrayList<>());
        shoppingCartRepository.add(cart1);

        Cart cart2 = new Cart();
        cart2.setDiscordUserId(USER_ID_2);
        cart2.setRestaurantName(RESTAURANT_2);
        cart2.setShoppingCart(new ArrayList<>());
        shoppingCartRepository.add(cart2);

        ShoppingCartController shoppingCartController =
                new ShoppingCartController(shoppingCartRepository);
        assertThat(shoppingCartRepository.count()).isEqualTo(2);
        assertThat(shoppingCartController.getRestaurantNameForCart(USER_ID_1).equals(RESTAURANT_1));

        shoppingCartController.resetShoppingCartByDiscordId(USER_ID_1);
        assertThat(shoppingCartRepository.count()).isEqualTo(2);
        assertThat(shoppingCartController.getRestaurantNameForCart(USER_ID_1).equals(""));
    }

    @Test
    void testGetTotalFromShoppingCart1() {

        ShoppingCartController shoppingCartController = getShoppingCartController();
        Cart cart1 = new Cart();
        cart1.setDiscordUserId(USER_ID_1);
        cart1.setRestaurantName(RESTAURANT_1);
        cart1.setShoppingCart(new ArrayList<Order>());
        ArrayList<Order> orderList1 = new ArrayList<>();
        Order order1 = new Order();
        order1.setName(DISH_NAME_1);
        order1.setPrice(DISH_PRICE_1);
        order1.setQuantity(DISH_QUANTITY_1);
        orderList1.add(order1);
        Order order2 = new Order();
        order2.setName(DISH_NAME_2);
        order2.setPrice(DISH_PRICE_2);
        order2.setQuantity(DISH_QUANTITY_2);
        orderList1.add(order2);
        Double totalPrice = shoppingCartController.getTotalFromShoppingCart(cart1);

        assertThat(totalPrice == 28.00);
    }

    @Test
    void testGetTotalFromShoppingCart2() {

        ShoppingCartController shoppingCartController = getShoppingCartController();
        Cart cart2 = new Cart();
        cart2.setDiscordUserId(USER_ID_2);
        cart2.setRestaurantName(RESTAURANT_2);
        cart2.setShoppingCart(new ArrayList<Order>());
        ArrayList<Order> orderList1 = new ArrayList<>();
        Order order1 = new Order();
        order1.setName(DISH_NAME_1);
        order1.setPrice(DISH_PRICE_1);
        order1.setQuantity(DISH_QUANTITY_1);
        orderList1.add(order1);
        Order order2 = new Order();
        order2.setName(DISH_NAME_2);
        order2.setPrice(DISH_PRICE_2);
        order2.setQuantity(DISH_QUANTITY_3);
        orderList1.add(order2);
        Order order3 = new Order();
        order3.setName(DISH_NAME_1);
        order3.setPrice(DISH_PRICE_1);
        order3.setQuantity(DISH_QUANTITY_3);
        orderList1.add(order3);

        Double totalPrice = shoppingCartController.getTotalFromShoppingCart(cart2);
        assertThat(totalPrice == 68.00);
    }
}
