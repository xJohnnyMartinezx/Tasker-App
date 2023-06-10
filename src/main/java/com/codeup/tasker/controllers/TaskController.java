package com.codeup.tasker.controllers;

import com.codeup.tasker.models.Task;
import com.codeup.tasker.models.User;
import com.codeup.tasker.repos.TasksRepo;
import com.codeup.tasker.repos.UserRepo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/task/create")
    public String createTaskForm(Model model){
        model.addAttribute("task", new Task());
        return "/tasks/task-create-form";
    }

    @PostMapping("/task/create")
    public String createTaskSubmit(@ModelAttribute Task task){
        User user = userDao.findById(1L).get();
        task.setUser(user);
        taskDao.save(task);

        return "redirect:/tasks";
    }




}
