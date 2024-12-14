package edu.northeastern.cs5500.starterbot.service;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
class NearbyZipcode {
    protected Integer zipcode;
    protected Double distance;
}
