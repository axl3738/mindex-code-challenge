package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.*;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee create(Employee employee) {
        LOG.debug("Creating employee [{}]", employee);

        employee.setEmployeeId(UUID.randomUUID().toString());
        employeeRepository.insert(employee);

        return employee;
    }

    @Override
    public Employee read(String id) {
        LOG.debug("Creating employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return employee;
    }
	
	@Override
    public List<Employee> readAll() {
		List<Employee> employeeList = employeeRepository.findAll();

        return employeeList;
    }

    @Override
    public Employee update(Employee employee) {
        LOG.debug("Updating employee [{}]", employee);

		//Employee employeeInDb = employeeRepository.findByEmployeeId(employee.getEmployeeId());
        return employeeRepository.save(employee);
    }
	
	@Override
    public ReportingStructure readReport(String id) {
        LOG.debug("Creating employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);
		int reports = getAllReports(employee);
		ReportingStructure report = new ReportingStructure(employee, reports);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return report;
    }
	
	public int getAllReports(Employee employee)
	{
		List<Employee> newReportingEmployees = 	employee.getDirectReports();
		if(newReportingEmployees == null){
			return 0;
		}
		List<Employee> employeesToAdd = new ArrayList<>();
		int numOfReports = newReportingEmployees.size();
		
			
		for(Employee e : newReportingEmployees){
			numOfReports += getAllReports(employeeRepository.findByEmployeeId(e.getEmployeeId()));
		}	
		return numOfReports;
	
	}
}
