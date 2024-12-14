package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.Restaurants;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import edu.northeastern.cs5500.starterbot.service.PostalCodeService;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RecommendationsController {

    GenericRepository<Restaurants> restaurantsRepository;

    /**
     * Inject the restaurants from the MongoDB repository
     *
     * @param restaurantRepository - representing the restaurant's repository in Generic Repository.
     */
    @Inject
    RecommendationsController(GenericRepository<Restaurants> postalCodeRepository) {
        this.restaurantsRepository = postalCodeRepository;
    }

    /**
     * Expand the scope of postalcode. Get the restaurant info based on the postalcode' choice.
     *
     * @param postalCode - String, representing restaurant postalcode that user type in, could be
     *     show restaurant information can be searched within the scope.
     * @return - the restaurant info.
     */
    public Collection<String> getRestaurantsInPostalCode(String postalCode) {
        Collection<String> res =
                getRestaurantsForPostalCode(postalCode).stream()
                        .map(
                                restaurant -> {
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("\n");
                                    sb.append(restaurant.getTitle());
                                    sb.append(" ");
                                    sb.append(restaurant.getCategories());
                                    return sb.toString();
                                })
                        .collect(Collectors.toList());

        return res;
    }

    /**
     * Expand the scope of postalcode. Get the restaurant info based on the postalcode' choice.
     *
     * @param postalCode - String, representing restaurant postalcode that user type in, could be
     *     show restaurant information can be searched within the scope.
     * @return - the restaurant List.
     */
    public Collection<Restaurants> getRestaurantsForPostalCode(String postalCode) {

        PostalCodeService postalCodeService = new PostalCodeService();

        List<String> postalCodeSet = postalCodeService.getNearbyPostalCodes(postalCode, 10);

        Collection<Restaurants> res =
                restaurantsRepository.getAll().stream()
                        .filter(restaurant -> postalCodeSet.contains(restaurant.getPostalCode()))
                        .collect(Collectors.toList());

        return res;
    }
}
