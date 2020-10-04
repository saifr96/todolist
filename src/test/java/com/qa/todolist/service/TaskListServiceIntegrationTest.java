package com.qa.todolist.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.qa.todolist.dto.TaskListDTO;
import com.qa.todolist.persistence.domain.TaskList;
import com.qa.todolist.persistence.repository.TaskListRepository;

@SpringBootTest
class TaskListServiceIntegrationTest {

    @Autowired
    private TaskListService service;

    @Autowired
    private TaskListRepository repo;

    private TaskList testTaskList;
    private TaskList testTaskListWithId;

    @Autowired
    private ModelMapper modelMapper;

    private TaskListDTO mapToDTO(TaskList tasklist) {
        return this.modelMapper.map(tasklist, TaskListDTO.class);
    }

    @BeforeEach
    void init() {
        this.repo.deleteAll();
        this.testTaskList = new TaskList("wash the car");
        this.testTaskListWithId = this.repo.save(this.testTaskList);
    }

    @Test
    void testCreate() {
        assertThat(this.mapToDTO(this.testTaskListWithId))
            .isEqualTo(this.service.create(testTaskList));
    }

    @Test
    void testRead() {
        assertThat(this.service.read(this.testTaskListWithId.getId()))
            .isEqualTo(this.mapToDTO(this.testTaskListWithId));
    }

    @Test
    void testReadAll() {
        assertThat(this.service.readAll())
            .isEqualTo(Stream.of(this.mapToDTO(testTaskListWithId)).collect(Collectors.toList()));
    }

    @Test
    void testUpdate() {
    	TaskListDTO newTaskList = new TaskListDTO(null, "take the car to the carwash", null);
    	TaskListDTO updatedTaskList = new TaskListDTO(this.testTaskListWithId.getId(), newTaskList.getName(), newTaskList.getTask_id());

        assertThat(this.service.update(newTaskList, this.testTaskListWithId.getId()))
            .isEqualTo(updatedTaskList);
    }

    @Test
    void testDelete() {
        assertThat(this.service.delete(this.testTaskListWithId.getId()))
            .isTrue();
    }

}
