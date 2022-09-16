package basics;

import io.restassured.path.json.JsonPath;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;

public class DynamicJson {
//    Add new books
    @Test(dataProvider = "newBooksData")
    public void addBook(String isbn, int aisle){
        baseURI = "http://216.10.245.166";
       String response =  given().log().all().header("content-Type", "application/json").body(PayLoad.booksDetails(isbn, aisle))
                .when().post("/Library/Addbook.php").then().log().all().assertThat().statusCode(200)
                .extract().response().asString();
       JsonPath jsonPath = ReUsableMethods.convertRawToJson(response);
       String id = jsonPath.get("ID");
        System.out.println(id);

    }

//    This is to parameterize data using dataprovider annotation.
    @DataProvider(name = "newBooksData")
    public Object[][] getData(){
       return new Object[][] {
                {"oshja",7372},{"ushsa",8272},{"ussha",9289}
        };
    }


}
