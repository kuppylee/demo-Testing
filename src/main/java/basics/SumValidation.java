package basics;

import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SumValidation {
    @Test
    public void sumOfCoursesAmount(){
        JsonPath jsonPath = new JsonPath(PayLoad.CoursePrice());
        int numberOfCourses = jsonPath.getInt("courses.size()");
        int sum = 0;

        for(int i = 0; i<numberOfCourses; i++){
            int coursePrices = jsonPath.getInt("courses["+i+"].price");
            int copiesSold = jsonPath.getInt("courses["+i+"].copies");
            int amount = coursePrices * copiesSold;
            sum = sum + amount;
        }
        int purchaseAmount = jsonPath.getInt("dashboard.purchaseAmount");
        Assert.assertEquals(sum,purchaseAmount);
    }





}
