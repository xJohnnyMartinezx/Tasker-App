package com.codeup.tasker.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tasks")
@DynamicUpdate
@DynamicInsert
@EntityListeners(AuditingEntityListener.class)
public class Task extends AuditableBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 60, nullable = false)
    private String taskTitle;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String taskDescription;

    @Column
    boolean isComplete;

    @ManyToOne
    @JoinColumn (name = "user_id")
    private User user;



}
