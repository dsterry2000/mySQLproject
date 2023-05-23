package projects;

import projects.entity.Project;
import projects.exception.DbException;
import projects.service.ProjectService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class ProjectsApp {

    private ProjectService projectService = new ProjectService();
    Scanner sc = new Scanner(System.in);
    //@formatter:off
    private List<String> operations = List.of(
            "-1. Exit",
            "1. Add a project",
            "2. List projects",
            "3. Select a project",
            "4. Update project details",
            "5. Delete a project"
    );
    //@formatter:on
    private Project currentProject;
    public static void main(String[] args){

        new ProjectsApp().processUserSelections();

    }

    private void processUserSelections() {
        boolean done = false;

        while(!done){
            try{
              int selection = getUserSelection();
              switch(selection){
                  case -1:
                      done = exitMenu();
                      break;
                  case 1:
                      createProject();
                      break;
                  case 2:
                      listProjects();
                      break;
                  case 3: selectProject();
                          break;
                  case 4: updateProjectDetails();
                          break;
                  case 5: deleteProject();
                          break;
                  default:
                      System.out.println("\n" + selection + " is not a valid selection. Try again.");

              }
            }catch(Exception e){
                System.out.println("\nError: " + e + " Try again.");
            }
        }

    }

    private boolean exitMenu() {
        System.out.println("\nExiting the menu.");
        return true;
    }

    private void createProject() {
        String projectName = getStringInput("Enter the project name");
        BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours");
        BigDecimal actualHours = getDecimalInput("Enter the actual hours");
        Integer difficulty = getIntInput("Enter the difficulty level (1-5)");
        String notes = getStringInput("Enter the project notes");

        Project project = new Project();

        project.setProjectName(projectName);
        project.setEstimatedHours(estimatedHours);
        project.setActualHours(actualHours);
        project.setDifficulty(difficulty);
        project.setNotes(notes);

        Project dbProject = ProjectService.addProject(project);
        System.out.println("You have successfully created project: " + dbProject);
    }

    private void listProjects() {
        List<Project> projects = projectService.fetchAllProjects();

        System.out.println("\nProjects:");

        projects.forEach(project -> System.out.println(" " + project.getProjectId()
        + ": " + project.getProjectName()));
    }

    private void selectProject(){
        listProjects();
        Integer projectID = getIntInput("Enter a project ID to select a project");

        //Unselect the current project
        currentProject = null;
        currentProject = projectService.fetchByProjectID(projectID);//throws an exception if an invalid ID is entered.
    }

    private void updateProjectDetails() {
        if(Objects.isNull(currentProject)) {
            System.out.println("\nPlease select a project.");
            return;
        }

        String projectName = getStringInput("Enter the project name [current: " + currentProject.getProjectName() + "]");
        BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours [current: " + currentProject.getEstimatedHours() + "]");
        BigDecimal actualHours = getDecimalInput("Enter the actual hours [current: " + currentProject.getActualHours() + "]");
        Integer difficulty = getIntInput("Enter the difficulty level (1-5) [current: " + currentProject.getDifficulty() + "]");
        String notes = getStringInput("Enter project notes [current: " + currentProject.getNotes() + "]");

        Project project = new Project();

        project.setProjectId(currentProject.getProjectId());
        project.setProjectName(Objects.isNull(projectName) ? currentProject.getProjectName() : projectName);
        project.setEstimatedHours(Objects.isNull(estimatedHours) ? currentProject.getEstimatedHours() : estimatedHours);
        project.setActualHours(Objects.isNull(actualHours) ? currentProject.getActualHours() : actualHours);
        project.setDifficulty(Objects.isNull(difficulty) ? currentProject.getDifficulty() : difficulty);
        project.setNotes(Objects.isNull(notes) ? currentProject.getNotes() : notes);

        projectService.modifyProjectDetails(project);
        currentProject = projectService.fetchByProjectID(currentProject.getProjectId());
    }

    private void deleteProject() {
        listProjects();

        Integer projectID = getIntInput("Enter Project ID to delete a project: ");

        projectService.deleteProject(projectID);
        System.out.println("Project " + projectID + " was successfully deleted.");

        if(Objects.isNull(currentProject) && currentProject.getProjectId().equals(projectID)){
            currentProject = null;
        }
    }

    private int getUserSelection() {
        printOperations();

        Integer input = getIntInput("Enter a menu selection");

        return Objects.isNull(input) ? -1 : input;
    }

    private void printOperations() {
        System.out.println();
        System.out.println("These are the available selections. Press the Enter key to quit: ");
        operations.forEach(op -> System.out.println("\t" + op));

        if(Objects.isNull(currentProject)){
            System.out.println("\nYou are not working with a project.");
        }
        else{
            System.out.println("\nYou are working with project: " + currentProject);
        }
    }

    private Integer getIntInput(String prompt) {
        String input = getStringInput(prompt);

        if(Objects.isNull(input)){
            return null;
        }

        try{
            return Integer.valueOf(input);
        }catch (NumberFormatException e){
            throw new DbException(input + " is not a valid number.");
        }

    }

    private BigDecimal getDecimalInput(String prompt) {
        String input = getStringInput(prompt);

        if(Objects.isNull(input)){
            return null;
        }

        try{
            return new BigDecimal(input).setScale(2);
        }catch (NumberFormatException e){
            throw new DbException(input + " is not a valid decimal number.");
        }
    }
    private String getStringInput(String prompt) {
        System.out.print(prompt + ": ");
        String input = sc.nextLine();

        return input.isBlank() ? null : input.trim();
    }


}
