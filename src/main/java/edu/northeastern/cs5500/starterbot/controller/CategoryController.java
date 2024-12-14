package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.Restaurants;
import edu.northeastern.cs5500.starterbot.model.UserPreference;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import edu.northeastern.cs5500.starterbot.service.PostalCodeService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CategoryController {

    GenericRepository<Restaurants> restaurantRepository;
    GenericRepository<UserPreference> userPreferenceRepository;

    @Inject
    CategoryController(GenericRepository<Restaurants> restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    /**
     * This method will get all restaurant names that matches the category keyword the user input
     *
     * @param category, discorId
     * @return list of restaurant names that matches the keyword provided.
     */
    @Nonnull
    public ArrayList<String> getRestaurantNameByCategory(String category, String postalCode) {
        ArrayList<String> restaurantList = new ArrayList<>();
        PostalCodeService postalCodeService = new PostalCodeService();
        List<String> postalCodeSet = postalCodeService.getNearbyPostalCodes(postalCode, 10);
        Collection<Restaurants> restaurants = restaurantRepository.getAll();

        for (Restaurants restaurant : restaurants) {
            if (postalCodeSet.contains(restaurant.getPostalCode())) {
                ArrayList<String> categories = restaurant.getCategories();
                for (String caty : categories) {
                    if (caty.toLowerCase().contains(category.toLowerCase())) {
                        restaurantList.add(restaurant.getTitle());
                        break;
                    }
                }
            }
        }
        return restaurantList;
    }

    /**
     * This method is use to format the Arraylist to a string, that way user will see the list of
     * restaurant in a more formatted way
     *
     * @param restaurantList the restaurant in list
     * @return string of list of restaurant with newline
     */
    @Nonnull
    public String formatRestaurantList(ArrayList<String> restaurantList) {
        StringBuilder sb = new StringBuilder();
        for (String restaurant : restaurantList) {
            sb.append(restaurant + "\n");
        }
        return sb.toString();
    }
}
