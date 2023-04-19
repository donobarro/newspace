package pl.wobara.api;

import io.restassured.response.Response;
import lombok.experimental.UtilityClass;
import pl.wobara.api.model.Output;

import java.util.Map;

import static io.restassured.RestAssured.given;

@UtilityClass
public class RestHelper {
    public Response getCadResponse(Map<String, Object> queryParams) {
        return given()
                .baseUri("https://ssd-api.jpl.nasa.gov")
                .basePath("cad.api")
                .queryParams(queryParams)
                .when()
                .get();
    }

    public Output getOutput(Map<String, Object> queryParams) {
        return getCadResponse(queryParams).as(Output.class);
    }
}
