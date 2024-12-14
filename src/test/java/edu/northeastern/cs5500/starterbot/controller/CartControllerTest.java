package edu.northeastern.cs5500.starterbot.controller;

import static com.google.common.truth.Truth.assertThat;

import edu.northeastern.cs5500.starterbot.model.Cart;
import edu.northeastern.cs5500.starterbot.model.Order;
import edu.northeastern.cs5500.starterbot.repository.InMemoryRepository;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class CartControllerTest {
    static final String discord_ID_1 = "935049484717678613";
    static final String discord_ID_2 = "900203549529628722";
    static final String discord_ID_3 = "1234";

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

    private ShoppingCartController getShoppingCartController() {

        InMemoryRepository<Cart> cartRepository = new InMemoryRepository<>();

        order1.setName("dish1");
        order1.setPrice(1366);
        order1.setQuantity(1);

        order2.setName("dish2");
        order2.setQuantity(2);
        order2.setPrice(1099);

        cart1.add(order1);
        cart2.add(order1);
        cart2.add(order2);

        shoppingCart1.setDiscordUserId(discord_ID_1);
        shoppingCart1.setRestaurantName(RESTAURANT_1);
        shoppingCart1.setShoppingCart(cart1);

        shoppingCart2.setDiscordUserId(discord_ID_2);
        shoppingCart2.setRestaurantName(RESTAURANT_2);
        shoppingCart2.setShoppingCart(cart2);

        shoppingCart3.setDiscordUserId(discord_ID_3);
        shoppingCart3.setRestaurantName(RESTAURANT_3);
        shoppingCart3.setShoppingCart(cart3);

        cartRepository.add(shoppingCart1);
        cartRepository.add(shoppingCart2);

        ShoppingCartController shoppingCartController = new ShoppingCartController(cartRepository);
        return shoppingCartController;
    }

    @Test
    void testGetShoppingCartByDiscordId() {

        ShoppingCartController shoppingCartController = getShoppingCartController();
        assertThat(
                shoppingCartController
                        .getShoppingCartByDiscordId(discord_ID_1)
                        .equals(shoppingCart1));
        assertThat(
                !shoppingCartController
                        .getShoppingCartByDiscordId(discord_ID_1)
                        .equals(shoppingCart2));
        assertThat(
                shoppingCartController
                        .getShoppingCartByDiscordId(discord_ID_3)
                        .equals(shoppingCart3));
        assertThat(
                !shoppingCartController
                        .getShoppingCartByDiscordId(discord_ID_3)
                        .equals(shoppingCart2));
    }

    @Test
    void testFormatCart() {
        ShoppingCartController shoppingCartController = getShoppingCartController();
        Cart cart1 = shoppingCartController.getShoppingCartByDiscordId(discord_ID_1);
        String result = shoppingCartController.formatCart(cart1);
        System.out.println(result);
        String expected =
                "Your order at Toulouse Petit is shown as follows: "
                        + "\n"
                        + "x1"
                        + " "
                        + "dish1"
                        + " "
                        + "$13"
                        + "\n"
                        + "Your total price of current order is 13";
        assertThat(result.equals(expected));
        assertThat(!result.equals(null));
        assertThat(!result.equals("1234"));
    }
}
