package basics;

import io.restassured.path.json.JsonPath;
import org.testng.Assert;

public class ComplexJsonParser {

    public static void main (String[] args){

        JsonPath jsonPath = new JsonPath(PayLoad.CoursePrice());
//        Print number of courses
        int numberOfCourses = jsonPath.getInt("courses.size()");
        System.out.println(numberOfCourses);

//        print purchase amount
        int totalAmount = jsonPath.getInt("dashboard.purchaseAmount");
        System.out.println(totalAmount);

        int calculatedAmount = 0 ;

//        print title of the first course
        String firstCourseTitle = jsonPath.getString("courses[0].title");
        System.out.println(firstCourseTitle);

//        print all course title and their respective classes
        for (int i = 0; i < numberOfCourses ; i++){
            String courseTitles = jsonPath.getString("courses["+i+"].title");
            int coursePrices = jsonPath.getInt("courses["+i+"].price");
            int copiesSold = jsonPath.getInt("courses["+i+"].copies");
            System.out.println("Title of course is "+courseTitles+" and the price is "+coursePrices+"");

            // print number of sold copies for RPA
            if (courseTitles.equalsIgnoreCase("RPA")){
                int numberOfCopiesSold = jsonPath.getInt("courses["+i+"].copies");
                System.out.println("The sold copies for "+ courseTitles +" is "+numberOfCopiesSold+"");
            }
        }
    }

}
