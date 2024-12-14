package edu.northeastern.cs5500.starterbot.controller;

import static com.google.common.truth.Truth.assertThat;

import edu.northeastern.cs5500.starterbot.model.Dish;
import edu.northeastern.cs5500.starterbot.model.Restaurants;
import edu.northeastern.cs5500.starterbot.repository.InMemoryRepository;
import java.util.ArrayList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RestaurantControllerTest {
    static final String USER_ZIPCODE_1 = "98119";
    static final String RESTAURANT_1 = "Toulouse Petit";
    static final String RESTAURANT_2 = "toul";
    static final String RESTAURANT_ID_1 = "63b4ef3b-a2fb-47c3-9e19-6cb6d3e082eb";
    static final String RESTAURANT_ADDRESS_1 = "601 Queen Anne Ave N";
    static final String RESTAURANT_CITY_1 = "Seattle";
    static final String RESTAURANT_ZIPCODE_1 = "98119";
    static final String RESTAURANT_REGION = "WA";
    static final ArrayList<String> category_1 = new ArrayList<>();
    static final ArrayList<Dish> menu_1 = new ArrayList<>();
    static final String DISH_ID_1 = "d185950f-b7ff-46f8-95d9-fc0d4df88685";
    static final String DISH_NAME_1 = "Small Beignets (5 pieces)";
    static final Integer DISH_PRICE_1 = 1475;
    static final String DISH_TEXT_1 = "$14.75";

    static final String USER_ZIPCODE_2 = "98021";
    static final String RESTAURANT_3 = "bread";
    static final String RESTAURANT_4 = "Panera Bread";
    static final String RESTAURANT_ID_2 = "6699a6cf-1996-43c6-82a9-76c4d8f37b04";
    static final String RESTAURANT_ADDRESS_2 = "21221 Bothell Everett Hwy";
    static final String RESTAURANT_CITY_2 = "Bothell";
    static final String RESTAURANT_ZIPCODE_2 = "98021";
    static final ArrayList<String> category_2 = new ArrayList<>();
    static final ArrayList<Dish> menu_2 = new ArrayList<>();
    static final String DISH_ID_2 = "ac19e6cc-988e-56d5-9ee2-4f180bd09ca4";
    static final String DISH_NAME_2 = "2 Flatbread Pizza Family Feast";
    static final Integer DISH_PRICE_2 = 3009;
    static final String DISH_TEXT_2 = "$30.09";

    static final String RESTAURANT_5 = "aaa";

    private RestaurantController getRestaurantController() {
        InMemoryRepository<Restaurants> restaurantRepository = new InMemoryRepository<>();
        category_1.add("Seafood");
        category_1.add("American");
        category_2.add("Salad");
        category_2.add("Sandwish");

        Dish dish1 = new Dish();
        dish1.setUuid(DISH_ID_1);
        dish1.setTitle(DISH_NAME_1);
        dish1.setText(DISH_TEXT_1);
        dish1.setPrice(DISH_PRICE_1);
        menu_1.add(dish1);

        Dish dish2 = new Dish();
        dish2.setUuid(DISH_ID_2);
        dish2.setTitle(DISH_NAME_2);
        dish2.setText(DISH_TEXT_2);
        dish2.setPrice(DISH_PRICE_2);
        menu_2.add(dish2);

        Restaurants restaurant1 = new Restaurants();
        restaurant1.setTitle(RESTAURANT_1);
        restaurant1.setUuid(RESTAURANT_ID_1);
        restaurant1.setStreetAddress(RESTAURANT_ADDRESS_1);
        restaurant1.setCity(RESTAURANT_CITY_1);
        restaurant1.setPostalCode(RESTAURANT_ZIPCODE_1);
        restaurant1.setRegion(RESTAURANT_REGION);
        restaurant1.setCategories(category_1);
        restaurant1.setMenu(menu_1);

        Restaurants restaurant2 = new Restaurants();
        restaurant2.setTitle(RESTAURANT_4);
        restaurant2.setUuid(RESTAURANT_ID_2);
        restaurant2.setStreetAddress(RESTAURANT_ADDRESS_2);
        restaurant2.setCity(RESTAURANT_CITY_2);
        restaurant2.setPostalCode(RESTAURANT_ZIPCODE_2);
        restaurant2.setRegion(RESTAURANT_REGION);
        restaurant2.setCategories(category_2);
        restaurant2.setMenu(menu_2);

        restaurantRepository.add(restaurant1);
        restaurantRepository.add(restaurant2);

        RestaurantController restaurantController = new RestaurantController(restaurantRepository);

        return restaurantController;
    }

    @Test
    void testGetRestaurantForUserByFullName() {
        RestaurantController restaurantController = getRestaurantController();
        System.out.println(
                restaurantController.getRestaurantForUser(RESTAURANT_1, USER_ZIPCODE_1).getTitle());

        assertThat(
                        restaurantController
                                .getRestaurantForUser(RESTAURANT_1, USER_ZIPCODE_1)
                                .getTitle())
                .isEqualTo(RESTAURANT_1);
    }

    @Test
    void testGetRestaurantForUserByPartName1() {
        RestaurantController restaurantController = getRestaurantController();

        assertThat(
                        restaurantController
                                .getRestaurantForUser(RESTAURANT_2, USER_ZIPCODE_1)
                                .getTitle())
                .isEqualTo(RESTAURANT_1);
    }

    @Test
    void testGetRestaurantForUserByPartName2() {
        RestaurantController restaurantController = getRestaurantController();

        assertThat(
                        restaurantController
                                .getRestaurantForUser(RESTAURANT_3, USER_ZIPCODE_2)
                                .getTitle())
                .isEqualTo(RESTAURANT_4);
    }

    @Test
    void testGetRestaurantForUserByNotExistName() {
        RestaurantController restaurantController = getRestaurantController();
        Assertions.assertNull(
                restaurantController.getRestaurantForUser(RESTAURANT_5, USER_ZIPCODE_2));
    }

    @Test
    void testGetMenuForUser1() {
        RestaurantController restaurantController = getRestaurantController();
        String menu_1 = restaurantController.getMenuForUser(RESTAURANT_1, USER_ZIPCODE_1);
        String menu_2 = restaurantController.getMenuForUser(RESTAURANT_2, USER_ZIPCODE_1);
        assertThat(menu_1).isEqualTo(menu_2);
    }

    @Test
    void testGetMenuForUser2() {
        RestaurantController restaurantController = getRestaurantController();
        String menu_3 = restaurantController.getMenuForUser(RESTAURANT_3, USER_ZIPCODE_2);
        String menu_4 = restaurantController.getMenuForUser(RESTAURANT_4, USER_ZIPCODE_2);
        assertThat(menu_3).isEqualTo(menu_4);
    }

    @Test
    void testGetMenuForUser3() {
        RestaurantController restaurantController = getRestaurantController();
        assertThat(restaurantController.getMenuForUser(RESTAURANT_5, USER_ZIPCODE_2))
                .isEqualTo("This restaurant cannot be delivered.");
    }

    @Test
    void testGetMenuToOrder1() {
        RestaurantController restaurantController = getRestaurantController();
        assertThat(restaurantController.getMenuToOrder(RESTAURANT_2, USER_ZIPCODE_1))
                .isEqualTo(restaurantController.getMenuToOrder(RESTAURANT_1, USER_ZIPCODE_1));
    }

    @Test
    void testGetMenuToOrder2() {
        RestaurantController restaurantController = getRestaurantController();
        assertThat(restaurantController.getMenuToOrder(RESTAURANT_3, USER_ZIPCODE_2))
                .isNotEqualTo(restaurantController.getMenuToOrder(RESTAURANT_2, USER_ZIPCODE_1));
        assertThat(restaurantController.getMenuToOrder(RESTAURANT_3, USER_ZIPCODE_2))
                .isEqualTo(restaurantController.getMenuToOrder(RESTAURANT_4, USER_ZIPCODE_2));
    }

    @Test
    void testGetRestaurantZipCode() {
        RestaurantController restaurantController = getRestaurantController();
        assertThat(
                restaurantController
                        .getRestaurantZipCode(RESTAURANT_1)
                        .equals(RESTAURANT_ZIPCODE_1));
        assertThat(
                !restaurantController
                        .getRestaurantZipCode(RESTAURANT_1)
                        .equals(RESTAURANT_ZIPCODE_2));
        assertThat(!restaurantController.getRestaurantZipCode(RESTAURANT_1).equals(null));
    }
}
