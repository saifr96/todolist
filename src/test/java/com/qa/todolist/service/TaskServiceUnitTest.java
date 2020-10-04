package com.qa.todolist.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.qa.todolist.dto.TaskDTO;
import com.qa.todolist.persistence.domain.Task;
import com.qa.todolist.persistence.repository.TaskRepository;

@SpringBootTest
public class TaskServiceUnitTest {
	
	@Autowired
	private TaskService service;
	
	@MockBean
	private TaskRepository repo;
	
	@MockBean
	private ModelMapper modelMapper;
	
	// Testing Variables
	private List<Task> taskList;
	private Task tester;
	private Task testerId;
	private Task emptyTester;
	private TaskDTO dto;
	
	// Testing constants
	final Long id = 1L;
	final String exampleName = "clean";
	// List of Guitarists here ? <== <== <==
	
	// Define Mapping Function
	private TaskDTO mapToDTO(Task task) {
		return this.modelMapper.map(task, TaskDTO.class);
	}
	
	// Initialise variables before each test
	@BeforeEach
	void init() {
		// Instantiate lists to emulate returned data
		this.taskList = new ArrayList<>();
		this.taskList.add(tester);
		
		// Create example band
		this.tester = new Task(
				this.exampleName);
		
		// Create id band
		this.testerId = new Task(
				this.exampleName);
		
		testerId.setId(this.id);
		
		// Create example empty band
		this.emptyTester = new Task();
		
		// Create DTO from band with Id
		this.dto = new ModelMapper().map(testerId, TaskDTO.class);
	}
	
	@Test
	void createTest() {
		// Setup mock data
		when(this.modelMapper.map(mapToDTO(tester), Task.class))
		.thenReturn(tester);
		when(this.repo.save(tester))
		.thenReturn(testerId);
		when(this.modelMapper.map(testerId, TaskDTO.class))
		.thenReturn(dto);
		
		// Test assertion
		assertThat(this.dto)
		.isEqualTo(this.service.create(tester));
		
		// Check repo methods used in this test
		verify(this.repo, times(1)).save(this.tester);
		
	}
}