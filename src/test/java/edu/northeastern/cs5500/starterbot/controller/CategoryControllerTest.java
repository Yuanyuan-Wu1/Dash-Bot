package edu.northeastern.cs5500.starterbot.controller;

import static com.google.common.truth.Truth.assertThat;

import edu.northeastern.cs5500.starterbot.model.Dish;
import edu.northeastern.cs5500.starterbot.model.Restaurants;
import edu.northeastern.cs5500.starterbot.repository.InMemoryRepository;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

class CategoryControllerTest {
    static final String DISCORD_ID_1 = "935049484717678613";
    static final String DISCORD_ID_2 = "900203549529628722";
    static final String ZIPCODE_1 = "98105";
    static final String ZIPCODE_2 = "98106";
    static final String CATEGORY1 = "American";
    static final String CATEGORY2 = "AMERICAN";
    static final String CATEGORY3 = "mexican";
    static final String CATEGORY4 = "mexi";

    static final String RESTAURANT_1 = "Toulouse Petit";
    static final String RESTAURANT_2 = "toul";
    static final String RESTAURANT_ID_1 = "63b4ef3b-a2fb-47c3-9e19-6cb6d3e082eb";
    static final String RESTAURANT_ADDRESS_1 = "601 Queen Anne Ave N";
    static final String RESTAURANT_CITY_1 = "Seattle";
    static final String RESTAURANT_ZIPCODE_1 = "98105";
    static final String RESTAURANT_REGION = "WA";
    static final ArrayList<String> category_1 = new ArrayList<>();
    static final ArrayList<Dish> menu_1 = new ArrayList<>();
    static final String DISH_ID_1 = "d185950f-b7ff-46f8-95d9-fc0d4df88685";
    static final String DISH_NAME_1 = "Small Beignets (5 pieces)";
    static final Integer DISH_PRICE_1 = 1475;
    static final String DISH_TEXT_1 = "$14.75";

    static final String RESTAURANT_3 = "bread";
    static final String RESTAURANT_4 = "Panera Bread";
    static final String RESTAURANT_ID_2 = "6699a6cf-1996-43c6-82a9-76c4d8f37b04";
    static final String RESTAURANT_ADDRESS_2 = "21221 Bothell Everett Hwy";
    static final String RESTAURANT_CITY_2 = "Bothell";
    static final String RESTAURANT_ZIPCODE_2 = "98109";
    static final ArrayList<String> category_2 = new ArrayList<>();
    static final ArrayList<Dish> menu_2 = new ArrayList<>();
    static final String DISH_ID_2 = "ac19e6cc-988e-56d5-9ee2-4f180bd09ca4";
    static final String DISH_NAME_2 = "2 Flatbread Pizza Family Feast";
    static final Integer DISH_PRICE_2 = 3009;
    static final String DISH_TEXT_2 = "$30.09";

    static final String RESTAURANT_5 = "aaa";

    private CategoryController getCategoryController() {

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

        CategoryController categoryController = new CategoryController(restaurantRepository);
        ;

        return categoryController;
    }

    @Test
    void testGetRestaurantNameByCategory() {
        CategoryController categoryController = getCategoryController();
        ArrayList<String> list = new ArrayList<>();
        list.add(RESTAURANT_1);
        assertThat(
                categoryController
                        .getRestaurantNameByCategory(CATEGORY1, ZIPCODE_1)
                        .contains(RESTAURANT_1));
        assertThat(
                categoryController.getRestaurantNameByCategory(CATEGORY1, ZIPCODE_1).equals(list));
        assertThat(
                categoryController
                        .getRestaurantNameByCategory(CATEGORY2, ZIPCODE_1)
                        .contains(RESTAURANT_1));
        assertThat(
                categoryController
                        .getRestaurantNameByCategory(CATEGORY3, ZIPCODE_1)
                        .contains(RESTAURANT_1));
        assertThat(
                categoryController
                        .getRestaurantNameByCategory(CATEGORY4, ZIPCODE_1)
                        .contains(RESTAURANT_1));
        assertThat(
                !categoryController
                        .getRestaurantNameByCategory(CATEGORY1, ZIPCODE_2)
                        .contains(RESTAURANT_1));
        assertThat(!categoryController.getRestaurantNameByCategory(CATEGORY2, ZIPCODE_1).isEmpty());
        assertThat(
                !categoryController.getRestaurantNameByCategory(CATEGORY2, ZIPCODE_2).equals(null));
    }

    @Test
    void testformatRestaurantList() {
        CategoryController categoryController = getCategoryController();
        ArrayList<String> arr1 =
                categoryController.getRestaurantNameByCategory(CATEGORY1, DISCORD_ID_1);
        String result = categoryController.formatRestaurantList(arr1);
        String expected = "Toulouse Petit";
        String not_expected = "123";
        assertThat(result.equals(expected));
        assertThat(!result.equals(null));
        assertThat(!result.equals(not_expected));
    }
}
