package com.example.test.repository;

import com.example.test.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findAllByIdNotNullOrderByPriority();
}
