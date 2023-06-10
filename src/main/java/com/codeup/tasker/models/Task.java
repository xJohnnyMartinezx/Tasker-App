package com.codeup.tasker.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 60, nullable = false)
    private String taskTitle;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String taskDescription;

    @ManyToOne
    @JoinColumn (name = "user_id")
    private User user;


}
