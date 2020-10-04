package com.qa.todolist.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qa.todolist.dto.TaskListDTO;
import com.qa.todolist.exception.TaskListNotFoundException;
import com.qa.todolist.persistence.domain.TaskList;
import com.qa.todolist.persistence.repository.TaskListRepository;
import com.qa.todolist.utils.todolistBeanUtils;

@Service
public class TaskListService {

    private TaskListRepository repo;

    private ModelMapper mapper;

    @Autowired
    public TaskListService(TaskListRepository repo, ModelMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    private TaskListDTO mapToDTO(TaskList tasklist) {
        return this.mapper.map(tasklist, TaskListDTO.class);
    }

    private TaskList mapFromDTO(TaskListDTO tasklistDTO) {
        return this.mapper.map(tasklistDTO, TaskList.class);
    }

    // create
//    public GuitaristDTO create(GuitaristDTO guitaristDTO) {
//        Guitarist toSave = this.mapFromDTO(guitaristDTO);
//        Guitarist saved = this.repo.save(toSave);
//        return this.mapToDTO(saved);
//    }

    public TaskListDTO create(TaskList tasklist) {
        TaskList created = this.repo.save(tasklist);
        TaskListDTO mapped = this.mapToDTO(created);
        return mapped;
    }

    // readAll
    public List<TaskListDTO> readAll() {
        List<TaskList> found = this.repo.findAll();
        List<TaskListDTO> streamed = found.stream().map(this::mapToDTO).collect(Collectors.toList());
        return streamed;
    }

    // readById
    public TaskListDTO read(Long id) {
        TaskList found = this.repo.findById(id).orElseThrow(TaskListNotFoundException::new);
        return this.mapToDTO(found);
    }

    // update
    public TaskListDTO update(TaskListDTO tasklistDTO, Long id) {
        TaskList toUpdate = this.repo.findById(id).orElseThrow(TaskListNotFoundException::new);
        todolistBeanUtils.mergeNotNull(tasklistDTO, toUpdate);
        return this.mapToDTO(this.repo.save(toUpdate));
    }

    // delete
    public boolean delete(Long id) {
        if (!this.repo.existsById(id)) {
            throw new TaskListNotFoundException();
        }
        this.repo.deleteById(id);
        return !this.repo.existsById(id);
    }

}
