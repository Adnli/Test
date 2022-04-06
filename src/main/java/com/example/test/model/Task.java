package com.example.test.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "tasks")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
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