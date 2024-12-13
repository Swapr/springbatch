package com.example.SpringBatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.batch.JobLauncherApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {
	

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PlatformTransactionManager transactionManager;
	
	@Autowired
	private JobRepository jobRepository;
	
	@Autowired
	private JobListener jobListener;
	
	@Autowired
	private Steplistener steplistener;
	
	public BatchConfig( UserRepository userRepository ,PlatformTransactionManager transactionManager) {
		
		this.userRepository=userRepository;
		this.transactionManager=transactionManager;
	}
	
	@Value("${userFile}")
	private String fileLoaction;          //file taking from resouurce and name is mentioned in application.properties
	
	
	
	
	
	
	@Bean
	public  FlatFileItemReader<User> flatFileItemReader()  {                                         //to read csv file  
		
		final FlatFileItemReader<User> flatFileItemReader= new FlatFileItemReader<>();
		
			flatFileItemReader.setResource(new FileSystemResource(fileLoaction));
			flatFileItemReader.setName("fileReader");
			flatFileItemReader.setLinesToSkip(1);
			flatFileItemReader.setStrict(false);
			flatFileItemReader.setLineMapper(lineMapper());          //linemapper tells how to store readed data(in this case as java obj)
                    
		
		return flatFileItemReader;
		
	    }
	
	
	public LineMapper<User> lineMapper(){
		
		final DefaultLineMapper<User> lineMapper =new DefaultLineMapper<>();
		final DelimitedLineTokenizer delimitedLineTokenizer=new DelimitedLineTokenizer();   //read data 
		
	
			
			delimitedLineTokenizer.setDelimiter(",");
			delimitedLineTokenizer.setStrict(true);
			delimitedLineTokenizer.setNames("userId","firstName","lastName","country","email");
			
			final BeanWrapperFieldSetMapper<User> beanWrapperFieldSetMapper=new BeanWrapperFieldSetMapper<>();  //map readed data to given type
			beanWrapperFieldSetMapper.setTargetType(User.class);                                               //set which type to map
			
			lineMapper.setLineTokenizer(delimitedLineTokenizer);                                              // reader 
			lineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);                                          //writer to java obj
			
		return lineMapper;
		
	}
	
	
	
	
	
	
	
	
	/*
	 * @Bean public RepositoryItemWriter<User> deleteItemWriter() //to delete data
	 * if already present { final RepositoryItemWriter<User> writer=new
	 * RepositoryItemWriter<>();
	 * 
	 * writer.setRepository(userRepository); writer.setMethodName("delete"); return
	 * writer;
	 * 
	 * }
	 */
	
	
	
	@Bean
	public RepositoryItemWriter<User> insertitemWriter(){                                 //to save new data 
		
		 RepositoryItemWriter<User> writer=new RepositoryItemWriter<>();
		
		writer.setRepository(userRepository);
		writer.setMethodName("save");
		return writer;
		
	}
	
	
   
	
	
	
	
	/*
	 * @Bean public CompositeItemWriter<User> compositeItemWriter(){ // to run
	 * itemdelete writer and itemsavewriter
	 * 
	 * final CompositeItemWriter<User> compositeItemWriter=new
	 * CompositeItemWriter<>();
	 * 
	 * final List<ItemWriter<? super User>> writers=new ArrayList<>();
	 * writers.add(deleteItemWriter()); writers.add(insertitemWriter());
	 * compositeItemWriter.setDelegates(writers); return compositeItemWriter;
	 * 
	 * }
	 */
	
	
		
	
	
	
	@Bean
	 public Step userDataImportStep() {
		return new StepBuilder("userDataImportStep", jobRepository)
				    .<User,User>chunk(10, transactionManager)
				    .reader(flatFileItemReader())
				    .writer(insertitemWriter())
				    .listener(steplistener)
				    .build();
	}
	
	
	
	
	@Bean
	public Job userimportjob()  {
		
		return new JobBuilder("userimportjob",jobRepository)
				.start(userDataImportStep())
				.listener(jobListener)
				.build();
	}
	
	
	
	 @Bean(name="asyncJobLauncher")
	 public JobLauncher asyncJobLauncher(JobRepository jobRepository) throws Exception {
		 
		 

			TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
			jobLauncher.setJobRepository(jobRepository);
			jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
			jobLauncher.afterPropertiesSet();
			return jobLauncher;

	 }
	
	
	
	
	
	
	

}
