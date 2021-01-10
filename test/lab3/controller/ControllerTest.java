package lab3.controller;
import lab3.model.Course;
import org.junit.Assert;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.junit.Test;

public class ControllerTest {

    @Test
    public void controllerTest() {
        try {
            RegistrationSystem tester = new RegistrationSystem();

            Scanner scanner = new Scanner("files/courses.txt");
            List<String> courses = new ArrayList<>();

            while (scanner.hasNextLine()) {
                String[] courseData = scanner.nextLine().split(";");

                Course course = new Course(Long.parseLong(courseData[0]), courseData[1], Integer.parseInt(courseData[2]), Integer.parseInt(courseData[3]));
                course.setTeacher(Long.parseLong(courseData[4]));

                if (courseData[5].compareTo("-") != 0) {
                    String[] studentData = courseData[5].split(",");
                    List<Long> studentsEnrolled = new ArrayList<>();
                    for (String student : studentData) {
                        studentsEnrolled.add(Long.parseLong(student));
                    }
                    course.setStudentsEnrolled(studentsEnrolled);
                }

                courses.add(course.getCourseId() + " : " + course.getName()
                        + " | Credits: " + course.getCredits()
                        + " | Maximum enrollment: " + course.getMaxEnrollment()
                        + " | Available places: " + (course.getMaxEnrollment() - course.getStudentsEnrolled().size()));
            }
            scanner.close();

            Assert.assertEquals(tester.showAllCourses(), courses);

        } catch (Exception ignored) { }
    }
}
