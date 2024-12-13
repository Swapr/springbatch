

package com.example.SpringBatch;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.PlatformTransactionManager;

import static org.mockito.Mockito.*;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = "userFile=classpath:customers-100.csv")

class BatchConfigTest {

	 @InjectMocks
	    private BatchConfig batchConfig;

	    @Mock
	    private UserRepository userRepository;

	    @Mock
	    private PlatformTransactionManager transactionManager;

	    @Mock
	    private JobRepository jobRepository;

	    @Mock
	    private JobListener jobListener;

	    @Mock
	    private Steplistener steplistener;

	    @Mock
	    private FlatFileItemReader<User> flatFileItemReader;

	    @Mock
	    private LineMapper<User> mockLineMapper;

	    @Mock
	    private FileSystemResource fileSystemResource;
	    
	    @Mock
	    private RepositoryItemWriter<User> itemWriter;

	    @BeforeEach
	    void setUp() {
	        MockitoAnnotations.openMocks(this);
	    }

	    @Test
	    void testFlatFileItemReaderWithMockedCSV() throws Exception {
	        String mockCSVLine = "1,John,Doe,US,john.doe@example.com";
	        User mockUser = new User();
	        mockUser.setUserId(1);
	        mockUser.setFirstName("John");
	        mockUser.setLastName("Doe");
	        mockUser.setCountry("US");
	        mockUser.setEmail("john.doe@example.com");

	        when(mockLineMapper.mapLine(mockCSVLine, 1)).thenReturn(mockUser);
	        flatFileItemReader.setResource(fileSystemResource);
	        flatFileItemReader.setLineMapper(mockLineMapper);

	        flatFileItemReader.open(new ExecutionContext());

	        User resultUser = mockLineMapper.mapLine(mockCSVLine, 1);

	        assertNotNull(resultUser);
	        assertEquals(1, resultUser.getUserId());
	        assertEquals("John", resultUser.getFirstName());
	        assertEquals("Doe", resultUser.getLastName());
	        assertEquals("US", resultUser.getCountry());
	        assertEquals("john.doe@example.com", resultUser.getEmail());

	        verify(mockLineMapper, times(1)).mapLine(mockCSVLine, 1);
	    }
    
		
	    
	    
	    @Test
	    void testinsertItemWriter() throws Exception {
	    	
	    	RepositoryItemWriter<User> itemWriter=batchConfig.insertitemWriter();
	    	User user=new User();
	    	
	    	assertNotNull(itemWriter);
	    	
	    	
	    	itemWriter.write(new Chunk<>(Collections.singletonList(user)));
	    	
	    	verify(userRepository,times(1)).save(user);
	    	
	    }
	    
	    
	    
	    
	    
	    
    
}
