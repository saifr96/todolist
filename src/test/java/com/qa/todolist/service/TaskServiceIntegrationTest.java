package com.qa.todolist.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
//import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.qa.todolist.dto.TaskDTO;
import com.qa.todolist.dto.TaskListDTO;
import com.qa.todolist.persistence.domain.Task;
import com.qa.todolist.persistence.repository.TaskRepository;

@SpringBootTest
public class TaskServiceIntegrationTest {

	 @Autowired
	    private TaskService service;

	    @Autowired
	    private TaskRepository repo;

	    private Task testTask;
	    private Task testTaskWithId;

	  //  @Autowired
	    @MockBean
	    private ModelMapper modelMapper;
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
		this.testTask = new Task(this.exampleName);
		this.testTaskWithId = this.repo.save(testTask);
	}
	
	@Test
	void testCreate() {
		// Test assertion
		assertThat(this.mapToDTO(this.testTaskWithId))
		.isEqualTo(this.service.create(testTask));
	}
	
    @Test
    void testRead() {
		assertThat(this.service.read(testTaskWithId.getId()))
		.isEqualTo(this.mapToDTO(this.testTaskWithId));
    }

    @Test
	void testReadAll() {
		assertThat(Stream.of(this.mapToDTO(testTaskWithId))
		.collect(Collectors.toList()))
		.isEqualTo(this.service.read());
	}
	
	@Test
	void testUpdate() {
		TaskDTO newTask = new TaskDTO(null, "take the car to the carwash", new ArrayList<>());
    	TaskDTO updatedTask = new TaskDTO(this.testTaskWithId.getId(), newTask.getName(), new ArrayList<>());

        assertThat(this.service.update(newTask, this.testTaskWithId.getId()))
            .isEqualTo(this.mapToDTO(testTaskWithId));
	}
	
	@Test
	void testDelete() {
		assertThat(this.service.delete(this.testTask.getId()))
		.isTrue();
	}
}