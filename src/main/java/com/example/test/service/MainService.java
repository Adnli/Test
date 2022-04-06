package com.example.test.service;

import com.example.test.model.Project;
import com.example.test.model.Task;
import com.example.test.repository.ProjectRepository;
import com.example.test.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MainService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    public Task getTask(Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    public List<Task> getTasks() {
        return taskRepository.findAll();
    }

    public void deleteTask(Task task) {
        taskRepository.delete(task);
    }

    public void saveProject(Project project) {
        projectRepository.save(project);
    }

    public Project getProject(Long id) {
        return projectRepository.findById(id).orElse(null);
    }

    public List<Project> getProjects() {
        return projectRepository.findAll();
    }

    public void deteleProject(Project project) {
        projectRepository.delete(project);
    }

    public List<Project> getProjectsByPriority() {
        return projectRepository.findAllByIdNotNullOrderByPriority();
    }
}
