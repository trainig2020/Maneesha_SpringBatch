package com.ezhil.config;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import com.ezhil.model.Employee;

public class ConsoleItemWriter<T> implements ItemWriter<T> {
    @Override
    public void write(List<? extends T> items) throws Exception {
        for (T item : items) {
            Employee itemnew = (Employee) item;
            System.out.println(itemnew.toString());
        }
    }
}
