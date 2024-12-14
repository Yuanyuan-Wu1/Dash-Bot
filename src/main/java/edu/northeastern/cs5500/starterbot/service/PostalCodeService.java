package edu.northeastern.cs5500.starterbot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
public class PostalCodeService implements Service {
    public static final String ZIPCODE_API = "https://zipcode-radius.glitch.me/v1/";
    public static final int DEFAULT_RADIUS_KILOMETERS = 20;

    @Inject
    public PostalCodeService() {}

    public void register() {
        log.info("Registered PostalCodeService");
    }

    public List<String> getNearbyPostalCodes(String originPostalCode) {
        return getNearbyPostalCodes(originPostalCode, DEFAULT_RADIUS_KILOMETERS);
    }

    public List<String> getNearbyPostalCodes(String originPostalCode, int radiusKilometers) {
        String uri =
                String.format("%s/nearby/%s/%d", ZIPCODE_API, originPostalCode, radiusKilometers);
        ArrayList<String> result = new ArrayList<>();
        // post codes are nearby themselves
        result.add(originPostalCode);

        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            CloseableHttpResponse response = client.execute(new HttpGet(uri));
            String bodyAsString = EntityUtils.toString(response.getEntity());

            ObjectMapper mapper = new ObjectMapper();
            NearbyZipcode[] nearbyZipcodes = mapper.readValue(bodyAsString, NearbyZipcode[].class);

            for (NearbyZipcode nearbyZipcode : nearbyZipcodes) {
                result.add(Integer.toString(nearbyZipcode.getZipcode()));
            }
        } catch (ParseException | IOException e) {
            log.error("Unable to get data from zipcode-radius: {}", e);
        }
        return result;
    }
}
