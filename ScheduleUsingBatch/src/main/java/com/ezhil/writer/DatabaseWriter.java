package com.ezhil.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ezhil.model.Employee;
import com.ezhil.repo.EmpRepo;

@Component
public class DatabaseWriter implements ItemWriter<Employee> {
	
	@Autowired
	private EmpRepo empRepo;

	@Override
	public void write(List<? extends Employee> items) throws Exception {
		empRepo.saveAll(items);
		
	}

}
