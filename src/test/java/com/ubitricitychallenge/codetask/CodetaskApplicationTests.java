package com.ubitricitychallenge.codetask;

import com.ubitricitychallenge.codetask.domain.MissingChargingPointException;
import com.ubitricitychallenge.codetask.domain.OperationForbiddenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CodetaskApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	private int port;

	private String address;

	@BeforeEach
	void setUp() {
		 address = "http://localhost:" + port + "/api/v1/carparkubi/";
	}

	@Test
	void testUsage() {
		String expectedReport =
				"CP1 AVAILABLE\n" +
				"CP2 AVAILABLE\n" +
				"CP3 AVAILABLE\n" +
				"CP4 AVAILABLE\n" +
				"CP5 AVAILABLE\n" +
				"CP6 AVAILABLE\n" +
				"CP7 AVAILABLE\n" +
				"CP8 AVAILABLE\n" +
				"CP9 AVAILABLE\n" +
				"CP10 AVAILABLE";
		assertEquals(expectedReport, getStatusReport());

		// Example 1 from the Coding-challenge
		connectChargingPoint(0);
		expectedReport =
				"CP1 OCCUPIED 20A\n" +
				"CP2 AVAILABLE\n" +
				"CP3 AVAILABLE\n" +
				"CP4 AVAILABLE\n" +
				"CP5 AVAILABLE\n" +
				"CP6 AVAILABLE\n" +
				"CP7 AVAILABLE\n" +
				"CP8 AVAILABLE\n" +
				"CP9 AVAILABLE\n" +
				"CP10 AVAILABLE";
		assertEquals(expectedReport, getStatusReport());

		// Example 2 from the Coding-challenge
		for (int i = 1; i < 6; i++) {
			connectChargingPoint(i);
		}

		expectedReport =
				"CP1 OCCUPIED 10A\n" +
				"CP2 OCCUPIED 10A\n" +
				"CP3 OCCUPIED 20A\n" +
				"CP4 OCCUPIED 20A\n" +
				"CP5 OCCUPIED 20A\n" +
				"CP6 OCCUPIED 20A\n" +
				"CP7 AVAILABLE\n" +
				"CP8 AVAILABLE\n" +
				"CP9 AVAILABLE\n" +
				"CP10 AVAILABLE";
		assertEquals(expectedReport, getStatusReport());

		// could not connect already connected charging point
		ResponseEntity<Object> response = restTemplate.postForEntity(
		address + "connect?charging_point_id=0", "", Object.class
			);
		assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

		// could not disconnect already disconnected charging point
		response = restTemplate.postForEntity(address + "connect?charging_point_id=0", "", Object.class);
		assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

		// test disconnecting
		restTemplate.delete(address + "disconnect?charging_point_id=0");
		expectedReport =
				"CP1 AVAILABLE\n" +
				"CP2 OCCUPIED 20A\n" +
				"CP3 OCCUPIED 20A\n" +
				"CP4 OCCUPIED 20A\n" +
				"CP5 OCCUPIED 20A\n" +
				"CP6 OCCUPIED 20A\n" +
				"CP7 AVAILABLE\n" +
				"CP8 AVAILABLE\n" +
				"CP9 AVAILABLE\n" +
				"CP10 AVAILABLE";
		assertEquals(expectedReport, getStatusReport());
	}

	@Test
	void testMissingChargePoint() {
		ResponseEntity<Object> response = restTemplate.postForEntity(
		address + "connect?charging_point_id=-1", "", Object.class)
			;
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

		response = restTemplate.postForEntity(address + "connect?charging_point_id=10", "", Object.class);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	private String getStatusReport() {
		ResponseEntity<String> entity = restTemplate.getForEntity(address + "report", String.class);
		assertEquals(HttpStatus.OK, entity.getStatusCode());
		return entity.getBody();
	}

	private void connectChargingPoint(int chargingPointIndex) {
		ResponseEntity<Object> response = restTemplate.postForEntity(
		address + "connect?charging_point_id=" + chargingPointIndex, "", Object.class
			);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

}
