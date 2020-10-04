package com.qa.todolist.persistence.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Task {

	@Id
	@GeneratedValue
	private long id;
	
	@Column(name = "task_name", unique = true)
	private String name;
	
	//@OneToMany(targetEntity = Guitarist.class, cascade = CascadeType.ALL)
	@OneToMany(mappedBy = "task")
	private List<TaskList> tasklist = new ArrayList<>();

	public Task(String name) {
		this.name = name;
		this.tasklist = new ArrayList<>();
	}
}