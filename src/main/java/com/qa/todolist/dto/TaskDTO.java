package com.qa.todolist.dto;

import java.util.ArrayList;
import java.util.List;

import com.qa.todolist.dto.TaskListDTO;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
//@ToString
//@EqualsAndHashCode
public class TaskDTO {

    private Long id;
    private String name;
    private List<TaskListDTO> tasklist = new ArrayList<>();
     

}
