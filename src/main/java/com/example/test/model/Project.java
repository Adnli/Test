package com.example.test.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "projects")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Task> tasks;
    private Date startDate;
    private Date finishDate;
    private String status;
    private int priority;

    public String getPriority() { //output priority for table
        if (priority == 1) {
            return "HIGH";
        } else if (priority == 2) {
            return "MEDIUM";
        } else if (priority == 3) {
            return "LOW";
        } else return "NO PRIORITY";
    }
}
