package projects.service;

import projects.dao.ProjectDao;
import projects.entity.Project;

public class ProjectService {

    private ProjectDao projectDao = new ProjectDao();
    public static Project addProject(Project project) {
        ProjectDao projectDao = new ProjectDao();

        return projectDao.insertProject(project);
    }
}
