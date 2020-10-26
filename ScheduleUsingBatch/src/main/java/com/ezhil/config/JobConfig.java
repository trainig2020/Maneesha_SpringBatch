
package com.ezhil.config;

import java.util.Arrays;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.ezhil.model.Employee;
import com.ezhil.processor.EmployeeProcessor;
import com.ezhil.writer.ConsoleItemWriter;
import com.ezhil.writer.DatabaseWriter;

@Configuration
@EnableBatchProcessing
public class JobConfig {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	public DataSource dataSource;

	
    static Resource[] resources;

	
	

	public JobConfig() {
		// TODO Auto-generated constructor stub
	}



	public Resource[] getResources() {
		
		
		return resources;
	}



	public void setResources(Resource[] resources) {
		this.resources = resources;
	}



	
	// @Value("classpath:*.csv") private Resource[] inputResources;

	@Bean
	@StepScope
	@Qualifier
	public MultiResourceItemReader<Employee> multiResourceItemReader() throws Exception {
		MultiResourceItemReader<Employee> resourceItemReader = new MultiResourceItemReader<Employee>();
		ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();

		
		Resource[] resources = {};
		String filePath = "file:";

		resources = patternResolver.getResources(filePath + "C:/Users/EZHILARASI/Documents/CSV/*.csv");

		
		 
		resourceItemReader.setResources(resources);
		resourceItemReader.setDelegate(reader());

		return resourceItemReader;

	}

	@Bean
	@StepScope
	@Qualifier
	public MultiResourceItemReader<Employee> multiResourceItemReader2() throws Exception {

		MultiResourceItemReader<Employee> resourceItemReader = new MultiResourceItemReader<Employee>();
		
		JobConfig spd = new JobConfig();
		resources = spd.getResources();
		for (Resource resource : resources) {
			System.out.println("resource name : " + resource.getFilename());
		}

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
	public DatabaseWriter writer() {
		return new DatabaseWriter();
	}

	/*
	 * @Bean public JdbcBatchItemWriter<Employee> writer() {
	 * JdbcBatchItemWriter<Employee> writer = new JdbcBatchItemWriter<Employee>();
	 * writer.setItemSqlParameterSourceProvider(new
	 * BeanPropertyItemSqlParameterSourceProvider<>());
	 * writer.setSql("INSERT INTO employee   " +
	 * "VALUES (:empId,:empName,:mobile)"); writer.setDataSource(dataSource);
	 * 
	 * return writer; }
	 */

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
	public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setMaxPoolSize(10);
		taskExecutor.afterPropertiesSet();
		taskExecutor.getActiveCount();

		return taskExecutor;
	}


	@Bean(name ="autoScheJob" )
	public Job autoSchJob() throws Exception {
		return jobBuilderFactory.get("autoScheJob").incrementer(new RunIdIncrementer()).start(step1()).build();
	}
	
	@Bean(name ="manualScheJob" )
	public Job job2() throws Exception {
		return jobBuilderFactory.get("manualScheJob").start(step2()).build();
	}

	@Bean
	public Step step1() throws Exception {
		return stepBuilderFactory.get("step1").<Employee, Employee>chunk(5).reader(multiResourceItemReader())
				.processor(processor()).writer(compositeItemWriter()).taskExecutor(taskExecutor()).build();
	}
	
	@Bean
	public Step step2() throws Exception {
		return stepBuilderFactory.get("step2").<Employee, Employee>chunk(5).reader(multiResourceItemReader2())
				.processor(processor()).writer(compositeItemWriter()).taskExecutor(taskExecutor()).build();
	}

}
