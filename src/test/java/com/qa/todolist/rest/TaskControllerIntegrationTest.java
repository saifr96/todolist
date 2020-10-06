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
import com.qa.todolist.dto.TaskListDTO;
import com.qa.todolist.persistence.domain.Task;
import com.qa.todolist.persistence.repository.TaskRepository;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mock;

    @Autowired
    private TaskRepository repo;
    
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Long id;
    private Task testTask;
    private Task testTaskWithId;
    private TaskDTO dto;
    private List<TaskListDTO> tasklist;
    
    private TaskDTO mapToDTO(Task task) {
        return this.modelMapper.map(task, TaskDTO.class);
    }

    @BeforeEach
    void init() {
        this.repo.deleteAll();
        this.testTask = new Task("clean");
        this.testTaskWithId = this.repo.save(this.testTask);
        this.dto = this.mapToDTO(testTask);
        this.id = this.testTaskWithId.getId();
        this.tasklist = new ArrayList<>();
    }

    @Test
    void testCreate() throws Exception {
        this.mock
            .perform(request(HttpMethod.POST, "/task/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(testTask))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(content().json(this.objectMapper.writeValueAsString(this.dto)));
    }

    @Test
    void testRead() throws Exception {
        this.mock.perform
            (request(HttpMethod.GET, "/task/read/" + this.id)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(this.objectMapper.writeValueAsString(this.dto)));
    }

    @Test
    void testReadAll() throws Exception {
        List<TaskDTO> taskList = new ArrayList<>();
        taskList.add(this.dto);
        
        this.objectMapper.writeValueAsString(taskList);
        String content = this.mock
            .perform(request(HttpMethod.GET, "/task/read")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        
        assertEquals(this.objectMapper.writeValueAsString(taskList), content);
    }

    @Test
	 void testUpdate() throws Exception {
    	TaskDTO newTask = new TaskDTO(null, "take the car to the carwash", null);
    	Task updatedTask = new Task(newTask.getName());
        updatedTask.setId(this.id);
        String expected = this.objectMapper.writeValueAsString(this.mapToDTO(updatedTask));
        String actual = this.mock.perform(request(HttpMethod.PUT, "/task/update/" + this.id) 
                .contentType(MediaType.APPLICATION_JSON).content(this.objectMapper.writeValueAsString(newTask))
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isAccepted()) // 201
                .andReturn().getResponse().getContentAsString();

        assertEquals(expected, actual);
    
    
    }
    
    @Test
    void testDelete() throws Exception {
        this.mock
            .perform(request(HttpMethod.DELETE, "/task/delete/" + this.id))
            .andExpect(status().isNoContent());
    }

}
