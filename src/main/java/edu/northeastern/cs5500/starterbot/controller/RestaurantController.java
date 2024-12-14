package edu.northeastern.cs5500.starterbot.controller;

import com.mongodb.lang.Nullable;
import edu.northeastern.cs5500.starterbot.model.Dish;
import edu.northeastern.cs5500.starterbot.model.Restaurants;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import edu.northeastern.cs5500.starterbot.service.PostalCodeService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RestaurantController {

    GenericRepository<Restaurants> restaurantRepository;

    /**
     * Inject the restaurants from the MongoDB repository
     *
     * @param restaurantRepository - representing the restaurant's repository in Generic Repository.
     */
    @Inject
    public RestaurantController(GenericRepository<Restaurants> restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    /**
     * Get the restaurant info based on the users' choice.
     *
     * @param title - String, representing restaurant name that user type in, could be part or the
     *     full name.
     * @return - the restaurant info.
     */
    @Nullable
    public Restaurants getRestaurantForUser(String title, String postalCode) {
        PostalCodeService postalCodeService = new PostalCodeService();

        List<String> postalCodeSet = postalCodeService.getNearbyPostalCodes(postalCode, 10);

        Collection<Restaurants> restaurants =
                restaurantRepository.getAll().stream()
                        .filter(restaurant -> postalCodeSet.contains(restaurant.getPostalCode()))
                        .collect(Collectors.toList());

        for (Restaurants currRestaurant : restaurants) {
            if (currRestaurant.getTitle().toLowerCase().contains(title.toLowerCase())) {
                return currRestaurant;
            }
        }
        return null;
    }

    /**
     * Get the menu list of the restaurant
     *
     * @param title - String, representing restaurant name based on user choice.
     * @return - the menu of the selected restaurant.
     */
    @Nonnull
    public String getMenuForUser(String title, String postalCode) {
        StringBuilder sb = new StringBuilder();
        if (getRestaurantForUser(title, postalCode) == null) {
            return "This restaurant cannot be delivered.";
        } else {
            ArrayList<Dish> menuList = getRestaurantForUser(title, postalCode).getMenu();
            for (int i = 0; i < menuList.size(); i++) {
                sb.append(
                        String.valueOf(i + 1)
                                + ".  "
                                + menuList.get(i).getTitle()
                                + ": "
                                + menuList.get(i).getText()
                                + "\n");
            }
            return sb.toString();
        }
    }

    /**
     * Get the hashmap of the menu for ordering
     *
     * @param title - String, representing restaurant name based on user choice.
     * @return - the menu as a hashmap, key is the menu number, value is the dish info with its name
     *     and price.
     */
    @Nonnull
    public Map<String, List<String>> getMenuToOrder(String title, String postalCode) {
        HashMap<String, List<String>> menuMap = new HashMap<>();
        ArrayList<Dish> menuList = getRestaurantForUser(title, postalCode).getMenu();
        for (int i = 0; i < menuList.size(); i++) {
            List<String> dishInfo = new ArrayList<>();
            dishInfo.add(menuList.get(i).getTitle());
            dishInfo.add(String.valueOf(menuList.get(i).getPrice()));
            menuMap.put(String.valueOf(i + 1), dishInfo);
        }

        return menuMap;
    }

    /**
     * This method is to get restaurant zipcode using restaurant name
     *
     * @param restaurantName the restaurant name
     * @return the restaurant zipcode
     */
    @Nullable
    public String getRestaurantZipCode(String restaurantName) {

        Collection<Restaurants> restaurants = restaurantRepository.getAll();

        for (Restaurants currRestaurant : restaurants) {
            if (currRestaurant.getTitle().equals(restaurantName)) {
                return currRestaurant.getPostalCode();
            }
        }
        return null;
    }
}
