package edu.northeastern.cs5500.starterbot.controller;

import com.mongodb.lang.Nullable;
import edu.northeastern.cs5500.starterbot.model.Cart;
import edu.northeastern.cs5500.starterbot.model.Order;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ShoppingCartController {

    GenericRepository<Cart> shoppingCartRepository;

    /**
     * Inject the user cart from the MongoDB repository
     *
     * @param shoppingCartRepository - representing the cart's repository in Generic Repository.
     */
    @Inject
    ShoppingCartController(GenericRepository<Cart> shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;

        if (shoppingCartRepository.count() == 0) {
            Cart cart = new Cart();
            cart.setDiscordUserId("1234");
            cart.setRestaurantName("first_restaurant");
            cart.setShoppingCart(new ArrayList<Order>());
            shoppingCartRepository.add(cart);
        }
    }

    /**
     * Set an empty cart per the user selected on the restaurant name.
     *
     * @param discordUserId - String, representing user's discordUserId
     * @param title - String, representing restaurant name that user type in, could be part or the
     *     full name.
     * @return the shopping cart for this user, if the user already has a shopping cart, will update
     *     this cart to the new restaurant.
     */
    public GenericRepository<Cart> setRestaurantNameForCart(String discordUserId, String title) {
        Collection<Cart> userCarts = shoppingCartRepository.getAll();
        for (Cart currentUserCart : userCarts) {
            if (currentUserCart.getDiscordUserId().equals(discordUserId)) {
                currentUserCart.setRestaurantName(title);
                currentUserCart.setShoppingCart(new ArrayList<Order>());
                shoppingCartRepository.update(currentUserCart);
                return shoppingCartRepository;
            }
        }
        Cart cart = new Cart();
        cart.setDiscordUserId(discordUserId);
        cart.setRestaurantName(title);
        cart.setShoppingCart(new ArrayList<Order>());
        shoppingCartRepository.add(cart);
        return shoppingCartRepository;
    }

    /**
     * This method is to return the restaurant name for this shopping cart
     *
     * @param discordMemberId - String, representing user's discordUserId
     * @return the restaurant name regarding this shopping cart.
     */
    @Nonnull
    public String getRestaurantNameForCart(String discordMemberId) {
        return getShoppingCartForMemberId(discordMemberId).getRestaurantName();
    }

    /**
     * This method is to show the shopping cart for the user
     *
     * @param discordMemberId - String, representing user's discordUserId
     * @return the user's shopping cart based on their memberid.
     */
    @Nullable
    public Cart getShoppingCartForMemberId(String discordMemberId) {
        Collection<Cart> userCarts = shoppingCartRepository.getAll();
        for (Cart currentUserCart : userCarts) {
            if (currentUserCart.getDiscordUserId().equals(discordMemberId)) {
                return currentUserCart;
            }
        }

        return null;
    }

    /**
     * This method is to set order to the shopping cart.
     *
     * @param discordMemberId - String, representing user's discordUserId
     * @param dishName - String, representing the dish name that user ordered
     * @param quantity - String, representing the dish quantity.
     * @param price - String, representing the dish price.
     * @return the updating cart adding all the orders together.
     */
    @Nonnull
    public GenericRepository<Cart> setOrderToShoppingCart(
            String discordMemberId, String dishName, Integer quantity, Integer price) {
        Cart userCart = getShoppingCartForMemberId(discordMemberId);
        ArrayList<Order> orderList = userCart.getShoppingCart();
        for (Order o : orderList) {
            if (o.getName().equals(dishName)) {
                o.setQuantity(o.getQuantity() + quantity);
                shoppingCartRepository.update(userCart);
                return shoppingCartRepository;
            }
        }
        Order order = new Order();
        order.setName(dishName);
        order.setPrice(price);
        order.setQuantity(quantity);
        orderList.add(order);
        shoppingCartRepository.update(userCart);
        return shoppingCartRepository;
    }

    /**
     * This method will match the discord id with shopping cart info in the db, and return the Cart
     * as cart class or null if not exist;
     *
     * @param discordID discord user id
     * @return cart or null
     */
    @Nonnull
    public Cart getShoppingCartByDiscordId(String discordID) {
        Collection<Cart> carts = shoppingCartRepository.getAll();

        for (Cart cart : carts) {
            if (discordID.equals(cart.getDiscordUserId())) {
                return cart;
            }
        }
        Cart cart1 = new Cart();
        cart1.setDiscordUserId(discordID);
        cart1.setRestaurantName("");
        cart1.setShoppingCart(new ArrayList<Order>());
        shoppingCartRepository.add(cart1);

        return cart1;
    }

    /**
     * this method is to format the return of the cart, that way user could see the information more
     * clearly;
     *
     * @param cart object
     * @return string of list of orders with total prices
     */
    public String formatCart(Cart cart) {
        StringBuilder sb = new StringBuilder();
        ArrayList<Order> shoppingCartInfo = cart.getShoppingCart();
        Double total = 0.00;
        for (Order dish : shoppingCartInfo) {
            Integer quantity = dish.getQuantity();
            String dishName = dish.getName();
            Double subTotal = (double) quantity * dish.getPrice() / 100;
            total += subTotal;

            sb.append("x" + quantity + "   " + dishName + "   " + "$" + subTotal + "\n");
        }

        sb.append("\n" + "Your total price of current order is " + "$" + total + ".");

        return sb.toString();
    }

    /**
     * This method is to reset user's shopping cart
     *
     * @param discordID user's discord id
     * @return the cart repos
     */
    @Nonnull
    public GenericRepository<Cart> resetShoppingCartByDiscordId(String discordID) {
        Collection<Cart> carts = shoppingCartRepository.getAll();

        for (Cart cart : carts) {
            if (cart.getDiscordUserId().equals(discordID)) {
                cart.setRestaurantName("");
                cart.setShoppingCart(new ArrayList<Order>());
                shoppingCartRepository.update(cart);
            }
        }
        return shoppingCartRepository;
    }

    /**
     * this method is to get the total price of the order
     *
     * @param cart - Cart, representing the all the cart info.
     * @return the total prices
     */
    @Nonnull
    public Double getTotalFromShoppingCart(Cart cart) {
        ArrayList<Order> shoppingCartInfo = cart.getShoppingCart();
        Double total = 0.00;
        for (Order dish : shoppingCartInfo) {
            Integer quantity = dish.getQuantity();
            Double subTotal = (double) (quantity * dish.getPrice() / 100);
            total += subTotal;
        }

        return total;
    }
}
