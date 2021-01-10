package lab3.view;
import lab3.controller.RegistrationSystem;
import lab3.exceptions.AssignationException;
import lab3.exceptions.IDInUseException;
import lab3.exceptions.IDInvalidException;
import lab3.exceptions.InvalidDataException;
import java.util.List;
import java.util.Scanner;

public class UI {

    private RegistrationSystem registrationSystem;
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_WHITE = "\u001B[30m";

    /**
     * Default constructor
     */
    public UI() {
        try {
            this.registrationSystem = new RegistrationSystem();
        } catch (Exception ex) {
            System.out.println(ANSI_RED+"An error has occurred."+ANSI_RESET);
        }
    }

    /**
     * displays the UI and allows for user input
     */
    public void displayUI() {
        while (true) {
            Scanner keyboard = new Scanner(System.in);

            System.out.print(ANSI_WHITE);
            System.out.println("\n-Registration System-\n" +
                    "1. Add a course\n" +
                    "2. Add a teacher\n" +
                    "3. Add a student\n" +
                    "4. View all existing courses\n" +
                    "5. View all available courses\n" +
                    "6. View all existing teachers\n" +
                    "7. View all existing students\n" +
                    "8. View all students enrolled in a course\n" +
                    "9. View all students sorted by their credits\n" +
                    "10. Assign a teacher to a course\n" +
                    "11. Remove the teacher from a course\n" +
                    "12. Assign a student to a course\n" +
                    "13. Remove a student from a course\n" +
                    "14. Change a course's credits\n" +
                    "15. Delete a course\n" +
                    "16. Delete a teacher\n" +
                    "17. Delete a student\n" +
                    "0. Exit\n");

            try {
                int op = Integer.parseInt(keyboard.next());

                if (op == 0) {
                    break;
                }

                if (op == 1) {
                    System.out.print("Course ID: ");
                    long courseId = keyboard.nextLong();

                    System.out.print("Course name: ");
                    String name = keyboard.next();

                    System.out.print("Maximum enrollment: ");
                    int maxEnrollment = keyboard.nextInt();

                    System.out.print("Number of credits: ");
                    int credits = keyboard.nextInt();

                    try {
                        this.registrationSystem.addCourse(courseId, name, maxEnrollment, credits);
                    } catch (IDInUseException | IDInvalidException ex) {
                        System.out.println(ex.getMessage());
                    } catch (Exception ex) {
                        System.out.println("Error!");
                    }
                }

                if (op == 2) {
                    System.out.print("Teacher ID: ");
                    long teacherId = keyboard.nextLong();

                    System.out.print("Teacher's first name: ");
                    String firstName = keyboard.next();

                    System.out.print("Teacher's last name: ");
                    String lastName = keyboard.next();

                    try {
                        this.registrationSystem.addTeacher(teacherId, firstName, lastName);
                    } catch (IDInUseException | IDInvalidException ex) {
                        System.out.println(ex.getMessage());
                    } catch (Exception ex) {
                        System.out.println("Error!");
                    }
                }

                if (op == 3) {
                    System.out.print("Student ID: ");
                    long studentId = keyboard.nextLong();

                    System.out.print("Student's first name: ");
                    String firstName = keyboard.next();

                    System.out.print("Student's last name: ");
                    String lastName = keyboard.next();

                    try {
                        this.registrationSystem.addStudent(studentId, firstName, lastName);
                    } catch (IDInUseException | IDInvalidException ex) {
                        System.out.println(ex.getMessage());
                    } catch (Exception ex) {
                        System.out.println("Error!");
                    }
                }

                if (op == 4) {
                    try {
                        List<String> courses = this.registrationSystem.showAllCourses();
                        if (courses.size() == 0)
                            System.out.println("Nothing to show.");
                        else {
                            for (String course : courses) {
                                System.out.println(course);
                            }
                        }
                    } catch (InvalidDataException ex) {
                        System.out.println(ex.getMessage());
                    }
                }

                if (op == 5) {
                    try {
                        List<String> courses = this.registrationSystem.showAvailableCourses();
                        if (courses.size() == 0)
                            System.out.println("Nothing to show.");
                        else {
                            for (String course : courses) {
                                System.out.println(course);
                            }
                        }
                    } catch (InvalidDataException ex) {
                        System.out.println(ex.getMessage());
                    }
                }

                if (op == 6) {
                    List<String> teachers = this.registrationSystem.showAllTeachers();
                    if (teachers.size() == 0)
                        System.out.println("Nothing to show.");
                    else {
                        for (String teacher : teachers) {
                            System.out.println(teacher);
                        }
                    }
                }

                if (op == 7) {
                    List<String> students = this.registrationSystem.showAllStudents();
                    if (students.size() == 0)
                        System.out.println("Nothing to show.");
                    else {
                        for (String student : students) {
                            System.out.println(student);
                        }
                    }
                }

                if (op == 8) {
                    System.out.print("Course ID: ");
                    long courseId = keyboard.nextLong();

                    List<String> students = this.registrationSystem.showEnrolledStudents(courseId);
                    if (students.size() == 0)
                        System.out.println("Nothing to show.");
                    else {
                        for (String student : students) {
                            System.out.println(student);
                        }
                    }
                }

                if (op == 9) {
                    List<String> students = this.registrationSystem.showSortedStudents();
                    if (students.size() == 0)
                        System.out.println("Nothing to show.");
                    else {
                        for (String student : students) {
                            System.out.println(student);
                        }
                    }
                }

                if (op == 10) {
                    System.out.print("Teacher ID: ");
                    long teacherId = keyboard.nextLong();

                    System.out.print("Course ID: ");
                    long courseId = keyboard.nextLong();

                    try {
                        this.registrationSystem.assignTeacherToCourse(teacherId, courseId);
                    } catch (AssignationException | IDInvalidException ex) {
                        System.out.println(ex.getMessage());
                    } catch (Exception ex) {
                        System.out.println("Error!");
                    }
                }

                if (op == 11) {
                    System.out.print("Course ID: ");
                    long courseId = keyboard.nextLong();

                    try {
                        this.registrationSystem.removeTeacherFromCourse(courseId);
                    } catch (AssignationException | IDInvalidException ex) {
                        System.out.println(ex.getMessage());
                    } catch (Exception ex) {
                        System.out.println("Error!");
                    }
                }

                if (op == 12) {
                    System.out.print("Student ID: ");
                    long studentId = keyboard.nextLong();

                    System.out.print("Course ID: ");
                    long courseId = keyboard.nextLong();

                    try {
                        this.registrationSystem.assignStudentToCourse(studentId, courseId);
                    } catch (AssignationException | IDInvalidException ex) {
                        System.out.println(ex.getMessage());
                    } catch (Exception ex) {
                        System.out.println("Error!");
                    }
                }

                if (op == 13) {
                    System.out.print("Student ID: ");
                    long studentId = keyboard.nextLong();

                    System.out.print("Course ID: ");
                    long courseId = keyboard.nextLong();

                    try {
                        this.registrationSystem.removeStudentFromCourse(studentId, courseId);
                    } catch (AssignationException | IDInvalidException ex) {
                        System.out.println(ex.getMessage());
                    } catch (Exception ex) {
                        System.out.println("Error!");
                    }
                }

                if (op == 14) {
                    System.out.print("Course ID: ");
                    long courseId = keyboard.nextLong();

                    System.out.print("New credit count: ");
                    int credits = keyboard.nextInt();

                    try {
                        this.registrationSystem.changeCredits(courseId, credits);
                    } catch (InvalidDataException | IDInvalidException ex) {
                        System.out.println(ex.getMessage());
                    } catch (Exception ex) {
                        System.out.println("Error!");
                    }
                }

                if (op == 15) {
                    System.out.print("Course ID: ");
                    long courseId = keyboard.nextLong();

                    try {
                        this.registrationSystem.deleteCourse(courseId);
                    } catch (InvalidDataException | IDInvalidException ex) {
                        System.out.println(ex.getMessage());
                    } catch (Exception ex) {
                        System.out.println("Error!");
                    }
                }

                if (op == 16) {
                    System.out.print("Teacher ID: ");
                    long teacherId = keyboard.nextLong();

                    try {
                        this.registrationSystem.deleteTeacher(teacherId);
                    } catch (InvalidDataException | IDInvalidException ex) {
                        System.out.println(ex.getMessage());
                    } catch (Exception ex) {
                        System.out.println("Error!");
                    }
                }

                if (op == 17) {
                    System.out.print("Student ID: ");
                    long studentId = keyboard.nextLong();

                    try {
                        this.registrationSystem.deleteStudent(studentId);
                    } catch (InvalidDataException | IDInvalidException ex) {
                        System.out.println(ex.getMessage());
                    } catch (Exception ex) {
                        System.out.println("Error!");
                    }
                }

                if (op < 0 || op > 17) {
                    System.out.println("Invalid command!");
                }

            } catch (Exception ex) {
                System.out.println("Invalid command!");
            }
        }
    }
}
