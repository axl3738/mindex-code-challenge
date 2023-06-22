package com.mindex.challenge.service;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import java.util.*;

public interface EmployeeService {
    Employee create(Employee employee);
    Employee read(String id);
	List<Employee> readAll();
    Employee update(Employee employee);
	ReportingStructure readReport(String id);
}
