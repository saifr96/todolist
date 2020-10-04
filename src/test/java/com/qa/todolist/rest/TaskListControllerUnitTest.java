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

import com.qa.todolist.dto.TaskListDTO;
import com.qa.todolist.persistence.domain.TaskList;
import com.qa.todolist.service.TaskListService;

@SpringBootTest
class TaskListControllerUnitTest {

    // the thing we're actually testing
    // (this is the real thing we've made)
    @Autowired
    private TaskListController controller;

    // the mock thing that we're connecting to
    // so that any requests we receive are always valid
    @MockBean
    private TaskListService service;

    // we need the mapper, because it works with the mock service layer
    @Autowired
    private ModelMapper modelMapper;

    // and we need the dto mapping as well, otherwise we can't test
    // our controller methods (which rely on RE<xDTO>)
    private TaskListDTO mapToDTO(TaskList tasklist) {
        return this.modelMapper.map(tasklist, TaskListDTO.class);
    }

    private List<TaskList> tasklistList;
    private TaskList testTaskList;
    private TaskList testTaskListWithId;
    private TaskListDTO tasklistDTO;

    private final String name = "Wash the car";
    private final Long id = 1L;
    private final Long task_id = 1L;
    
    
    @BeforeEach
    void init() {
        this.tasklistList = new ArrayList<>();
        this.testTaskList = new TaskList(name);
        this.testTaskListWithId = new TaskList(testTaskList.getName());
        this.testTaskListWithId.setId(id);
        this.tasklistList.add(testTaskListWithId);
        this.tasklistDTO = this.mapToDTO(testTaskListWithId);
    }

    @Test
    void createTest() {
        // set up what the mock is doing
        // when running some method, return some value we've predefined up there ^
        when(this.service.create(testTaskList)).thenReturn(this.tasklistDTO);

        // these are the same thing:
        // JUNIT: assertEquals(expected, actual)
        // MOCKITO: assertThat(expected).isEqualTo(actual);
        // .isEqualTo(what is the method actually returning?)
        // assertThat(what do we want to compare the method to?)
        TaskListDTO testCreated = this.tasklistDTO;
        assertThat(new ResponseEntity<TaskListDTO>(testCreated, HttpStatus.CREATED))
                .isEqualTo(this.controller.create(testTaskList));

        // check that the mocked method we ran our assertion on ... actually ran!
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

    // controller <- service
    @Test
    void readAllTest() {
        when(this.service.readAll())
                .thenReturn(this.tasklistList.stream().map(this::mapToDTO).collect(Collectors.toList()));

        // getBody() = get the list returned from the controller.read() method
        // isEmpty()).isFalse() - check that that list HAS SOMETHING IN IT
        // we can reason that if the list has something in it, it has a guitarist
        assertThat(this.controller.read().getBody().isEmpty()).isFalse();

        verify(this.service, times(1)).readAll();
    }

    // controller <- service
    @Test
    void updateTest() {
        // we need to feed the mocked service some updated data values
        // that way we can test if our 6-string guitarist changes to a 4-string
        // 'guitarist'
    	TaskListDTO newTaskList = new TaskListDTO(null, "take the car to the carwash",task_id);
    	TaskListDTO newTaskListWithId = new TaskListDTO(this.id, newTaskList.getName(), newTaskList.getTask_id());

        // feed the mock service the values we made up here ^
        when(this.service.update(newTaskList, this.id)).thenReturn(newTaskListWithId);

        assertThat(new ResponseEntity<TaskListDTO>(newTaskListWithId, HttpStatus.ACCEPTED))
                .isEqualTo(this.controller.update(this.id, newTaskList));

        verify(this.service, times(1)).update(newTaskList, this.id);
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