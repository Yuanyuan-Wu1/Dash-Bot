package edu.northeastern.cs5500.starterbot.controller;

import com.mongodb.lang.Nullable;
import edu.northeastern.cs5500.starterbot.model.Address;
import edu.northeastern.cs5500.starterbot.model.Payment;
import edu.northeastern.cs5500.starterbot.model.UserPreference;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import java.util.Collection;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserPreferenceController {

    GenericRepository<UserPreference> userPreferenceRepository;

    @Inject
    public UserPreferenceController(GenericRepository<UserPreference> userPreferenceRepository) {
        this.userPreferenceRepository = userPreferenceRepository;

        if (userPreferenceRepository.count() == 0) {
            UserPreference userPreference = new UserPreference();
            userPreference.setDiscordUserId("1234");
            userPreference.setDiscordUserName("Alex");
            userPreference.setPostalCode("98004");

            Address newAddressModel = new Address();

            newAddressModel.setAddress("2464  Main Street");
            newAddressModel.setCity("Bothell");
            newAddressModel.setState("WA");
            newAddressModel.setMessage("Leave at door.");

            Payment payment = new Payment();

            payment.setCardNumber("4808185823472290");
            payment.setExpireDate("02/2026");
            payment.setSecurityCode("915");

            userPreferenceRepository.add(userPreference);
        }
    }

    /**
     * Set the userNmae.
     *
     * @param discordMemberId - String, representing the discordMemberId
     * @param postalCode - String, updating postalcode that user type in.
     * @return - the postalcode info.
     */
    public void setDiscordUserName(String discordMemberId, String discordUserName) {
        UserPreference userPreference = getUserPreferenceForMemberId(discordMemberId);

        userPreference.setDiscordUserName(discordUserName);
        userPreferenceRepository.update(userPreference);
    }

    /**
     * Get the discordUserName.
     *
     * @param discordMemberId - String, representing the discordMemberId
     * @return - the user's discordUserName.
     */
    @Nullable
    public String getDiscordUserName(String discordMemberId) {
        return getUserPreferenceForMemberId(discordMemberId).getDiscordUserName();
    }

    /**
     * Set the postalcode.
     *
     * @param discordMemberId - String
     * @param postalCode - String, updating postalcode that user type in.
     * @return - the postalcode info.
     */
    public void setPostalCodeForUser(String discordMemberId, String postalCode) {
        UserPreference userPreference = getUserPreferenceForMemberId(discordMemberId);

        userPreference.setPostalCode(postalCode);
        userPreferenceRepository.update(userPreference);
    }

    /**
     * Get the postalcode info based on the users' choice.
     *
     * @param discordMemberId - String, representing postalcode that user type in.
     * @return - the postalcode info.
     */
    @Nullable
    public String getPostalCodeForUser(String discordMemberId) {
        return getUserPreferenceForMemberId(discordMemberId).getPostalCode();
    }

    /**
     * Set the address.
     *
     * @param discordMemberId - String
     * @param address - String, updating address that user type in.
     * @return - the address string.
     */
    public void setAddressForUser(String discordMemberId, Address address) {
        UserPreference userPreference = getUserPreferenceForMemberId(discordMemberId);
        userPreference.setAddress(address);
        userPreferenceRepository.update(userPreference);
    }

    /**
     * Get the address info based on the users' choice.
     *
     * @param discordMemberId - String, representing address that user type in.
     * @return - the address info.
     */
    @Nullable
    public Address getAddressForUser(String discordMemberId) {
        return getUserPreferenceForMemberId(discordMemberId).getAddress();
    }

    /**
     * Get the address info based on the users' choice.
     *
     * @param discordMemberId - String, representing address that user type in.
     * @return - the address info in string, format like: Address: 123 st City: Bellvue State: wa
     *     PostalCode: 98033
     */
    @Nullable
    public String getAddressStringForUser(String discordMemberId) {
        UserPreference up = getUserPreferenceForMemberId(discordMemberId);
        Address address = up.getAddress();
        return address == null
                ? null
                : String.format(
                        "\nAddress: %s\nCity: %s\nState: %s\nPostalCode: %s",
                        address.getAddress(),
                        address.getCity(),
                        address.getState(),
                        up.getPostalCode());
    }

    /**
     * Get the address info based on the users' choice.
     *
     * @param discordMemberId - String, representing address that user type in.
     * @return - the address info, format like: Address: 123 st City: Bellvue State: wa Message:
     *     blabla PostalCode: 98033
     */
    @Nullable
    public String getAddressStringWithMessageForUser(String discordMemberId) {
        UserPreference up = getUserPreferenceForMemberId(discordMemberId);
        Address address = up.getAddress();
        return address == null
                ? null
                : String.format("%s\nPostalCode: %s", address.toString(), up.getPostalCode());
    }

    /**
     * Set the payment.
     *
     * @param discordMemberId - String
     * @param address - String, updating payment that user type in.
     * @return - the payment string.
     */
    public void setPaymentForUser(String discordMemberId, Payment payment) {
        UserPreference userPreference = getUserPreferenceForMemberId(discordMemberId);
        userPreference.setPayment(payment);
        userPreferenceRepository.update(userPreference);
    }

    /**
     * Get the address info based on the users' choice.
     *
     * @param discordMemberId - String, representing address that user type in.
     * @return - the address info.
     */
    @Nullable
    public Payment getPaymentForUser(String discordMemberId) {
        return getUserPreferenceForMemberId(discordMemberId).getPayment();
    }

    @Nonnull
    public UserPreference getUserPreferenceForMemberId(String discordMemberId) {
        Collection<UserPreference> userPreferences = userPreferenceRepository.getAll();
        for (UserPreference currentUserPreference : userPreferences) {
            if (currentUserPreference.getDiscordUserId().equals(discordMemberId)) {
                return currentUserPreference;
            }
        }

        UserPreference userPreference = new UserPreference();
        userPreference.setDiscordUserId(discordMemberId);
        userPreferenceRepository.add(userPreference);
        return userPreference;
    }
}
