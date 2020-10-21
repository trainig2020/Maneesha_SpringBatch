/*
 * package com.ezhil.config;
 * 
 * import java.util.Arrays;
 * 
 * import javax.sql.DataSource;
 * 
 * import org.springframework.batch.core.Job; import
 * org.springframework.batch.core.Step; import
 * org.springframework.batch.core.configuration.annotation.
 * EnableBatchProcessing; import
 * org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
 * import
 * org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
 * import org.springframework.batch.core.configuration.annotation.StepScope;
 * import org.springframework.batch.core.launch.support.RunIdIncrementer; import
 * org.springframework.batch.core.partition.support.MultiResourcePartitioner;
 * import org.springframework.batch.core.partition.support.Partitioner; import
 * org.springframework.batch.item.database.
 * BeanPropertyItemSqlParameterSourceProvider; import
 * org.springframework.batch.item.database.JdbcBatchItemWriter; import
 * org.springframework.batch.item.file.FlatFileItemReader; import
 * org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper; import
 * org.springframework.batch.item.file.mapping.DefaultLineMapper; import
 * org.springframework.batch.item.file.transform.DelimitedLineTokenizer; import
 * org.springframework.batch.item.support.CompositeItemWriter; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.beans.factory.annotation.Qualifier; import
 * org.springframework.beans.factory.annotation.Value; import
 * org.springframework.context.annotation.Bean; import
 * org.springframework.context.annotation.Configuration; import
 * org.springframework.core.io.Resource; import
 * org.springframework.core.io.UrlResource; import
 * org.springframework.core.io.support.PathMatchingResourcePatternResolver;
 * import org.springframework.core.io.support.ResourcePatternResolver; import
 * org.springframework.core.task.TaskExecutor; import
 * org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
 * 
 * import com.ezhil.model.Employee; import
 * com.ezhil.processor.EmployeeProcessor;
 * 
 * @Configuration
 * 
 * @EnableBatchProcessing public class PartitionConfig {
 * 
 * @Autowired public JobBuilderFactory jobBuilderFactory;
 * 
 * @Autowired public StepBuilderFactory stepBuilderFactory;
 * 
 * @Autowired public DataSource dataSource;
 * 
 * @Bean("partitioner")
 * 
 * @StepScope public Partitioner partitioner() throws Exception {
 * 
 * MultiResourcePartitioner partitioner = new MultiResourcePartitioner();
 * ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
 * 
 * Resource[] resources = resolver.getResources("classpath:*.csv");
 * 
 * // partitioner.setResources(inputResources);
 * partitioner.setResources(resources); partitioner.partition(1); return
 * partitioner; }
 * 
 * @Bean public TaskExecutor taskExecutor() { ThreadPoolTaskExecutor
 * taskExecutor = new ThreadPoolTaskExecutor(); taskExecutor.setMaxPoolSize(10);
 * taskExecutor.setCorePoolSize(10); taskExecutor.setQueueCapacity(10);
 * taskExecutor.afterPropertiesSet(); return taskExecutor; }
 * 
 * @SuppressWarnings({ "unchecked", "rawtypes" })
 * 
 * @StepScope
 * 
 * @Bean public FlatFileItemReader<Employee> itemReader(
 * 
 * @Value("#{stepExecutionContext[fileName]}") String filename) throws Exception
 * {
 * 
 * FlatFileItemReader<Employee> reader = new FlatFileItemReader<>();
 * reader.setResource(new UrlResource(filename)); // Configure how each line
 * will be parsed and mapped to different values reader.setLineMapper(new
 * DefaultLineMapper() { { // 3 columns in each row setLineTokenizer(new
 * DelimitedLineTokenizer() { { setNames(new String[] { "empId", "empName",
 * "mobile" }); } }); // Set values in Employee class setFieldSetMapper(new
 * BeanWrapperFieldSetMapper<Employee>() { { setTargetType(Employee.class); }
 * }); } }); reader.setLinesToSkip(1); return reader; }
 * 
 * @Bean public EmployeeProcessor processor() { return new EmployeeProcessor();
 * 
 * }
 * 
 * @Bean public JdbcBatchItemWriter<Employee> writer() {
 * JdbcBatchItemWriter<Employee> writer = new JdbcBatchItemWriter<Employee>();
 * writer.setItemSqlParameterSourceProvider(new
 * BeanPropertyItemSqlParameterSourceProvider<>());
 * writer.setSql("INSERT INTO employee  " + "VALUES (:empId,:empName,:mobile)");
 * writer.setDataSource(dataSource);
 * 
 * return writer; }
 * 
 * @Bean public ConsoleItemWriter<Employee> customWriter() { return new
 * ConsoleItemWriter<Employee>(); }
 * 
 * @SuppressWarnings({ "unchecked", "rawtypes" })
 * 
 * @Bean public CompositeItemWriter<Employee> compositeItemWriter() {
 * CompositeItemWriter writer = new CompositeItemWriter();
 * writer.setDelegates(Arrays.asList(writer(), customWriter())); return writer;
 * }
 * 
 * @Bean public Job exportUserJob() throws Exception { return
 * jobBuilderFactory.get("exportUserJob").incrementer(new
 * RunIdIncrementer()).flow(masterStep()).end() .build(); }
 * 
 * @Bean
 * 
 * @Qualifier("masterStep") public Step masterStep() throws Exception { return
 * stepBuilderFactory.get("masterStep").partitioner("step1",
 * partitioner()).step(step1()) .taskExecutor(taskExecutor()).build(); }
 * 
 * @Bean public Step step1() throws Exception { return
 * stepBuilderFactory.get("step1").<Employee,
 * Employee>chunk(5).reader(itemReader("partitioner"))
 * .processor(processor()).writer(compositeItemWriter()).build(); }
 * 
 * }
 */