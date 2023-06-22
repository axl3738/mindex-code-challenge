package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.core.*;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeServiceImplTest {

    private String employeeUrl;
    private String employeeIdUrl;
	private String reportIdUrl;

    @Autowired
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
		reportIdUrl = "http://localhost:" + port + "/report/{id}";
		
    }

    @Test
    public void testCreateReadUpdate() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        // Create checks
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        assertNotNull(createdEmployee.getEmployeeId());
        assertEmployeeEquivalence(testEmployee, createdEmployee);


        // Read checks
        Employee readEmployee = restTemplate.getForEntity(employeeIdUrl, Employee.class, createdEmployee.getEmployeeId()).getBody();
        assertEquals(createdEmployee.getEmployeeId(), readEmployee.getEmployeeId());
        assertEmployeeEquivalence(createdEmployee, readEmployee);
		
		HttpEntity requestEntity = new HttpEntity<>(null, null);
		List<Employee> readEmployeeList = restTemplate.exchange(employeeUrl, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<Employee>>(){}).getBody();
		assertNotNull(readEmployeeList);

        // Update checks
        readEmployee.setPosition("Development Manager");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Employee updatedEmployee =
                restTemplate.exchange(employeeIdUrl,
                        HttpMethod.PUT,
                        new HttpEntity<Employee>(readEmployee, headers),
                        Employee.class,
                        readEmployee.getEmployeeId()).getBody();

        assertEmployeeEquivalence(readEmployee, updatedEmployee);
    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }
	
	@Test
    public void testReadReport() {
		// Test case that should mirror the basic recursion test case of the snapshot database
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("Pete");
        testEmployee.setLastName("Best");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");
		
		Employee testEmployee2 = new Employee();
        testEmployee2.setFirstName("George");
        testEmployee2.setLastName("Harrison");
        testEmployee2.setDepartment("Engineering");
        testEmployee2.setPosition("Developer");
		
		
		Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();
		Employee createdEmployee2 = restTemplate.postForEntity(employeeUrl, testEmployee2, Employee.class).getBody();
		
		List<Employee> employeeList = new ArrayList<Employee>();
		employeeList.add(createdEmployee);
		employeeList.add(createdEmployee2);
		
		Employee testEmployee3 = new Employee();
        testEmployee3.setFirstName("Ringo");
        testEmployee3.setLastName("Starr");
        testEmployee3.setDepartment("Engineering");
        testEmployee3.setPosition("Developer II");
		testEmployee3.setDirectReports(employeeList);
		Employee createdEmployee3 = restTemplate.postForEntity(employeeUrl, testEmployee3, Employee.class).getBody();
		
		Employee testEmployee4 = new Employee();
        testEmployee4.setFirstName("Paul");
        testEmployee4.setLastName("McCartney");
        testEmployee4.setDepartment("Engineering");
        testEmployee4.setPosition("Developer II");
		Employee createdEmployee4 = restTemplate.postForEntity(employeeUrl, testEmployee4, Employee.class).getBody();
		
		List<Employee> employeeList2 = new ArrayList<Employee>();
		employeeList2.add(createdEmployee3);
		employeeList2.add(createdEmployee4);
		
		Employee testEmployee5 = new Employee();
        testEmployee5.setFirstName("John");
        testEmployee5.setLastName("Lennon");
        testEmployee5.setDepartment("Engineering");
        testEmployee5.setPosition("Developer III");
		testEmployee5.setDirectReports(employeeList2);
		Employee createdEmployee5 = restTemplate.postForEntity(employeeUrl, testEmployee5, Employee.class).getBody();
		
		ReportingStructure testReportingStructure = new ReportingStructure(createdEmployee5,4);

        // Read checks
        ReportingStructure readReportingStructure = restTemplate.getForEntity(reportIdUrl, ReportingStructure.class, createdEmployee5.getEmployeeId()).getBody();
        assertReportingStructureEquivalence(testReportingStructure, readReportingStructure);
    }
	
	private static void assertReportingStructureEquivalence(ReportingStructure expected, ReportingStructure actual) {
		assertEmployeeEquivalence(expected.getEmployee(), actual.getEmployee());
        assertEquals(expected.getNumberOfReports(), actual.getNumberOfReports());
    }
}
