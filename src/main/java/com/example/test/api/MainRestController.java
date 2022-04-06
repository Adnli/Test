package com.example.test.api;

import com.example.test.model.Project;
import com.example.test.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin
@RequiredArgsConstructor
public class MainRestController {

    private final MainService mainService;

    @GetMapping(value = "/allprojects") //get all projects
    public ResponseEntity<List<Project>> getProjects() {
        List<Project> projects = mainService.getProjectsByPriority(); //get all projects from db ordered by priority
        if (projects != null) {
            return new ResponseEntity<>(projects, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/addproject") //add new project
    public ResponseEntity<String> addProject(@RequestParam(name = "projectName") String name) {
        Project project = new Project();
        project.setName(name);
        project.setPriority(3);
        project.setStatus("NotStarted");
        mainService.saveProject(project); //save new project
        return new ResponseEntity<>("Added successfully", HttpStatus.OK);
    }
}
