package edu.northeastern.cs5500.starterbot.controller;

import static com.google.common.truth.Truth.assertThat;

import edu.northeastern.cs5500.starterbot.model.Address;
import edu.northeastern.cs5500.starterbot.model.Payment;
import edu.northeastern.cs5500.starterbot.model.UserPreference;
import edu.northeastern.cs5500.starterbot.repository.InMemoryRepository;
import org.junit.Before;
import org.junit.jupiter.api.Test;

class UserPreferenceControllerTest {
    static final String USER_ID_1 = "23h5ikoqaehokljhaoe";
    static final String USER_ID_2 = "2kjfksdjdkhokljhaoe";
    static final String USER_NAME_1 = "Joe";
    static final String USER_NAME_2 = "Penny";
    static final String POST_CODE_1 = "98012";
    static final String POST_CODE_2 = "98033";
    Address address_1 = new Address();
    Address address_2 = new Address();

    static final String TEST_ADDRESS_1 = "2464  Main Street";
    static final String TEST_ADDRESS_2 = "9876 54ST PL SE";
    static final String CITY_1 = "kirkland";
    static final String CITY_2 = "bother";
    static final String STATE_1 = "WA";
    static final String STATE_2 = "CA";
    static final String INSTRUCTIONS_1 = "meet outside";
    static final String INSTRUCTIONS_2 = "leave at door";
    Payment payment_1 = new Payment();
    Payment payment_2 = new Payment();

    static final String TEST_PAYMENT_1 = "4023672804438886";
    static final String TEST_PAYMENT_2 = "4539356403726910";
    static final String EXPIREDATE_1 = "06/2023";
    static final String EXPIREDATE_2 = "03/2026";
    static final String SECURITYCODE_1 = "315";
    static final String SECURITYCODE_2 = "226";

    private UserPreferenceController getUserPreferenceController() {
        UserPreferenceController userPreferenceController =
                new UserPreferenceController(new InMemoryRepository<>());
        return userPreferenceController;
    }

    @Before
    void setTestData() {

        address_1.setAddress(TEST_ADDRESS_1);
        address_1.setCity(CITY_1);
        address_1.setState(STATE_1);
        address_1.setMessage(INSTRUCTIONS_1);

        address_2.setAddress(TEST_ADDRESS_2);
        address_2.setCity(CITY_2);
        address_2.setState(STATE_2);
        address_2.setMessage(INSTRUCTIONS_2);

        payment_1.setCardNumber(TEST_PAYMENT_1);
        payment_1.setExpireDate(EXPIREDATE_1);
        payment_1.setSecurityCode(SECURITYCODE_1);

        payment_2.setCardNumber(TEST_PAYMENT_2);
        payment_2.setExpireDate(EXPIREDATE_2);
        payment_2.setSecurityCode(SECURITYCODE_2);
    }

    @Test
    void testSetDiscordUserName() {
        // setup
        UserPreferenceController userPreferenceController = getUserPreferenceController();

        // precondition
        assertThat(userPreferenceController.getDiscordUserName(USER_ID_1))
                .isNotEqualTo(USER_NAME_1);

        // mutation
        userPreferenceController.setDiscordUserName(USER_ID_1, USER_NAME_1);

        // postcondition
        assertThat(userPreferenceController.getDiscordUserName(USER_ID_1)).isEqualTo(USER_NAME_1);
    }

    @Test
    void testOnlyAddsDefaultObjectsIfRepoIsEmpty() {
        InMemoryRepository<UserPreference> userPreferenceRepository = new InMemoryRepository<>();

        userPreferenceRepository.add(new UserPreference());
        userPreferenceRepository.add(new UserPreference());
        userPreferenceRepository.add(new UserPreference());

        UserPreferenceController userPreferenceController =
                new UserPreferenceController(userPreferenceRepository);
        assertThat(userPreferenceRepository.count()).isEqualTo(3);
        assertThat(userPreferenceController).isNotNull();
    }

    @Test
    void testSetPostCodeActuallySetsPostalCode() {
        UserPreferenceController userPreferenceController = getUserPreferenceController();
        assertThat(userPreferenceController.getPostalCodeForUser(USER_ID_1))
                .isNotEqualTo(POST_CODE_1);

        userPreferenceController.setPostalCodeForUser(USER_ID_1, POST_CODE_1);
        assertThat(userPreferenceController.getPostalCodeForUser(USER_ID_1)).isEqualTo(POST_CODE_1);
    }

    @Test
    void testSetPostCodeOverwritesOldPostCode() {
        UserPreferenceController userPreferenceController = getUserPreferenceController();
        userPreferenceController.setPostalCodeForUser(USER_ID_1, POST_CODE_1);
        assertThat(userPreferenceController.getPostalCodeForUser(USER_ID_1)).isEqualTo(POST_CODE_1);

        userPreferenceController.setPostalCodeForUser(USER_ID_1, POST_CODE_2);
        assertThat(userPreferenceController.getPostalCodeForUser(USER_ID_1)).isEqualTo(POST_CODE_2);
    }

    @Test
    void testSetPostCodeOnlyOverwritesTargetPostCode() {
        UserPreferenceController userPreferenceController = getUserPreferenceController();

        userPreferenceController.setPostalCodeForUser(USER_ID_1, POST_CODE_1);
        userPreferenceController.setPostalCodeForUser(USER_ID_2, POST_CODE_2);

        assertThat(userPreferenceController.getPostalCodeForUser(USER_ID_1)).isEqualTo(POST_CODE_1);
        assertThat(userPreferenceController.getPostalCodeForUser(USER_ID_2)).isEqualTo(POST_CODE_2);
    }

    @Test
    void testSetAddressActuallySetsAddress() {

        UserPreferenceController userPreferenceController = getUserPreferenceController();
        assertThat(userPreferenceController.getAddressForUser(USER_ID_1)).isNotEqualTo(address_1);

        userPreferenceController.setAddressForUser(USER_ID_1, address_1);
        assertThat(userPreferenceController.getAddressForUser(USER_ID_1)).isEqualTo(address_1);
    }

    @Test
    void testSetAddressOverwritesOldAddress() {
        UserPreferenceController userPreferenceController = getUserPreferenceController();
        userPreferenceController.setAddressForUser(USER_ID_1, address_1);
        assertThat(userPreferenceController.getAddressForUser(USER_ID_1)).isEqualTo(address_1);

        userPreferenceController.setAddressForUser(USER_ID_1, address_2);
        assertThat(userPreferenceController.getAddressForUser(USER_ID_1)).isEqualTo(address_2);
    }

    @Test
    void testSetAddressOnlyOverwritesTargetAddress() {
        UserPreferenceController userPreferenceController = getUserPreferenceController();

        userPreferenceController.setAddressForUser(USER_ID_1, address_1);
        userPreferenceController.setAddressForUser(USER_ID_2, address_2);

        assertThat(userPreferenceController.getAddressForUser(USER_ID_1)).isEqualTo(address_1);
        assertThat(userPreferenceController.getAddressForUser(USER_ID_2)).isEqualTo(address_2);
    }

    @Test
    void testGetAddressStringForUser() {
        UserPreferenceController userPreferenceController = getUserPreferenceController();
        userPreferenceController.setPostalCodeForUser(USER_ID_1, POST_CODE_1);
        userPreferenceController.setAddressForUser(USER_ID_1, address_1);
        userPreferenceController.getAddressStringForUser(USER_ID_1);
        assertThat(userPreferenceController.getAddressStringForUser(USER_ID_1))
                .isEqualTo(
                        String.format(
                                "\nAddress: %s\nCity: %s\nState: %s\nPostalCode: %s",
                                address_1.getAddress(),
                                address_1.getCity(),
                                address_1.getState(),
                                POST_CODE_1));
    }

    @Test
    void testSetPaymentActuallySetsPayment() {

        UserPreferenceController userPreferenceController = getUserPreferenceController();
        assertThat(userPreferenceController.getPaymentForUser(USER_ID_1)).isNotEqualTo(payment_1);

        userPreferenceController.setPaymentForUser(USER_ID_1, payment_1);
        assertThat(userPreferenceController.getPaymentForUser(USER_ID_1)).isEqualTo(payment_1);
    }

    @Test
    void testSetPaymentOverwritesOldPayment() {
        UserPreferenceController userPreferenceController = getUserPreferenceController();
        userPreferenceController.setPaymentForUser(USER_ID_1, payment_1);
        assertThat(userPreferenceController.getPaymentForUser(USER_ID_1)).isEqualTo(payment_1);

        userPreferenceController.setPaymentForUser(USER_ID_1, payment_2);
        assertThat(userPreferenceController.getPaymentForUser(USER_ID_1)).isEqualTo(payment_2);
    }

    @Test
    void testSetPaymentOnlyOverwritesTargetPayment() {
        UserPreferenceController userPreferenceController = getUserPreferenceController();

        userPreferenceController.setPaymentForUser(USER_ID_1, payment_1);
        userPreferenceController.setPaymentForUser(USER_ID_2, payment_2);

        assertThat(userPreferenceController.getPaymentForUser(USER_ID_1)).isEqualTo(payment_1);
        assertThat(userPreferenceController.getPaymentForUser(USER_ID_2)).isEqualTo(payment_2);
    }
}
