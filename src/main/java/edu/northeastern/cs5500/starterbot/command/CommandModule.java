package edu.northeastern.cs5500.starterbot.command;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;

@Module
@ExcludeFromJacocoGeneratedReport
public class CommandModule {

    @Provides
    @IntoSet
    public SlashCommandHandler provideStartCommand(StartCommand startCommand) {
        return startCommand;
    }

    @Provides
    @IntoSet
    public SlashCommandHandler provideSetPostCodeCommand(
            SetPostalCodeCommand setPostalCodeCommand) {
        return setPostalCodeCommand;
    }

    @Provides
    @IntoSet
    public SlashCommandHandler provideRecommendationsCommand(
            RecommendationsCommand RecommendationsCommand) {
        return RecommendationsCommand;
    }

    @Provides
    @IntoSet
    public SlashCommandHandler provideCategoryCommand(CategoryCommand categoryCommand) {
        return categoryCommand;
    }

    @Provides
    @IntoSet
    public SlashCommandHandler provideRestaurantCommand(RestaurantCommand restaurantCommand) {
        return restaurantCommand;
    }

    @Provides
    @IntoSet
    public SlashCommandHandler provideAddressCommand(AddressCommand addressCommand) {
        return addressCommand;
    }

    @Provides
    @IntoSet
    public SlashCommandHandler providePaymentCommand(PaymentCommand paymentCommand) {
        return paymentCommand;
    }

    @Provides
    @IntoSet
    public SlashCommandHandler provideOrderCommand(OrderCommand orderCommand) {
        return orderCommand;
    }

    @Provides
    @IntoSet
    public SlashCommandHandler provideCartCommand(CartCommand cartCommand) {
        return cartCommand;
    }

    @Provides
    @IntoSet
    public SlashCommandHandler provideButtonCommand(SubmitCommand submitCommand) {
        return submitCommand;
    }

    @Provides
    @IntoSet
    public ButtonClickHandler provideButtonCommandClickHandler(SubmitCommand submitCommand) {
        return submitCommand;
    }

    @Provides
    @IntoSet
    public SlashCommandHandler provideHistoryCommand(HistoryCommand historyCommand) {
        return historyCommand;
    }
}
