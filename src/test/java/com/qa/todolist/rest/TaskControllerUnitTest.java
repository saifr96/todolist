package com.qa.todolist.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import com.qa.todolist.dto.TaskDTO;
import com.qa.todolist.persistence.domain.Task;
import com.qa.todolist.service.TaskService;

@SpringBootTest
class TaskControllerUnitTest {

    // the thing we're actually testing
    // (this is the real thing we've made)
    @Autowired
    private TaskController controller;

    // the mock thing that we're connecting to
    // so that any requests we receive are always valid
    @MockBean
    private TaskService service;

    // we need the mapper, because it works with the mock service layer
    @Autowired
    private ModelMapper modelMapper;

    // and we need the dto mapping as well, otherwise we can't test
    // our controller methods (which rely on RE<xDTO>)
    private TaskDTO mapToDTO(Task task) {
        return this.modelMapper.map(task, TaskDTO.class);
    }

    private List<Task> taskList;
    private Task testTask;
    private Task testTaskWithId;
    private TaskDTO taskDTO;

    private final String name = "Wash the car";
    private final Long id = 1L;
    
    
    
    @BeforeEach
    void init() {
        this.taskList = new ArrayList<>();
        this.testTask = new Task(name);
        this.testTaskWithId = new Task(testTask.getName());
        this.testTaskWithId.setId(id);
        this.taskList.add(testTaskWithId);
        this.taskDTO = this.mapToDTO(testTaskWithId);
    }

    @Test
    void createTest() {
        // set up what the mock is doing
        // when running some method, return some value we've predefined up there ^
        when(this.service.create(testTask)).thenReturn(this.taskDTO);

        // these are the same thing:
        // JUNIT: assertEquals(expected, actual)
        // MOCKITO: assertThat(expected).isEqualTo(actual);
        // .isEqualTo(what is the method actually returning?)
        // assertThat(what do we want to compare the method to?)
        TaskDTO testCreated = this.taskDTO;
        assertThat(new ResponseEntity<TaskDTO>(testCreated, HttpStatus.CREATED))
                .isEqualTo(this.controller.create(testTask));

        // check that the mocked method we ran our assertion on ... actually ran!
        verify(this.service, times(1)).create(this.testTask);
    }

    @Test
    void readTest() {
        when(this.service.read(this.id)).thenReturn(this.taskDTO);

        TaskDTO testReadOne = this.taskDTO;
        assertThat(new ResponseEntity<TaskDTO>(testReadOne, HttpStatus.OK))
                .isEqualTo(this.controller.read(this.id));

        verify(this.service, times(1)).read(this.id);
    }

    // controller <- service
    @Test
    void readAllTest() {
        when(this.service.read())
                .thenReturn(this.taskList.stream().map(this::mapToDTO).collect(Collectors.toList()));

        // getBody() = get the list returned from the controller.read() method
        // isEmpty()).isFalse() - check that that list HAS SOMETHING IN IT
        // we can reason that if the list has something in it, it has a guitarist
        assertThat(this.controller.read().getBody().isEmpty()).isFalse();

        verify(this.service, times(1)).read();
    }

    // controller <- service
    @Test
    void updateTest() {
        // we need to feed the mocked service some updated data values
        // that way we can test if our 6-string guitarist changes to a 4-string
        // 'guitarist'
    	Task newTask = new Task("clean");
		TaskDTO newDTOId = new TaskDTO(this.id, "wash", null);
		
		// feed to mock
		when(this.service.update(newTask, this.id))
		.thenReturn(newDTOId);
		
		// Test assertion - returned object matches expected
		assertThat(new ResponseEntity<TaskDTO>(newDTOId, HttpStatus.ACCEPTED))
		.isEqualTo(this.controller.update(this.id, newTask));
		
		// Check service called
    }

    // controller -> service
    @Test
    void deleteTest() {
        this.controller.delete(id); // this will ping the service, which is mocked!

        // if the delete function ran, then it pinged the service successfully
        // since our service is a mocked one, we don't need to test anything in it
        // therefore: check if the controller delete function runs
        // if it does, then the test passes
        verify(this.service, times(1)).delete(id);
    }

}