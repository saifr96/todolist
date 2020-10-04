package com.qa.todolist.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qa.todolist.persistence.domain.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

}
