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
import com.qa.todolist.dto.TaskListDTO;
import com.qa.todolist.persistence.domain.Task;
import com.qa.todolist.service.TaskService;

@SpringBootTest
class TaskControllerUnitTest {

    @Autowired
    private TaskController controller;

    @MockBean
    private TaskService service;

    @Autowired
    private ModelMapper modelMapper;
    private TaskDTO mapToDTO(Task task) {
        return this.modelMapper.map(task, TaskDTO.class);
    }

    private List<Task> tasklist;
    private Task testTask;
    private Task testTaskWithId;
    private TaskDTO taskDTO;

    private final String name = "Wash";
    private final Long id = 1L;
    

    @BeforeEach
    void init() {
        this.tasklist = new ArrayList<>();
        this.testTask = new Task(name);
        this.testTaskWithId = new Task(testTask.getName());
        this.testTaskWithId.setId(id);
        this.tasklist.add(testTaskWithId);
        this.taskDTO = this.mapToDTO(testTaskWithId);
    }

    @Test
    void createTest() {
        when(this.service.create(testTask)).thenReturn(this.taskDTO);
        TaskDTO testCreated = this.taskDTO;
        assertThat(new ResponseEntity<TaskDTO>(testCreated, HttpStatus.CREATED))
                .isEqualTo(this.controller.create(testTask));
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

    @Test
    void readAllTest() {
        when(this.service.read())
                .thenReturn(this.tasklist.stream().map(this::mapToDTO).collect(Collectors.toList()));
        assertThat(this.controller.read().getBody().isEmpty()).isFalse();
        verify(this.service, times(1)).read();
    }

    @Test
    void updateTest() {
    	   TaskDTO newTask = new TaskDTO(null, "Clean", new ArrayList<>());
    	   TaskDTO newTaskWithId = new TaskDTO(this.id, newTask.getName(), new ArrayList<>());
           when(this.service.update(newTask, this.id)).thenReturn(newTaskWithId);
           assertThat(new ResponseEntity<TaskDTO>(newTaskWithId, HttpStatus.ACCEPTED))
                   .isEqualTo(this.controller.update(this.id, newTask));
           verify(this.service, times(1)).update(newTask, this.id);
    }

    @Test
    void deleteTest() {
        this.controller.delete(id); 
        verify(this.service, times(1)).delete(id);
    }
}
