package com.qa.todolist.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qa.todolist.persistence.domain.TaskList;

@Repository
public interface TaskListRepository extends JpaRepository<TaskList, Long> {

    // J - Java
    // P - Persistence
    // A - Application Programming Interface

//    List<Guitarist> findByName(String name);
//
//    List<Guitarist> findByStrings(Integer strings);
//
//    List<Guitarist> findByType(String type);

}
