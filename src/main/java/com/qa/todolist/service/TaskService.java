package com.qa.todolist.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.qa.todolist.dto.TaskDTO;
import com.qa.todolist.exception.TaskNotFoundException;
import com.qa.todolist.persistence.domain.Task;
import com.qa.todolist.persistence.repository.TaskRepository;
import com.qa.todolist.utils.todolistBeanUtils;


@Service
public class TaskService {

    private TaskRepository repo;

    private ModelMapper mapper;

    @Autowired
    public TaskService(TaskRepository repo, ModelMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    private TaskDTO mapToDTO(Task task) {
        return this.mapper.map(task, TaskDTO.class);
    }

//    private Task mapFromDTO(TaskDTO taskDTO) {
//       return this.mapper.map(taskDTO, Task.class);
//    }

//    public BandDTO create(BandDTO bandDTO) {
//        Band toSave = this.mapFromDTO(bandDTO);
//        Band saved = this.repo.save(toSave);
//        return this.mapToDTO(saved);
//    }

    public TaskDTO create(Task task) {
        Task created = this.repo.save(task);
        TaskDTO mapped = this.mapToDTO(created);
        return mapped;
    }

    public List<TaskDTO> read() {
        List<Task> found = this.repo.findAll();
        List<TaskDTO> streamed = found.stream().map(this::mapToDTO).collect(Collectors.toList());
        return streamed;
    }

    public TaskDTO read(Long id) {
        Task found = this.repo.findById(id).orElseThrow(TaskNotFoundException::new);
        return this.mapToDTO(found);
    }

 // Update
 //	public BandDTO update(Band band, Long id) {
 //		Band toUpdate = this.repo.findById(id).orElseThrow(BandNotFoundException::new);
 //		toUpdate.setName(band.getName());
 //		toUpdate.setGuitarists(band.getGuitarists());
 //		return this.mapToDTO(this.repo.save(toUpdate));
 //   }
    
    public TaskDTO update(Task task, Long id) {
		Task toUpdate = this.repo.findById(id).orElseThrow(TaskNotFoundException::new);
		toUpdate.setName(task.getName());
		toUpdate.setTasklist(task.getTasklist());
		return this.mapToDTO(this.repo.save(toUpdate));
    }
    

    public boolean delete(Long id) {
        if (!this.repo.existsById(id)) {
            throw new TaskNotFoundException();
        }
        this.repo.deleteById(id);
        return !this.repo.existsById(id);
    }



	}


