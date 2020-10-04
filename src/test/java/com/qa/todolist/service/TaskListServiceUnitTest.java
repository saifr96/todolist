package com.qa.todolist.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.qa.todolist.dto.TaskListDTO;
import com.qa.todolist.persistence.domain.TaskList;
import com.qa.todolist.persistence.repository.TaskListRepository;

@SpringBootTest
class TaskListServiceUnitTest {

    @Autowired
    private TaskListService service;

    @MockBean
    private TaskListRepository repo;

    @MockBean
    private ModelMapper modelMapper;

    private List<TaskList> tasklistList;
    private TaskList testTaskList;
    private TaskList testTaskListWithId;
    private TaskList emptyTaskList;
    private TaskListDTO tasklistDTO;

    final Long id = 1L;
    final String testName = "clean the car";
  

    private TaskListDTO mapToDTO(TaskList tasklist) {
        return this.modelMapper.map(tasklist, TaskListDTO.class);
    }

    @BeforeEach
    void init() {
        this.tasklistList = new ArrayList<>();
        this.tasklistList.add(testTaskList);
        this.testTaskList = new TaskList("clean the car");
        this.testTaskListWithId = new TaskList(testTaskList.getName());
        this.emptyTaskList = new TaskList();
        this.testTaskListWithId.setId(id);
        this.tasklistDTO = new ModelMapper().map(testTaskListWithId, TaskListDTO.class);
    }

    @Test
    void createTest() {
        when(this.modelMapper.map(mapToDTO(testTaskList), TaskList.class)).thenReturn(testTaskList);

        when(this.repo.save(testTaskList)).thenReturn(testTaskListWithId);

        when(this.modelMapper.map(testTaskListWithId, TaskListDTO.class)).thenReturn(tasklistDTO);

        assertThat(this.tasklistDTO).isEqualTo(this.service.create(testTaskList));

        verify(this.repo, times(1)).save(this.testTaskList);
    }

    @Test
    void readTest() {
        when(this.repo.findById(this.id)).thenReturn(Optional.of(this.testTaskListWithId));

        when(this.modelMapper.map(testTaskListWithId, TaskListDTO.class)).thenReturn(tasklistDTO);

        assertThat(this.tasklistDTO).isEqualTo(this.service.read(this.id));

        verify(this.repo, times(1)).findById(this.id);
    }

    @Test
    void readAllTest() {
        when(repo.findAll()).thenReturn(this.tasklistList);

        when(this.modelMapper.map(testTaskListWithId, TaskListDTO.class)).thenReturn(tasklistDTO);

        assertThat(this.service.readAll().isEmpty()).isFalse();

        verify(repo, times(1)).findAll();
    }

    @Test
    void updateTest() {
        final long ID = 1L;

        TaskListDTO newTaskList = new TaskListDTO(null, "Peter Peter Hughes", null);

        TaskList tasklist = new TaskList("Fender American");
        tasklist.setId(ID);

        TaskList updatedTaskList = new TaskList(newTaskList.getName());
        updatedTaskList.setId(ID);

        TaskListDTO updatedDTO = new TaskListDTO(ID, updatedTaskList.getName(),null);

        when(this.repo.findById(this.id)).thenReturn(Optional.of(tasklist));

        when(this.repo.save(updatedTaskList)).thenReturn(updatedTaskList);

        when(this.modelMapper.map(updatedTaskList, TaskListDTO.class)).thenReturn(updatedDTO);

        assertThat(updatedDTO).isEqualTo(this.service.update(newTaskList, this.id));

        verify(this.repo, times(1)).findById(1L);

        verify(this.repo, times(1)).save(updatedTaskList);
    }

    @Test
    void deleteTest() {
        when(this.repo.existsById(id)).thenReturn(true, false);

        assertThat(this.service.delete(id)).isTrue();

        verify(this.repo, times(1)).deleteById(id);

        verify(this.repo, times(2)).existsById(id);
    }

}