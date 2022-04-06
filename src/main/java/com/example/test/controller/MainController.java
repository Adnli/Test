package com.example.test.controller;

import com.example.test.model.Project;
import com.example.test.model.Task;
import com.example.test.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;

    @GetMapping(value = "/") //Main page
    public String mainPage(Model model) {
        return "projects";
    }

    @GetMapping(value = "/projects") //Projects page
    public String projectsPage() {
        return "projects";
    }

    @GetMapping(value = "/addprojectpage") //Add project page
    public String addProjectPage() {
        return "addprojectpage";
    }

    @GetMapping(value = "/projectdetails/{id}") //Project details page
    public String projectDetails(Model model, @PathVariable(name = "id") Long id) {
        Project project = mainService.getProject(id); //get project by id
        if(project!=null){
            model.addAttribute("project", project);
            return "projectdetails";
        } else return "NotFoundPage"; //if project doesn't exist

    }

    @GetMapping(value = "/taskdetails/{id}&project={project_id}") //task details page
    public String tasksDetails(Model model, @PathVariable(name = "id") Long id,
                               @PathVariable(name = "project_id") Long projectId) {
        Task task = mainService.getTask(id); //get task by id
        Project project = mainService.getProject(projectId); //get project by id for "BACK" button
        if(task!=null && project!=null){
            model.addAttribute("task", task);
            model.addAttribute("project", project);
            return "taskdetails";
        } else return "NotFoundPage"; //if project or task doesn't exist

    }

    @GetMapping(value = "/addtaskpage/project={project_id}") //add task page
    public String addTaskPage(Model model, @PathVariable(name = "project_id") Long id) {
        Project project = mainService.getProject(id); //get project by id for "BACK" button
        if(project!=null){
            model.addAttribute("project", project);
            return "addtaskpage";
        } else return "NotFoundPage"; //if project doesn't exist
    }

    @PostMapping(value = "/editproject") //edit project
    public String editProject(@RequestParam(name = "project_name") String name,
                              @RequestParam(name = "status") String status,
                              @RequestParam(name = "priority") int priority,
                              @RequestParam(name = "id") Long id) {
        Project project = mainService.getProject(id); //get project by id
        project.setName(name);
        project.setStatus(status);
        project.setPriority(priority);
        if (status.equals("Active")) { //Using status for change time of start or time of finish project
            project.setStartDate(new Timestamp(System.currentTimeMillis()));
            project.setFinishDate(null);
        } else if (status.equals("Completed")) {
            project.setFinishDate(new Timestamp(System.currentTimeMillis()));
        } else if (status.equals("NotStarted")) {
            project.setStartDate(null);
            project.setFinishDate(null);
        }
        mainService.saveProject(project); //save project as object
        return "redirect:/";
    }

    @PostMapping(value = "/deleteproject") //delete project
    public String deleteProject(@RequestParam(name = "id") Long id) {
        Project project = mainService.getProject(id); //get project by id
        List<Task> tasks = new ArrayList<>();
        if (project.getTasks() != null) {
            tasks = project.getTasks(); //get project's tasks if they are exists
        }
        mainService.deteleProject(project); //delete project
        if (tasks.size() != 0) {
            for (Task t : project.getTasks()) {
                mainService.deleteTask(t); //delete project's tasks
            }
        }
        return "redirect:/"; //return to main page
    }

    @PostMapping(value = "/addtask") //add task
    public String addTask(@RequestParam(name = "task_name") String name,
                          @RequestParam(name = "task_description") String description,
                          @RequestParam(name = "task_status") String status,
                          @RequestParam(name = "task_priority") int priority,
                          @RequestParam(name = "project_id") Long projectId) {
        Task task = new Task();
        task.setName(name);
        task.setDescription(description);
        task.setStatus(status);
        task.setPriority(priority);
        Project project = mainService.getProject(projectId); //select project where task has been added
        List<Task> tasks = project.getTasks(); //get tasks of project for add new task
        if (tasks.size() != 0) { //add new task in project's tasks if they are exists
            Task t = mainService.saveTask(task); //save task in db and return it with new id for add in project
            tasks.add(t); //add task in collection
            project.setTasks(tasks); //add task in project's collection
            mainService.saveProject(project); //save project with new task
        } else { //add new collection if project's tasks doesn't exist
            Task t = mainService.saveTask(task); //save task in db and return it with new id for add in project
            List<Task> newTasks = new ArrayList<>();
            newTasks.add(t);
            project.setTasks(newTasks);
            mainService.saveProject(project);
        }
        return "redirect:/projectdetails/" + projectId;
    }

    @PostMapping(value = "/edittask") //edit task
    public String editTask(@RequestParam(name = "task_name") String name,
                           @RequestParam(name = "task_description") String description,
                           @RequestParam(name = "status") String status,
                           @RequestParam(name = "priority") int priority,
                           @RequestParam(name = "id") Long id,
                           @RequestParam(name = "project_id") Long projectId) {
        Task task = mainService.getTask(id); //get task by id
        task.setName(name);
        task.setDescription(description);
        task.setStatus(status);
        task.setPriority(priority);
        mainService.saveTask(task); //save edited task in db
        return "redirect:/projectdetails/" + projectId;
    }

    @PostMapping(value = "deletetask") //delete task
    public String deleteTask(@RequestParam(name = "id") Long id,
                             @RequestParam(name = "project_id") Long projectId) {
        Task task = mainService.getTask(id); //get task by id
        Project project = mainService.getProject(projectId); //get project by id
        List<Task> tasks = project.getTasks(); //get project's tasks for remove selected task from collection
        tasks.remove(task); //remove selected task from collection
        project.setTasks(tasks); //add edited collection without selected task
        mainService.saveProject(project); //save edited collection
        mainService.deleteTask(task); //delete selected task
        return "redirect:/projectdetails/" + projectId;
    }
}