package com.qa.todolist.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import com.qa.todolist.dto.TaskDTO;
import com.qa.todolist.persistence.domain.Task;
import com.qa.todolist.persistence.repository.TaskRepository;

@SpringBootTest
public class TaskServiceUnitTest {
	
	@Autowired
	private TaskService service;
	
	@MockBean
	private TaskRepository repo;
	
	@MockBean
	   private ModelMapper modelMapper;
	   private List<Task> taskList;
	   private Task testTask;
	   private Task testTaskWithId;
	   private TaskDTO taskDTO;
	
	final Long id = 1L;
	final String exampleName = "clean";
	
	private TaskDTO mapToDTO(Task task) {
		return this.modelMapper.map(task, TaskDTO.class);
	}
	

	@BeforeEach
	void init() {	
		  this.taskList = new ArrayList<>();
	      this.taskList.add(testTask);
	      this.testTask = new Task("clean the car");
	      this.testTaskWithId = new Task(testTask.getName());
	      new Task();
	      this.testTaskWithId.setId(id);
	      this.taskDTO = new ModelMapper().map(testTaskWithId, TaskDTO.class);
	}
	
	@Test
	void createTest() {
		when(this.modelMapper.map(mapToDTO(testTask), Task.class)).thenReturn(testTask);
		when(this.repo.save(testTask)).thenReturn(testTaskWithId);
		when(this.modelMapper.map(testTaskWithId, TaskDTO.class)).thenReturn(taskDTO);
		assertThat(this.taskDTO).isEqualTo(this.service.create(testTask));
		verify(this.repo, times(1)).save(this.testTask);
		
	}
	
	 @Test
	    void readTest() {
	        when(this.repo.findById(this.id)).thenReturn(Optional.of(this.testTaskWithId));
	        when(this.modelMapper.map(testTaskWithId, TaskDTO.class)).thenReturn(taskDTO);
	        assertThat(this.taskDTO).isEqualTo(this.service.read(this.id));
	        verify(this.repo, times(1)).findById(this.id);
	}
	
	   @Test
	    void readAllTest() {
	        when(repo.findAll()).thenReturn(this.taskList);
	        when(this.modelMapper.map(testTaskWithId, TaskDTO.class)).thenReturn(taskDTO);
	        assertThat(this.service.read().isEmpty()).isFalse();
	        verify(repo, times(1)).findAll();
	   }
	   
	   @Test
	   void updateTest() {
	        final long ID = 1L;
	        TaskDTO newTask = new TaskDTO( null, "wash the car",  new ArrayList<>());
	        Task task = new Task("wash the car");
	        task.setId(ID);
	        Task updatedTask = new Task(newTask.getName());
	        updatedTask.setId(ID);
	        TaskDTO updatedDTO = new TaskDTO(ID, updatedTask.getName(),  new ArrayList<>());
	        when(this.repo.findById(this.id)).thenReturn(Optional.of(task));
	        when(this.repo.save(updatedTask)).thenReturn(updatedTask);
	        when(this.modelMapper.map(updatedTask, TaskDTO.class)).thenReturn(updatedDTO);
	        assertThat(updatedDTO).isEqualTo(this.service.update(newTask, this.id));
	        verify(this.repo, times(1)).findById(1L);
	        verify(this.repo, times(1)).save(updatedTask);
	   }
	   

	   @Test
	    void deleteTest() {
	        when(this.repo.existsById(id)).thenReturn(true, false);
	        assertThat(this.service.delete(id)).isTrue();
	        verify(this.repo, times(1)).deleteById(id);
	        verify(this.repo, times(2)).existsById(id);
	    }
	
	
}