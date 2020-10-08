package com.qa.todolist.integrationtest.rest;

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

import com.qa.todolist.dto.TaskListDTO;
import com.qa.todolist.persistence.domain.TaskList;
import com.qa.todolist.rest.TaskListController;
import com.qa.todolist.service.TaskListService;

@SpringBootTest
class TaskListControllerUnitTest {

    @Autowired
    private TaskListController controller;

    @MockBean
    private TaskListService service;

    @Autowired
    private ModelMapper modelMapper;

    private TaskListDTO mapToDTO(TaskList tasklist) {
        return this.modelMapper.map(tasklist, TaskListDTO.class);
    }

    private List<TaskList> tasklistList;
    private TaskList testTaskList;
    private TaskList testTaskListWithId;
    private TaskListDTO tasklistDTO;
    
    private final String name = "Wash the car";
    private final Long id = 1L;
    private final Long idTask = 1L;
    
    
    @BeforeEach
    void init() {
        this.tasklistList = new ArrayList<>();
        this.testTaskList = new TaskList(name, idTask);
        this.testTaskListWithId = new TaskList(testTaskList.getName(), idTask);
        this.testTaskListWithId.setId(id);
        this.tasklistList.add(testTaskListWithId);
        this.tasklistDTO = this.mapToDTO(testTaskListWithId);
    }

    @Test
    void createTest() {
        when(this.service.create(testTaskList)).thenReturn(this.tasklistDTO);
        TaskListDTO testCreated = this.tasklistDTO;
        assertThat(new ResponseEntity<TaskListDTO>(testCreated, HttpStatus.CREATED))
                .isEqualTo(this.controller.create(testTaskList));
        verify(this.service, times(1)).create(this.testTaskList);
    }

    @Test
    void readTest() {
        when(this.service.read(this.id)).thenReturn(this.tasklistDTO);
        TaskListDTO testReadOne = this.tasklistDTO;
        assertThat(new ResponseEntity<TaskListDTO>(testReadOne, HttpStatus.OK))
                .isEqualTo(this.controller.read(this.id));
        verify(this.service, times(1)).read(this.id);
    }

    @Test
    void readAllTest() {
        when(this.service.readAll())
                .thenReturn(this.tasklistList.stream().map(this::mapToDTO).collect(Collectors.toList()));
        assertThat(this.controller.readAll().getBody().isEmpty()).isFalse();
        verify(this.service, times(1)).readAll();
    }

    @Test
    void updateTest() {
    	TaskListDTO newTaskList = new TaskListDTO(null, "take the car to the carwash",idTask);
    	TaskListDTO newTaskListWithId = new TaskListDTO(this.id, newTaskList.getName(), newTaskList.getIdTask());
        when(this.service.update(newTaskList, this.id)).thenReturn(newTaskListWithId);
        assertThat(new ResponseEntity<TaskListDTO>(newTaskListWithId, HttpStatus.ACCEPTED))
                .isEqualTo(this.controller.update(this.id, newTaskList));
        verify(this.service, times(1)).update(newTaskList, this.id);
    }

    @Test
    void deleteTest() {
        this.controller.delete(id); 
        verify(this.service, times(1)).delete(id);
    }

}