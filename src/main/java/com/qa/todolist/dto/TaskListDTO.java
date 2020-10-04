package com.qa.todolist.dto;

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
@EqualsAndHashCode
public class TaskListDTO {

    // D - Data
    // T - Transfer
    // O - Object

    private Long id;
    private String name;
    private Long task_id;
   // private Integer strings;
   // private String type;

}
