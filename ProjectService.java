package projects.service;

import projects.dao.ProjectDao;
import projects.entity.Project;
import projects.exception.DbException;

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

    public void modifyProjectDetails(Project project) {
        if(!projectDao.modifyProjectDetails(project)){
            throw new DbException("Project with ID=" + project.getProjectId() + "does not exist.");
        }
    }

    public void deleteProject(Integer projectID) {
        if(!projectDao.deleteProject(projectID)){
            throw new DbException("Project with ID= " + projectID + " does not exist.");
        }
    }
}
