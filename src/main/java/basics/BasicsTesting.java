package basics;

import groovy.json.JsonParser;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class BasicsTesting {
    public static void main(String[] args) throws IOException {
        // given() - all the input details
        // when() - submit the API -resource,http method
        // then() - validate the response
        // to supply the body with a payload.json file
        // convert the content of the file to string
        baseURI = "https://rahulshettyacademy.com";
        // ADD new place
        String response = null;
            response = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
                    .body(PayLoad.addPlace()).when().post("/maps/api/place/add/json").then().assertThat().statusCode(200)
                    .body("scope", equalTo("APP")).header("Server", "Apache/2.4.41 (Ubuntu)").extract().response().asString();

        // parsing the string into Json to get the placeID by the path
        String placeID = ReUsableMethods.convertRawToJson(response).getString("place_id");
        System.out.println(placeID);

        //  Update place using the gotten place ID
        String inputedNewAddress = "70 Summer walk, USA";
        given().queryParam("key", "qaclick123").header("content-Type", "application/json")
                .body("{\n" +
                        "\"place_id\":\""+placeID+"\",\n" +
                        "\"address\":\""+inputedNewAddress+"\",\n" +
                        "\"key\":\"qaclick123\"\n" +
                        "}").when().put("/maps/api/place/update/json").then().log().all().assertThat().statusCode(200)
                .body("msg", equalTo("Address successfully updated"));


        // Get place
       String getResponse = given().log().all().queryParam("key","qaclick123").queryParam("place_id", placeID)
                .when().get("maps/api/place/get/json").then().log().all().assertThat()
                .statusCode(200).extract().response().asString();

       String actualAddress = ReUsableMethods.convertRawToJson(getResponse).getString("address");
        System.out.println();
        Assert.assertEquals(actualAddress,inputedNewAddress);

    }

    public static String generateStringFromResource(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }


}
