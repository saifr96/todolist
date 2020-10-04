package com.qa.todolist.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import com.qa.todolist.dto.TaskDTO;
import com.qa.todolist.dto.TaskListDTO;
import com.qa.todolist.persistence.domain.Task;
import com.qa.todolist.persistence.domain.TaskList;
import com.qa.todolist.persistence.repository.TaskRepository;

@SpringBootTest
public class TaskServiceIntegrationTest {

	@MockBean
	private ModelMapper modelMapper;
	
	@Autowired
	private TaskService service;
	
	@Autowired
	private TaskRepository repo;
	
	private Task tester;
	private Task testerId;
	private List<TaskList> tasklist;
	private TaskDTO dto;
	  private Task testTask;
	    private Task testTaskWithId;
	
	// Mapping function

	private TaskDTO mapToDTO(Task task) {
		return this.modelMapper.map(task, TaskDTO.class);
	}
	
	// Testing constants
	final Long id = 1L;
	final String exampleName = "clean";
	
	@BeforeEach
	void init() {
		this.repo.deleteAll(); // Clear
		this.tester = new Task(this.exampleName);
		this.testerId = this.repo.save(tester);
	}
	
	@Test
	void testCreate() {
		// Test assertion
		assertThat(this.mapToDTO(this.testerId))
		.isEqualTo(this.service.create(tester));
	}
	
	@Test
	void testRead() {
		assertThat(this.service.read(testerId.getId()))
		.isEqualTo(this.mapToDTO(this.testerId));
	}
	
	@Test
	void testReadAll() {
		assertThat(Stream.of(
				this.mapToDTO(testerId))
				.collect(Collectors.toList()))
		.isEqualTo(this.service.read());
	}
	
	@Test
	void testUpdate() {
		Task newTask = new Task("Clean");
		newTask.setId(this.id);
		TaskDTO updatedDTO = mapToDTO(newTask);
		assertThat(updatedDTO)
		.isEqualTo(this.service.update(newTask, this.testerId.getId()));
		

	}
	
	@Test
	void testDelete() {
		assertThat(this.service.delete(this.tester.getId()))
		.isTrue();
	}
}