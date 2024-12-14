package edu.northeastern.cs5500.starterbot.controller;

import static com.google.common.truth.Truth.assertThat;

import edu.northeastern.cs5500.starterbot.model.Restaurants;
import edu.northeastern.cs5500.starterbot.repository.InMemoryRepository;
import java.util.Collection;
import org.junit.jupiter.api.Test;

public class RecommendationsControllerTest {
    Restaurants restaurant_98012 = new Restaurants();
    Restaurants restaurant_98021 = new Restaurants();
    String POSTAL_CODE_1 = "98012";
    String POSTAL_CODE_2 = "98021";
    String TITLE_1 = "title_98012";
    String TITLE_2 = "title_98021";

    private RecommendationsController getRecommendationsController() {
        InMemoryRepository<Restaurants> restaurantRepository = new InMemoryRepository<>();

        restaurant_98012.setTitle(TITLE_1);
        restaurant_98012.setPostalCode(POSTAL_CODE_1);

        restaurant_98021.setTitle(TITLE_2);
        restaurant_98021.setPostalCode(POSTAL_CODE_2);

        restaurantRepository.add(restaurant_98012);
        restaurantRepository.add(restaurant_98021);

        RecommendationsController recommendationsController =
                new RecommendationsController(restaurantRepository);

        return recommendationsController;
    }

    @Test
    void testgetRestaurantsInPostalCode() {
        RecommendationsController recommendationsController = getRecommendationsController();

        Collection<String> restaurants =
                recommendationsController.getRestaurantsInPostalCode(POSTAL_CODE_1);

        assertThat(restaurants.size()).isEqualTo(2);

        assertThat(restaurants.toString()).contains(TITLE_2);
        assertThat(restaurants.toString()).contains(TITLE_1);
    }

    @Test
    void testgetRestaurantsInPostalCodeWithNoRestaurantInPostalcode() {
        RecommendationsController recommendationsController = getRecommendationsController();

        Collection<String> restaurants =
                recommendationsController.getRestaurantsInPostalCode("98079");

        assertThat(restaurants.size()).isEqualTo(0);
    }

    @Test
    void testgetRestaurantsForPostalCode() {
        RecommendationsController recommendationsController = getRecommendationsController();

        Collection<String> restaurants =
                recommendationsController.getRestaurantsInPostalCode(POSTAL_CODE_1);

        assertThat(restaurants.size()).isEqualTo(2);

        assertThat(restaurants.toString()).contains(TITLE_2);
        assertThat(restaurants.toString()).contains(TITLE_1);
    }

    @Test
    void testgetRestaurantsForPostalCodeWithNoRestaurantInPostalcode() {
        RecommendationsController recommendationsController = getRecommendationsController();

        Collection<String> restaurants =
                recommendationsController.getRestaurantsInPostalCode("98079");

        assertThat(restaurants.size()).isEqualTo(0);
    }
}
