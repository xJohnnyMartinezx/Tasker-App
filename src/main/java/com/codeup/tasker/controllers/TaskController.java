package com.codeup.tasker.controllers;

import com.codeup.tasker.models.Task;
import com.codeup.tasker.models.User;
import com.codeup.tasker.repos.TasksRepo;
import com.codeup.tasker.repos.UserRepo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class TaskController {

    private final TasksRepo taskDao;

    private final UserRepo userDao;


    public TaskController(TasksRepo taskDao, UserRepo userDao) {
        this.taskDao = taskDao;
        this.userDao = userDao;
    }


    @GetMapping("/tasks")
    public String viewAllTasks(Model model){
        List<Task> tasks = taskDao.findAll();
        model.addAttribute("taskList", tasks);
        return "/tasks/index";
    }

    @GetMapping("/tasks/{id}")
    public String viewIndividualTask(@PathVariable Long id, Model model){
        model.addAttribute("individualTask", taskDao.findById(id).get());
        return "/tasks/view-task-by-id";
    }

    @GetMapping("/tasks/create")
    public String createTaskForm(Model model){
        model.addAttribute("task", new Task());
        return "/tasks/task-create-form";
    }

    @PostMapping("/tasks/create")
    public String createTaskSubmit(@ModelAttribute Task task){
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        task.setUser(loggedInUser);
        taskDao.save(task);

        return "redirect:/tasks";
    }

    @GetMapping("/tasks/{id}/edit")
    public String editTaskForm(@PathVariable Long id, Model model){
        model.addAttribute("editTask", taskDao.findById(id).get());
        return "tasks/tasks-edit-form";
    }

    @PostMapping("/tasks/{id}/edit")
    public String editTaskSubmit(@ModelAttribute Task task){
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        task.setUser(loggedInUser);
        taskDao.save(task);
        return "redirect: /tasks/{id}";
    }




}
