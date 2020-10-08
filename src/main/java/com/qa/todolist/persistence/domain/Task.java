package com.qa.todolist.persistence.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

//import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@NoArgsConstructor
@Getter
@Setter

//@EqualsAndHashCode

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

	//@Override
	//public int hashCode() {
	//	final int prime = 31;
	//	int result = 1;
	//	result = prime * result + ((id == null) ? 0 : id.hashCode());
	//	result = prime * result + ((name == null) ? 0 : name.hashCode());
	//	result = prime * result + ((tasklist == null) ? 0 : tasklist.hashCode());
	//	return result;
	//}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Task other = (Task) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (tasklist == null) {
			if (other.tasklist != null)
				return false;
		} else if (!tasklist.equals(other.tasklist))
			return false;
		return true;
	}

}
