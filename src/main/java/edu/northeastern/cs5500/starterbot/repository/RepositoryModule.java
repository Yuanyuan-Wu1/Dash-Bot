package edu.northeastern.cs5500.starterbot.repository;

import dagger.Module;
import dagger.Provides;
import edu.northeastern.cs5500.starterbot.model.Cart;
import edu.northeastern.cs5500.starterbot.model.History;
import edu.northeastern.cs5500.starterbot.model.Restaurants;
import edu.northeastern.cs5500.starterbot.model.UserPreference;

@Module
public class RepositoryModule {
    // NOTE: You can use the following lines if you'd like to store objects in memory.
    // NOTE: The presence of commented-out code in your project *will* result in a lowered grade.

    /**
     * Extract the UserPreference data from InMemory to GenericRepository.
     *
     * @param repository - The userPreference data from InMemory
     * @return - UserPreference's repository
     */
    @Provides
    public GenericRepository<UserPreference> provideUserPreferencesRepository(
            MongoDBRepository<UserPreference> repository) {
        return repository;
    }

    @Provides
    public Class<UserPreference> provideUserPreference() {
        return UserPreference.class;
    }

    /**
     * Extract the Restaurants data from MongoDB to GenericRepository.
     *
     * @param repository - The restaurants data from MongoDB
     * @return - Restaurants's repository
     */
    @Provides
    public GenericRepository<Restaurants> provideRestaurantsRepository(
            MongoDBRepository<Restaurants> repository) {
        return repository;
    }

    @Provides
    public Class<Restaurants> provideRestaurants() {
        return Restaurants.class;
    }

    /**
     * Extract the Cart data from MongoDB to GenericRepository.
     *
     * @param repository - The cart data from MongoDB
     * @return - Cart's repository
     */
    @Provides
    public GenericRepository<Cart> provideShoppingCartRepository(
            MongoDBRepository<Cart> repository) {
        return repository;
    }

    @Provides
    public Class<Cart> provideShoppingCart() {
        return Cart.class;
    }

    /**
     * Extract the History data from MongoDB to GenericRepository.
     *
     * @param repository - The history data from MongoDB
     * @return - History's repository
     */
    @Provides
    public GenericRepository<History> provideHistoryRepository(
            MongoDBRepository<History> repository) {
        return repository;
    }

    @Provides
    public Class<History> provideHistory() {
        return History.class;
    }
}
