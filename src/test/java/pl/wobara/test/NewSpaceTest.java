package pl.wobara.test;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import pl.wobara.api.RestHelper;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;
import pl.wobara.api.model.Output;

import java.util.*;
import java.util.stream.Collectors;

@Guice
public class NewSpaceTest {

    @Test
    @Description("Test if API responds to valid input parameters")
    public void testValidInputParameters() {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("date-min", "1900-01-01");
        queryParams.put("date-max", "2100-01-01");
        queryParams.put("des", "433");
        queryParams.put("dist-max", "0.2");

        Output output = RestHelper.getOutput(queryParams);
        List<Map<String, String>> response = mapFieldsToData(output);

        SoftAssertions softly = new SoftAssertions();
        // Assert that the response is not empty
        softly.assertThat(response.isEmpty()).isFalse();
        // Assert that the response contains the expected number of rows
        softly.assertThat(response.size()).isEqualTo(output.getCount());

        // Assert that specific fields in the response have expected values
        Map<String, String> firstRow = response.get(0);

        softly.assertThat(firstRow.get("des")).isEqualTo("433");
        softly.assertThat(firstRow.get("cd")).isEqualTo("1931-Jan-30 04:07");
        softly.assertThat(firstRow.get("dist")).isEqualTo("0.174073145828143");
        // Assert that objects count is equal to the "count" field value
        softly.assertThat(response.size())
                .as("Close approach count")
                .isEqualTo(output.getCount());

        softly.assertAll();
    }

    @Test
    @Description("Test if API responds to invalid input parameters (dist-max=-1)")
    public void testInvalidInputParameters() {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("date-min", "2100-01-01");
        queryParams.put("date-max", "1900-01-01");
        queryParams.put("des", 433);
        queryParams.put("dist-max", -1); // negative distance

        Response response = RestHelper.getCadResponse(queryParams);

        Assertions.assertThat(response.getStatusCode()).as("API should return 400 to invalid query params").isEqualTo(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @Description("Test if API returns objects with proper distance")
    public void testFilterDistMinMax() {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("date-min", "1900-01-01");
        queryParams.put("date-max", "2100-01-01");
        queryParams.put("des", 433);
        queryParams.put("dist-min", 0.2);
        queryParams.put("dist-max", 0.3);

        Output output = RestHelper.getOutput(queryParams);
        List<Map<String, String>> response = mapFieldsToData(output);

        List<Map<String,String>> filtered = response.stream().filter(cad -> {
            double dist = Double.parseDouble(cad.get("dist"));
            if(dist >= 0.2 && dist <= 0.3) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());

        Assertions.assertThat(filtered.size())
                .as("Close approach count should be 4")
                .isEqualTo(4);

    }

    @Test
    @Description("Test if API returns objects sorted by dist in reverse order")
    public void testSortParameter() {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("date-min", "now");
        queryParams.put("sort", "-dist");
        queryParams.put("body", "Earth");

        List<Map<String, String>> data = mapFieldsToData(RestHelper.getOutput(queryParams));
        List<Double> distances = data.stream().map(item -> Double.parseDouble(item.get("dist"))).collect(Collectors.toList());

        Assertions.assertThat(distances).as("Objects should be sorted in reverse order").isSortedAccordingTo(Comparator.reverseOrder());
    }

    @Test
    @Description("Test if API returns 0 objects for kind: c (comets)")
    public void testKindParameter() {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("kind", "c");

        Output output = RestHelper.getOutput(queryParams);

        Assertions.assertThat(output.getCount())
                .as("Number of comets should be 0")
                .isEqualTo(0);
    }

    private List<Map<String,String>> mapFieldsToData(Output output) {
        List<Map<String, String>> mappedData = new ArrayList<>();

        List<List<String>> dataList = output.getData();
        List<String> fields = output.getFields();

        for (List<String> cadRecord : dataList) {
            Map<String, String> map = new HashMap<>();
            for (int i = 0; i < cadRecord.size(); i++) {
                String fieldName = fields.get(i);
                String fieldValue = cadRecord.get(i);
                map.put(fieldName, fieldValue);
            }
            mappedData.add(map);
        }
        return mappedData;
    }

}