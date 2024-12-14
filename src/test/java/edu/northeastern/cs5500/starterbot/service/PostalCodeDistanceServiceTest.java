package edu.northeastern.cs5500.starterbot.service;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.Test;

class PostalCodeDistanceServiceTest {

    @Test
    void testgetPostalCodesDistance() {
        PostalCodeDistanceService postalCodeDistanceService = new PostalCodeDistanceService();
        Double DISTANCE_1 = 5.015230704908135;
        Double DISTANCE_2 = 0.00;

        assertThat(
                postalCodeDistanceService
                        .getPostalCodesDistance("98052", "98033")
                        .equals(DISTANCE_1));
        assertThat(
                !postalCodeDistanceService
                        .getPostalCodesDistance("98052", "98033")
                        .equals(DISTANCE_2));
    }
}
