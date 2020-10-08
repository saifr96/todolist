package com.qa.todolist.persistence.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


//import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@NoArgsConstructor
@Getter
@Setter
//@EqualsAndHashCode
public class TaskList {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   
    private String name;

    private Long idTask;
  
    @ManyToOne
    private Task task;

    public TaskList(String name, Long idTask) {
        super();
        this.name = name;
        this.idTask = idTask;
     
        
     
    }

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaskList other = (TaskList) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (idTask == null) {
			if (other.idTask != null)
				return false;
		} else if (!idTask.equals(other.idTask))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (task == null) {
			if (other.task != null)
				return false;
		} else if (!task.equals(other.task))
			return false;
		return true;
	}

	//@Override
	//public int hashCode() {
	//	final int prime = 31;
	//	int result = 1;
	//	result = prime * result + ((id == null) ? 0 : id.hashCode());
	//	result = prime * result + ((idTask == null) ? 0 : idTask.hashCode());
	//	result = prime * result + ((name == null) ? 0 : name.hashCode());
	//	result = prime * result + ((task == null) ? 0 : task.hashCode());
	//	return result;
	//}

}
