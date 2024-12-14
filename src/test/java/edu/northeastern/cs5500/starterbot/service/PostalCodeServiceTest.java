package edu.northeastern.cs5500.starterbot.service;

import static com.google.common.truth.Truth.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class PostalCodeServiceTest {
    final String REDMOND_POSTAL_CODE = "98052";
    final String KIRKLAND_POSTAL_CODE = "98033";

    @Test
    void testRedmondIsNearKirkland() {
        PostalCodeService postalCodeService = new PostalCodeService();
        List<String> nearbyCodes = postalCodeService.getNearbyPostalCodes(KIRKLAND_POSTAL_CODE);
        assertThat(nearbyCodes).contains(REDMOND_POSTAL_CODE);
    }

    @Test
    void testKirklandIsNearRedmond() {
        PostalCodeService postalCodeService = new PostalCodeService();
        List<String> nearbyCodes = postalCodeService.getNearbyPostalCodes(REDMOND_POSTAL_CODE);
        assertThat(nearbyCodes).contains(KIRKLAND_POSTAL_CODE);
    }

    @Test
    void testRedmondIsNearRedmond() {
        PostalCodeService postalCodeService = new PostalCodeService();
        List<String> nearbyCodes = postalCodeService.getNearbyPostalCodes(REDMOND_POSTAL_CODE);
        assertThat(nearbyCodes).contains(REDMOND_POSTAL_CODE);
    }
}
