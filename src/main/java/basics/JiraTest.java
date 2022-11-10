package basics;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

import java.io.File;

import static io.restassured.RestAssured.*;

public class JiraTest {

    public static void main(String[] args){
        baseURI = "http://localhost:8080";
        // login scenario
        SessionFilter session = new SessionFilter();
        String response = given().header("Content-Type","application/json").body("{ \"username\": \"bjkuppy\", \"password\": \"Music@42\" }").log().all().filter(session).when()
                .post("/rest/auth/1/session").then().extract().response().asString();

        // Add comments
        String expectedMessage = "Hi, How are you";
         String addCommentsResponse = given().pathParam("key","10101").log().all().header("Content-Type","application/json").body(" {\n" +
                "    \"body\": \""+expectedMessage+"\",\n" +
                "    \"visibility\": {\n" +
                "        \"type\": \"role\",\n" +
                "        \"value\": \"Administrators\"\n" +
                "    }\n" +
                "} ").filter(session).when().post("/rest/api/2/issue/{key}/comment").then().log().all().assertThat().statusCode(201).extract().response().asString();

         // get comments by id
        JsonPath jsonPath = new JsonPath(addCommentsResponse);
        String commentId = jsonPath.getString("id");

        // Add Attachments
        given().header("X-Atlassian-Token","no-check").filter(session).pathParam("key", "10101").header("Content-Type","multipart/form-data").multiPart("file", new File("jira.txt"))
                .when().post("rest/api/2/issue/{key}/attachments").then().log().all().assertThat().statusCode(200);

        // Get Issues and return only comments field
       String issuesResponse = given().filter(session).pathParam("key","10101").queryParam("fields", "comment").log().all().when()
               .get("/rest/api/2/issue/{key}").then().log().all().extract().response().asString();

       // To validate if the comment i sent is present
        JsonPath commentJsonPath = new JsonPath(issuesResponse);
       int  commentCount = commentJsonPath.getInt("fields.comment.comments.size()");
       for (int i = 0; i < commentCount; i++){
          String responseCommentIDs = commentJsonPath.get("fields.comment.comments["+i+"].id").toString();
          if(responseCommentIDs.equalsIgnoreCase(commentId)){
              String message = commentJsonPath.get("fields.comment.comments["+i+"].body").toString();
              System.out.println(message);
              Assert.assertEquals(message, expectedMessage);


          }
       }



    }
}
