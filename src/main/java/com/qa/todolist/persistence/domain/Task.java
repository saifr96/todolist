package com.qa.todolist.persistence.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
//import lombok.ToString;

@Entity
@NoArgsConstructor
@Getter
@Setter
//@ToString
@EqualsAndHashCode

public class Task {

	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<TaskList> tasklist = new ArrayList<>();

    public Task(String name) {
        this.name = name;
    }

}
