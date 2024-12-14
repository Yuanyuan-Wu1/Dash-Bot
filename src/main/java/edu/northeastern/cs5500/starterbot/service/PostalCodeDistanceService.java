package edu.northeastern.cs5500.starterbot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

@Singleton
@Slf4j
public class PostalCodeDistanceService implements Service {
    public static final String DISTANCE_API = "https://zipcode-radius.glitch.me/v1/";

    @Inject
    public PostalCodeDistanceService() {}

    public void register() {
        log.info("Registered PostalCodeDistanceService");
    }

    /**
     * This method is to get distance between two zipcode
     *
     * @param originPostalCode - the postal code that user located
     * @param zipcodeToCompare - the postal code that restaurant located
     * @return distance in double
     */
    public Double getPostalCodesDistance(String originPostalCode, String zipcodeToCompare) {
        String uri =
                String.format(
                        "%s/distance/%s/%s", DISTANCE_API, originPostalCode, zipcodeToCompare);
        PostalCodeDistance postalCodeDistance = new PostalCodeDistance();

        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            CloseableHttpResponse response = client.execute(new HttpGet(uri));
            String bodyAsString = EntityUtils.toString(response.getEntity());
            log.info(bodyAsString);

            ObjectMapper mapper = new ObjectMapper();
            postalCodeDistance = mapper.readValue(bodyAsString, PostalCodeDistance.class);

        } catch (ParseException | IOException e) {
            log.error("Unable to get data from zipcode-radius: {}", e);
        }
        return postalCodeDistance.getDistance();
    }
}
