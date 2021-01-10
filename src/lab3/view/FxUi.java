package lab3.view;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lab3.controller.RegistrationSystem;
import lab3.model.Course;
import lab3.model.Student;

import java.util.concurrent.atomic.AtomicBoolean;

public class FxUi {
    private Stage window;
    private RegistrationSystem controller = new RegistrationSystem();
    private TableView<Student> table;
    private long currentTeacher;

    /**
     * Constructor
     *
     * @param window Main window
     */
    public FxUi(Stage window) throws Exception {
        try {
            this.controller = new RegistrationSystem();
            this.window = window;
        } catch (Exception ex) {
            System.out.println("An error has occurred.");
        }
    }

    /**
     * Starts the application in teacher mode and student mode
     */
    public void displayUi() {
        this.loginWindow(true);
        this.loginWindow(false);
    }

    /**
     * Teacher window for viewing students
     * @param id teacher's ID
     * @param name teacher's name
     * @param surname teacher's surname
     */
    private void teacherWindow(long id, String name, String surname) {
        window.setTitle("Teacher "+id+" : "+name+" "+surname);

        TableColumn<Student,String> studentName=new TableColumn<>("Name");
        studentName.setMinWidth(200);
        studentName.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<Student,String> studentSurname=new TableColumn<>("Surname");
        studentSurname.setMinWidth(200);
        studentSurname.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<Student, Integer> studentCredits=new TableColumn<>("Credits");
        studentCredits.setMinWidth(200);
        studentCredits.setCellValueFactory(new PropertyValueFactory<>("totalCredits"));

        table=new TableView<>();
        table.getColumns().addAll(studentName,studentSurname,studentCredits);
        table.setItems(this.controller.observableStudents(id));

        Scene scene=new Scene(table);
        window.setScene(scene);
        window.show();
    }

    /**
     * Teacher window for viewing and interacting with courses
     * @param id student's ID
     * @param name student's name
     * @param surname student's surname
     */
    private void studentWindow(long id, String name, String surname) {
        Stage studentWindow = new Stage();
        studentWindow.setTitle("Student " + id + " : " + name + " " + surname);

        TableColumn<Course, String> courseId = new TableColumn<>("ID");
        courseId.setMinWidth(100);
        courseId.setCellValueFactory(new PropertyValueFactory<>("courseId"));

        TableColumn<Course, String> courseName = new TableColumn<>("Course name");
        courseName.setMinWidth(200);
        courseName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Course, Integer> courseCredits = new TableColumn<>("Credit Count");
        courseCredits.setMinWidth(200);
        courseCredits.setCellValueFactory(new PropertyValueFactory<>("credits"));

        TableColumn<Course, Integer> courseEnrollment = new TableColumn<>("Maximum enrollment");
        courseEnrollment.setMinWidth(200);
        courseEnrollment.setCellValueFactory(new PropertyValueFactory<>("maxEnrollment"));

        TableView<Course> courseTable = new TableView<>();
        courseTable.getColumns().addAll(courseId, courseName, courseCredits, courseEnrollment);
        courseTable.setItems(this.controller.observableCourses());

        TextField enrollInput = new TextField();
        enrollInput.setPromptText("Course ID");
        Button enrollButton = new Button("Enroll");
        Button leaveButton = new Button("Leave");
        Label studentCredits = new Label("                                                     "
                + "Current credits: " + this.controller.getCredits(id));
        studentCredits.setPadding(new Insets(5,0,0,0));

        Label messageBox = new Label("Welcome! Messages will appear here.");
        messageBox.setPadding(new Insets(0, 10, 20, 10));

        HBox courseInteraction = new HBox(10);
        courseInteraction.setPadding(new Insets(10, 10, 10, 10));
        courseInteraction.getChildren().addAll(enrollInput, enrollButton, leaveButton, studentCredits);

        VBox layout = new VBox(8);
        layout.getChildren().addAll(courseTable, courseInteraction, messageBox);

        enrollButton.setOnAction(e->{
            try {
                this.controller.assignStudentToCourse(id,Long.parseLong(enrollInput.getText()));
                messageBox.setText("Enrolled successfully");
                studentCredits.setText("                                                     "
                        + "Current credits: " + this.controller.getCredits(id));
                table.setItems(this.controller.observableStudents(this.currentTeacher));
            } catch (Exception ex) {
                messageBox.setText("Unable to enroll");
            }
            enrollInput.clear();
        });

        leaveButton.setOnAction(e->{
            try {
                this.controller.removeStudentFromCourse(id,Long.parseLong(enrollInput.getText()));
                messageBox.setText("Left course successfully");
                studentCredits.setText("                                                     "
                        + "Current credits: " + this.controller.getCredits(id));
                table.setItems(this.controller.observableStudents(this.currentTeacher));
            } catch (Exception ex) {
                messageBox.setText("Unable to leave course");
            }
            enrollInput.clear();
        });

        Scene scene = new Scene(layout);
        studentWindow.setScene(scene);
        studentWindow.show();
    }

    /**
     * Creates a confirmation window when a new user might be created
     */
    private boolean confirmationWindow(boolean teacherMode, long id, String name, String surname) {
        AtomicBoolean answer = new AtomicBoolean(false);

        Stage confirmation = new Stage();
        confirmation.initModality(Modality.APPLICATION_MODAL);
        confirmation.setTitle("Confirm new registration");

        Label prompt = new Label("User not found. Create new login?");
        Button confirmButton = new Button("Yes");
        Button denyButton = new Button("No");

        HBox buttons = new HBox();
        buttons.setSpacing(20);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(confirmButton, denyButton);

        VBox confirmationLayout = new VBox();
        confirmationLayout.setSpacing(20);
        confirmationLayout.setAlignment(Pos.CENTER);
        confirmationLayout.getChildren().addAll(prompt, buttons);

        Scene scene = new Scene(confirmationLayout, 600, 200);

        denyButton.setOnAction(e -> confirmation.close());

        confirmButton.setOnAction(e -> {
            try {
                if (teacherMode)
                    this.controller.addTeacher(id, name, surname);
                else
                    this.controller.addStudent(id, name, surname);
                answer.set(true);
            } catch (Exception ignored) {
            }
            confirmation.close();
        });


        confirmation.setScene(scene);
        confirmation.showAndWait();

        return answer.get();
    }

    /**
     * Creates a login window
     */
    private void loginWindow(boolean teacherMode) {
        Stage loginStage = new Stage();

        if (teacherMode)
            loginStage.setTitle("Teacher login");
        else
            loginStage.setTitle("Student login");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(8);
        grid.setHgap(10);

        Label nameLabel = new Label("Name:");
        Label surnameLabel = new Label("Surname:");
        Label idLabel;

        if (teacherMode)
            idLabel = new Label("Teacher Id:");
        else
            idLabel = new Label("Student Id:");

        TextField nameField = new TextField();
        TextField surnameField = new TextField();
        TextField idField = new TextField();

        nameField.setPromptText("First Name");
        surnameField.setPromptText("Last Name");
        idField.setPromptText("ID");

        Button loginConfirmButton = new Button("Log in");
        Button loginDenyButton = new Button("Cancel");
        HBox loginButtons = new HBox(10);
        loginButtons.getChildren().addAll(loginConfirmButton, loginDenyButton);

        GridPane.setConstraints(nameLabel, 0, 0);
        GridPane.setConstraints(nameField, 1, 0);
        GridPane.setConstraints(surnameLabel, 0, 1);
        GridPane.setConstraints(surnameField, 1, 1);
        GridPane.setConstraints(idLabel, 0, 2);
        GridPane.setConstraints(idField, 1, 2);
        GridPane.setConstraints(loginButtons, 1, 3);

        grid.getChildren().addAll(nameLabel, nameField, surnameLabel, surnameField, idLabel, idField, loginButtons);

        Scene loginScene = new Scene(grid, 600, 200);

        loginConfirmButton.setOnAction(e -> {
            long id = Long.parseLong(idField.getText());
            String name = nameField.getText();
            String surname = surnameField.getText();

            if (teacherMode) {
                if (this.controller.checkTeacher(id, name, surname)) {
                    this.currentTeacher=id;
                    this.teacherWindow(id, name, surname);
                    loginStage.close();
                } else {
                    boolean newUser = this.confirmationWindow(true, id, name, surname);
                    if (newUser) {
                        this.currentTeacher=id;
                        this.teacherWindow(id, name, surname);
                        loginStage.close();
                    }
                }
            } else {
                if (this.controller.checkStudent(id, name, surname)) {
                    this.studentWindow(id, name, surname);
                    loginStage.close();
                } else {
                    boolean newUser = this.confirmationWindow(false, id, name, surname);
                    if (newUser) {
                        this.studentWindow(id, name, surname);
                        loginStage.close();
                    }
                }
            }
        });
        loginDenyButton.setOnAction(e -> loginStage.close());

        loginStage.setScene(loginScene);
        loginStage.show();
    }
}
