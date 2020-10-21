package com.ezhil.processor;

import org.springframework.batch.item.ItemProcessor;

import com.ezhil.model.Employee;


public class EmployeeProcessor implements ItemProcessor<Employee, Employee> {

	@Override
	public Employee process(Employee emp) throws Exception {
		
		return emp;
	}

}
