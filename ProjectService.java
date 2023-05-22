package projects.service;

import projects.dao.ProjectDao;
import projects.entity.Project;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class ProjectService {

    private ProjectDao projectDao = new ProjectDao();
    public static Project addProject(Project project) {
        ProjectDao projectDao = new ProjectDao();

        return projectDao.insertProject(project);
    }

    public List<Project> fetchAllProjects() {
        return projectDao.fetchAllProjects();
    }

    public Project fetchByProjectID(Integer projectID) {
        return projectDao.fetchByProjectID(projectID).orElseThrow(() -> new NoSuchElementException(
                "Project with project ID= " + projectID + "does not exist."
        ));
    }
}
