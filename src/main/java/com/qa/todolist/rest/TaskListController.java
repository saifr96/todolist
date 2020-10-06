package com.qa.todolist.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.qa.todolist.dto.TaskListDTO;
import com.qa.todolist.persistence.domain.TaskList;
import com.qa.todolist.service.TaskListService;

@CrossOrigin
@RestController
@RequestMapping("/tasklist")
public class TaskListController {


    private TaskListService service;

    @Autowired
    public TaskListController(TaskListService service) {
        super();
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<TaskListDTO> create(@RequestBody TaskList tasklist) {
        TaskListDTO created = this.service.create(tasklist);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/read")
    public ResponseEntity<List<TaskListDTO>> read() {
        return ResponseEntity.ok(this.service.readAll());
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<TaskListDTO> read(@PathVariable Long id) {
        return ResponseEntity.ok(this.service.read(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<TaskListDTO> update(@PathVariable Long id, @RequestBody TaskListDTO tasklistDTO) {
        return new ResponseEntity<>(this.service.update(tasklistDTO, id), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<TaskListDTO> delete(@PathVariable Long id) {

        return this.service.delete(id) ? new ResponseEntity<>(HttpStatus.NO_CONTENT) 
                : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); 
    }

}
