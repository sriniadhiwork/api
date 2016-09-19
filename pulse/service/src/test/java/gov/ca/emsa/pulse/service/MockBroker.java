package gov.ca.emsa.pulse.service;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gov.ca.emsa.pulse.common.domain.PatientSearch;

@RestController
public class MockBroker {
	private static final String brokerSearchActiveQueryResponse = "[{\"id\":1,\"userToken\":\"fake@sample.com\",\"status\":\"ACTIVE\",\"terms\":{\"givenName\":\"John\",\"familyName\":\"Doe\",\"dob\":null,\"ssn\":null,\"gender\":null,\"zip\":null},\"lastRead\":1473775555673,\"orgStatuses\":[{\"id\":144,\"queryId\":1,\"org\":{\"name\":\"IHEOrg3\",\"id\":6,\"organizationId\":6,\"adapter\":\"IHE\",\"ipAddress\":\"127.0.0.1\",\"username\":null,\"password\":null,\"certificationKey\":\"1234567\",\"endpointUrl\":\"http://localhost:9080/mock/ihe\",\"active\":true},\"status\":\"ACTIVE\",\"startDate\":1473775522676,\"endDate\":null,\"success\":null,\"results\":[]},{\"id\":143,\"queryId\":24,\"org\":{\"name\":\"IHEOrg2\",\"id\":5,\"organizationId\":5,\"adapter\":\"IHE\",\"ipAddress\":\"127.0.0.1\",\"username\":null,\"password\":null,\"certificationKey\":\"1234567\",\"endpointUrl\":\"http://localhost:9080/mock/ihe\",\"active\":true},\"status\":\"ACTIVE\",\"startDate\":1473775522674,\"endDate\":null,\"success\":null,\"results\":[]},{\"id\":141,\"queryId\":24,\"org\":{\"name\":\"eHealthExchangeOrg3\",\"id\":3,\"organizationId\":3,\"adapter\":\"eHealth\",\"ipAddress\":\"127.0.0.1\",\"username\":\"org1User\",\"password\":\"password1\",\"certificationKey\":null,\"endpointUrl\":\"http://localhost:9080/mock/ehealthexchange\",\"active\":true},\"status\":\"ACTIVE\",\"startDate\":1473775522671,\"endDate\":null,\"success\":null,\"results\":[]},{\"id\":140,\"queryId\":24,\"org\":{\"name\":\"eHealthExchangeOrg2\",\"id\":2,\"organizationId\":2,\"adapter\":\"eHealth\",\"ipAddress\":\"127.0.0.1\",\"username\":\"org1User\",\"password\":\"password1\",\"certificationKey\":null,\"endpointUrl\":\"http://localhost:9080/mock/ehealthexchange\",\"active\":true},\"status\":\"ACTIVE\",\"startDate\":1473775522670,\"endDate\":null,\"success\":null,\"results\":[]},{\"id\":139,\"queryId\":24,\"org\":{\"name\":\"eHealthExchangeOrg\",\"id\":1,\"organizationId\":1,\"adapter\":\"eHealth\",\"ipAddress\":\"127.0.0.1\",\"username\":\"org1User\",\"password\":\"password1\",\"certificationKey\":null,\"endpointUrl\":\"http://localhost:9080/mock/ehealthexchange\",\"active\":true},\"status\":\"ACTIVE\",\"startDate\":1473775522668,\"endDate\":null,\"success\":null,\"results\":[]},{\"id\":142,\"queryId\":24,\"org\":{\"name\":\"IHEOrg\",\"id\":4,\"organizationId\":4,\"adapter\":\"IHE\",\"ipAddress\":\"127.0.0.1\",\"username\":null,\"password\":null,\"certificationKey\":\"1234567\",\"endpointUrl\":\"http://localhost:9080/mock/ihe\",\"active\":true},\"status\":\"ACTIVE\",\"startDate\":1473775522673,\"endDate\":null,\"success\":null,\"results\":[]}]}]";
	private static final String brokerSearchCompletedQueryResponse = "[{\"id\":1,\"userToken\":\"fake@sample.com\",\"status\":\"COMPLETE\",\"terms\":{\"givenName\":\"John\",\"familyName\":\"Doe\",\"dob\":null,\"ssn\":null,\"gender\":null,\"zip\":null},\"lastRead\":1473775608988,\"orgStatuses\":[{\"id\":141,\"queryId\":1,\"org\":{\"name\":\"eHealthExchangeOrg3\",\"id\":3,\"organizationId\":3,\"adapter\":\"eHealth\",\"ipAddress\":\"127.0.0.1\",\"username\":\"org1User\",\"password\":\"password1\",\"certificationKey\":null,\"endpointUrl\":\"http://localhost:9080/mock/ehealthexchange\",\"active\":true},\"status\":\"COMPLETE\",\"startDate\":1473775522671,\"endDate\":1473775555980,\"success\":true,\"results\":[{\"id\":85,\"givenName\":\"John\",\"familyName\":\"Doe\",\"dateOfBirth\":-461030400000,\"gender\":\"M\",\"phoneNumber\":\"3517869574\",\"address\":null,\"ssn\":\"451674563\"}]},{\"id\":139,\"queryId\":24,\"org\":{\"name\":\"eHealthExchangeOrg\",\"id\":1,\"organizationId\":1,\"adapter\":\"eHealth\",\"ipAddress\":\"127.0.0.1\",\"username\":\"org1User\",\"password\":\"password1\",\"certificationKey\":null,\"endpointUrl\":\"http://localhost:9080/mock/ehealthexchange\",\"active\":true},\"status\":\"COMPLETE\",\"startDate\":1473775522668,\"endDate\":1473775608969,\"success\":true,\"results\":[{\"id\":87,\"givenName\":\"John\",\"familyName\":\"Doe\",\"dateOfBirth\":-461030400000,\"gender\":\"M\",\"phoneNumber\":\"3517869574\",\"address\":null,\"ssn\":\"451674563\"}]},{\"id\":140,\"queryId\":24,\"org\":{\"name\":\"eHealthExchangeOrg2\",\"id\":2,\"organizationId\":2,\"adapter\":\"eHealth\",\"ipAddress\":\"127.0.0.1\",\"username\":\"org1User\",\"password\":\"password1\",\"certificationKey\":null,\"endpointUrl\":\"http://localhost:9080/mock/ehealthexchange\",\"active\":true},\"status\":\"COMPLETE\",\"startDate\":1473775522670,\"endDate\":1473775607975,\"success\":true,\"results\":[{\"id\":86,\"givenName\":\"John\",\"familyName\":\"Doe\",\"dateOfBirth\":-461030400000,\"gender\":\"M\",\"phoneNumber\":\"3517869574\",\"address\":null,\"ssn\":\"451674563\"}]},{\"id\":144,\"queryId\":24,\"org\":{\"name\":\"IHEOrg3\",\"id\":6,\"organizationId\":6,\"adapter\":\"IHE\",\"ipAddress\":\"127.0.0.1\",\"username\":null,\"password\":null,\"certificationKey\":\"1234567\",\"endpointUrl\":\"http://localhost:9080/mock/ihe\",\"active\":true},\"status\":\"COMPLETE\",\"startDate\":1473775522676,\"endDate\":1473775561522,\"success\":true,\"results\":[]},{\"id\":142,\"queryId\":24,\"org\":{\"name\":\"IHEOrg\",\"id\":4,\"organizationId\":4,\"adapter\":\"IHE\",\"ipAddress\":\"127.0.0.1\",\"username\":null,\"password\":null,\"certificationKey\":\"1234567\",\"endpointUrl\":\"http://localhost:9080/mock/ihe\",\"active\":true},\"status\":\"COMPLETE\",\"startDate\":1473775522673,\"endDate\":1473775592713,\"success\":true,\"results\":[]},{\"id\":143,\"queryId\":24,\"org\":{\"name\":\"IHEOrg2\",\"id\":5,\"organizationId\":5,\"adapter\":\"IHE\",\"ipAddress\":\"127.0.0.1\",\"username\":null,\"password\":null,\"certificationKey\":\"1234567\",\"endpointUrl\":\"http://localhost:9080/mock/ihe\",\"active\":true},\"status\":\"COMPLETE\",\"startDate\":1473775522674,\"endDate\":1473775587889,\"success\":true,\"results\":[]}]}]";
	
	@RequestMapping("/search")
	public String provideActiveQueryForTesting(@RequestBody PatientSearch patientSearch){
		if(patientSearch.getDob().equals("05/02/1993") &&
				patientSearch.getFamilyName().equals("Doe") &&
				patientSearch.getGivenName().equals("John") &&
				patientSearch.getGender().equals("M")){
			return brokerSearchActiveQueryResponse;
		}
		return null;

	}
	
	@RequestMapping("/queries/{queryId}")
	public String provideCompletedQueryForTesting(@PathVariable(value="queryId") Long queryId){
		if(queryId.equals(1)){
			return brokerSearchCompletedQueryResponse;
		}
		return null;
	}
}
