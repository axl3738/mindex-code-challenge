package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.helper.DateValidatorWithDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.*;
import java.text.*;

@Service
public class CompensationServiceImpl implements CompensationService {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    @Autowired
    private CompensationRepository compensationRepository;
	
	@Autowired
	private EmployeeRepository employeeRepository;

    @Override
    public Compensation create(Compensation compensation) {
        LOG.debug("Creating compensation [{}]", compensation);

		String receivedEmployeeId = compensation.getEmployee().getEmployeeId();
		Employee employee = employeeRepository.findByEmployeeId(receivedEmployeeId);
		
		// Check if the employee already exists in the employee database.
		if(employee == null)
		{
			throw new RuntimeException("Employee does not exist in database yet. Please add");
		}
		// Check if the employee already has a compensation in the database. We do not want multiple compensations linked to a single employee being created.
		Compensation compensationInDb = compensationRepository.findByEmployee(employee);
		if(compensationInDb != null)
		{
			throw new RuntimeException("Compensation exists already for employee with id: " + receivedEmployeeId);
		}
		
		DateValidatorWithDateFormat validator = new DateValidatorWithDateFormat("MM/dd/yy");
		if(!validator.isValid(compensation.getEffectiveDate())){
			throw new RuntimeException("Incorrect Date Format. Please use the format MM/dd/yy for the effective date ");
		}
        compensationRepository.insert(compensation);

        return compensation;
    }

    @Override
    public Compensation read(String id) {
        LOG.debug("Creating compensation with id [{}]", id);
		
		Employee employee = employeeRepository.findByEmployeeId(id);
		
        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }
		
		Compensation compensation = compensationRepository.findByEmployee(employee);
		
		if (compensation == null) {
            throw new RuntimeException("No Compensation for employee exists with employeeId: " + id);
        }

        return compensation;
    }
	
	@Override
    public List<Compensation> readAll() {
		List<Compensation> compensationList = compensationRepository.findAll();
		// List<Compensation> employeeList = 	new ArrayList<Employee>();
		// employeeList = employeeRepository.findAll();

        return compensationList;
    }
	
}
