package com.codeup.tasker.repos;

import com.codeup.tasker.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TasksRepo extends JpaRepository<Task, Long> {

}
