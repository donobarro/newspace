package pl.wobara.api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import lombok.experimental.UtilityClass;
import pl.wobara.api.model.Output;

import java.util.Map;

import static io.restassured.RestAssured.given;

@UtilityClass
public class RestHelper {
    @Step("Get output response")
    public Response getCadResponse(Map<String, Object> queryParams) {
        return given()
                .baseUri("https://ssd-api.jpl.nasa.gov")
                .basePath("cad.api")
                .queryParams(queryParams)
                .when()
                .get();
    }

    @Step("Deserialize output to Output.class")
    public Output getOutput(Map<String, Object> queryParams) {
        return getCadResponse(queryParams).as(Output.class);
    }
}
