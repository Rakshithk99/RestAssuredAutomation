package api.test;


import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.async.AsyncLogger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;


import api.endpoints.UserEndPoints2;
import api.payload.User;
import groovyjarjarantlr4.v4.runtime.misc.LogManager;
import io.restassured.response.Response;

public class UserTests2 {
	
	Faker faker;
	User userPayload;
	@BeforeClass
	public void setupData() {
		faker=new Faker();
		userPayload = new User();
		
		 final Logger logger;
		
		userPayload.setId(faker.idNumber().hashCode());
		userPayload.setUsername(faker.name().username());
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().emailAddress());
		userPayload.setPassword(faker.internet().password());
		userPayload.setPhone(faker.phoneNumber().cellPhone());
		
		//logs
		//logger=LogManager.getLogger(this.getClass()); //Not working
	}
	
	@Test(priority=1)
	public void testPostUser() {
		//logger.info("*********CReating User************");
		Response response = UserEndPoints2.createUser(userPayload);
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(), 200);
		
	}
	
	@Test(priority=2)
	public void testGetUserByName() {
		//System.out.println("username: "+this.userPayload.getUsername());
		Response response = UserEndPoints2.readUser(this.userPayload.getUsername());
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(), 200);
	}
	
	@Test(priority=3)
	public void testUpdateUser() {
		//update data using payload
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().emailAddress());
		
		Response response = UserEndPoints2.updateUser(this.userPayload.getUsername(), userPayload);
		response.then().log().all();
		response.then().log().body().statusCode(200);
		Assert.assertEquals(response.getStatusCode(), 200);
		//Checking data after update
				Response responseAfterUpdate = UserEndPoints2.readUser(this.userPayload.getUsername());
				responseAfterUpdate.then().log().all();
				Assert.assertEquals(responseAfterUpdate.getStatusCode(), 200);
		
	}
	
	@Test(priority=4)
	public void testDeleteUser() {
		Response response = UserEndPoints2.deleteUser(this.userPayload.getUsername());
		response.then().log().all();
		Assert.assertEquals(response.getStatusCode(), 200);
		Assert.assertEquals(response.jsonPath().get("message"), this.userPayload.getUsername());
		
		
	}
	
	
}
