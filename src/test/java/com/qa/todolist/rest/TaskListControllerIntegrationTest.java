package com.qa.todolist.rest;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.todolist.dto.TaskListDTO;
import com.qa.todolist.persistence.domain.TaskList;
import com.qa.todolist.persistence.repository.TaskListRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskListControllerIntegrationTest {

    // autowiring objects for mocking different aspects of the application
    // here, a mock repo (and relevant mappers) are autowired
    // they'll 'just work', so we don't need to worry about them
    // all we're testing is how our controller integrates with the rest of the API

    // mockito's request-making backend
    // you only need this in integration testing - no mocked service required!
    // this acts as postman would, across your whole application
    @Autowired
    private MockMvc mock;

    // i'm reusing my normal repo to ping different things to for testing purposes
    // this is only used for my <expected> objects, not <actual> ones!
    @Autowired
    private TaskListRepository repo;

    // this specifically maps POJOs for us, in our case to JSON
    // slightly different from ObjectMapper because we built it ourselves (and use
    // it exclusively on our <expected> objects
    @Autowired
    private ModelMapper modelMapper;

    // this specifically maps objects to JSON format for us
    // slightly different from ModelMapper because this is bundled with mockito
    @Autowired
    private ObjectMapper objectMapper;

    private TaskList testTaskList;
    private TaskList testTaskListWithId;
    private TaskListDTO tasklistDTO;

    private Long id = 1L;
    private String testName;
    private Long task_id;
  

    private TaskListDTO mapToDTO(TaskList tasklist) {
        return this.modelMapper.map(tasklist, TaskListDTO.class);
    }

    @BeforeEach
    void init() {
        this.repo.deleteAll();

        this.testTaskList = new TaskList("wash the car");
        this.testTaskListWithId = this.repo.save(this.testTaskList);
        this.tasklistDTO = this.mapToDTO(testTaskListWithId);
        
        this.id = this.testTaskListWithId.getId();
        this.testName = this.testTaskListWithId.getName();
       
    }

    @Test
    void testCreate() throws Exception {
        this.mock
                .perform(request(HttpMethod.POST, "/tasklist/create").contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(this.testTaskList))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(this.objectMapper.writeValueAsString(this.tasklistDTO)));
    }

    @Test
    void testRead() throws Exception {
        this.mock.perform(request(HttpMethod.GET, "/tasklist/read/" + this.id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(this.objectMapper.writeValueAsString(this.tasklistDTO)));
    }

    @Test
    void testReadAll() throws Exception {
    	List<TaskListDTO> tasklistList = new ArrayList<>();
    	        tasklistList.add(this.tasklistDTO);
    	        String expected = this.objectMapper.writeValueAsString(tasklistList);
    	        // expected = { { "name": "Nick", ... } , { "name": "Cris", ... } }

    	        String actual = this.mock.perform(request(HttpMethod.GET, "/tasklist/read").accept(MediaType.APPLICATION_JSON))
    	                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

    	        assertEquals(expected, actual);
    }

    @Test
    void testUpdate() throws Exception {
    	TaskListDTO newTaskList = new TaskListDTO(null, "take the car to the carwash",task_id);
    	TaskList updatedTaskList = new TaskList(newTaskList.getName());
        updatedTaskList.setId(this.id);
        String expected = this.objectMapper.writeValueAsString(this.mapToDTO(updatedTaskList));

        String actual = this.mock.perform(request(HttpMethod.PUT, "/tasklist/update/" + this.id) 
                .contentType(MediaType.APPLICATION_JSON).content(this.objectMapper.writeValueAsString(newTaskList))
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted()) // 201
                .andReturn().getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
    void testDelete() throws Exception {
        this.mock.perform(request(HttpMethod.DELETE, "/tasklist/delete/" + this.id)).andExpect(status().isNoContent());
    }

}
