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

    @Autowired
    private MockMvc mock;

    @Autowired
    private TaskListRepository repo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private TaskList testTaskList;
    private TaskList testTaskListWithId;
    private TaskListDTO tasklistDTO;

    private Long id = 1L;
    private String testName;
    private Long idTask = 1L;
  
    private TaskListDTO mapToDTO(TaskList tasklist) {
        return this.modelMapper.map(tasklist, TaskListDTO.class);
    }

    @BeforeEach
    void init() {
        this.repo.deleteAll();
        this.testTaskList = new TaskList("wash the car", idTask);
        this.testTaskListWithId = this.repo.save(this.testTaskList);
        this.tasklistDTO = this.mapToDTO(testTaskListWithId);  
        this.id = this.testTaskListWithId.getId();
        this.testName = this.testTaskListWithId.getName();
        this.idTask = this.testTaskListWithId.getIdTask();
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
    	        String actual = this.mock.perform(request(HttpMethod.GET, "/tasklist/read").accept(MediaType.APPLICATION_JSON))
    	                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    	        assertEquals(expected, actual);
    }

    @Test
    void testUpdate() throws Exception {
    	TaskListDTO newTaskList = new TaskListDTO(null, "take the car to the carwash",idTask);
    	TaskList updatedTaskList = new TaskList(newTaskList.getName(), idTask);
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
