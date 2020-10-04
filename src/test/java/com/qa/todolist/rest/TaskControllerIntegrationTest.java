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
import com.qa.todolist.dto.TaskDTO;
import com.qa.todolist.persistence.domain.Task;
import com.qa.todolist.persistence.repository.TaskRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerIntegrationTest {

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
    private TaskRepository repo;

    // this specifically maps POJOs for us, in our case to JSON
    // slightly different from ObjectMapper because we built it ourselves (and use
    // it exclusively on our <expected> objects
    @Autowired
    private ModelMapper modelMapper;

    // this specifically maps objects to JSON format for us
    // slightly different from ModelMapper because this is bundled with mockito
    @Autowired
    private ObjectMapper objectMapper;

    private Task testTask;
    private Task testTaskWithId;
    private TaskDTO taskDTO;

    private Long id = 1L;
    private String testName;
    
  

    private TaskDTO mapToDTO(Task task) {
        return this.modelMapper.map(task, TaskDTO.class);
    }

    @BeforeEach
    void init() {
        this.repo.deleteAll();

        this.testTask = new Task("wash the car");
        this.testTaskWithId = this.repo.save(this.testTask);
        this.taskDTO = this.mapToDTO(testTaskWithId);
        
        this.id = this.testTaskWithId.getId();
        this.testName = this.testTaskWithId.getName();
       
    }

    @Test
    void testCreate() throws Exception {
        this.mock
                .perform(request(HttpMethod.POST, "/task/create").contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(this.testTask))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(this.objectMapper.writeValueAsString(this.taskDTO)));
    }

    @Test
    void testRead() throws Exception {
        this.mock.perform(request(HttpMethod.GET, "/task/read/" + this.id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(this.objectMapper.writeValueAsString(this.taskDTO)));
    }

    @Test
    void testReadAll() throws Exception {
        List<TaskDTO> taskList = new ArrayList<>();
        taskList.add(this.taskDTO);
        String expected = this.objectMapper.writeValueAsString(taskList);
        // expected = { { "name": "Nick", ... } , { "name": "Cris", ... } }

        String actual = this.mock.perform(request(HttpMethod.GET, "/task/read").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    @Test
	void testUpdate() throws Exception {
		// Create objects /w values to test (since using actual, not mock)
		TaskDTO newDTO = new TaskDTO();
		Task updatedTask = new Task();
				newDTO.getName();
		updatedTask.setId(this.id);
		
		// Stringify for comparison
		String expected = this.objectMapper.writeValueAsString(
				this.mapToDTO(updatedTask));
		
		String actual = this.mock.perform(request(HttpMethod.PUT, "/task/update/" + this.id)
				.contentType(MediaType.APPLICATION_JSON).content(
						this.objectMapper.writeValueAsString(newDTO))
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isAccepted())
				.andReturn().getResponse().getContentAsString();
		
		// Test assertion
		assertEquals(expected, actual);			
  }

    @Test
    void testDelete() throws Exception {
        this.mock.perform(request(HttpMethod.DELETE, "/task/delete/" + this.id)).andExpect(status().isNoContent());
    }

}
