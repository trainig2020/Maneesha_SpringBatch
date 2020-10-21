
package com.ezhil.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.ezhil.model.Employee;
import com.ezhil.processor.EmployeeProcessor;

@Configuration
@EnableBatchProcessing
public class JobConfig {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	public DataSource dataSource;

	// @Value("classpath:*.csv") private Resource[] inputResources;

	@Bean
	@StepScope
	public MultiResourceItemReader<Employee> multiResourceItemReader() throws Exception {
		MultiResourceItemReader<Employee> resourceItemReader = new MultiResourceItemReader<Employee>();
		ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();

		//List<Resource> fileSystemResources = new ArrayList<>();
		Resource[] resources = {};
		String filePath = "file:";

		resources = patternResolver.getResources(filePath + "C:/Users/EZHILARASI/Documents/CSV/*.csv");

		/*
		 * for (Resource resource : resources) {
		 * 
		 * fileSystemResources.add(resource);
		 * 
		 * }
		 */
		resourceItemReader.setResources(resources);
		resourceItemReader.setDelegate(reader());

		return resourceItemReader;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })

	@Bean
	public FlatFileItemReader<Employee> reader() throws Exception {

		FlatFileItemReader<Employee> reader = new FlatFileItemReader<Employee>();
		reader.setLinesToSkip(1);

		// Configure how each line will be parsed and mapped to different values
		reader.setLineMapper(new DefaultLineMapper() {
			{ // 3 columns in each row
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames(new String[] { "empId", "empName", "mobile" });
					}
				}); // Set values in Employee class
				setFieldSetMapper(new BeanWrapperFieldSetMapper<Employee>() {
					{
						setTargetType(Employee.class);
					}
				});
			}
		});

		return reader;
	}

	@Bean
	public EmployeeProcessor processor() {
		return new EmployeeProcessor();

	}

	@Bean
	public JdbcBatchItemWriter<Employee> writer() {
		JdbcBatchItemWriter<Employee> writer = new JdbcBatchItemWriter<Employee>();
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
		writer.setSql("INSERT INTO employee   " + "VALUES (:empId,:empName,:mobile)");
		writer.setDataSource(dataSource);

		return writer;
	}

	@Bean
	public ConsoleItemWriter<Employee> customWriter() {
		return new ConsoleItemWriter<Employee>();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })

	@Bean
	public CompositeItemWriter<Employee> compositeItemWriter() {
		CompositeItemWriter writer = new CompositeItemWriter();
		writer.setDelegates(Arrays.asList(writer(), customWriter()));
		return writer;
	}

	@Bean
	public Job autoSchJob() throws Exception {
		return jobBuilderFactory.get("autoSchJob").incrementer(new RunIdIncrementer()).flow(step1()).end().build();
	}

	@Bean
	public Step step1() throws Exception {
		return stepBuilderFactory.get("step1").<Employee, Employee>chunk(10).reader(multiResourceItemReader())
				.processor(processor()).writer(compositeItemWriter()).build();
	}

}
