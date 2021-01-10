package lab3.model;
import java.util.ArrayList;
import java.util.List;

public class Student extends Person {
    private long studentId;
    private int totalCredits;
    private List<Long> enrolledCourses;

    //constructors

    /**
     * Default constructor
     */
    public Student() {
        super();
        this.studentId = 0;
        this.totalCredits = 0;
        this.enrolledCourses = new ArrayList<>();
    }

    /**
     * Constructor with parameters
     *
     * @param studentId student id
     * @param firstName first name
     * @param lastName  last name
     */
    public Student(long studentId, String firstName, String lastName) {
        super(firstName, lastName);
        this.studentId = studentId;
        this.totalCredits = 0;
        this.enrolledCourses = new ArrayList<>();
    }

    //getters

    /**
     * Returns the student's ID
     *
     * @return Student ID
     */
    public long getStudentId() {
        return studentId;
    }

    /**
     * Returns the student's credits
     *
     * @return Number of credits
     */
    public int getTotalCredits() {
        return totalCredits;
    }

    /**
     * Returns the enrolled Courses
     *
     * @return Enrolled courses
     */
    public List<Long> getEnrolledCourses() {
        return enrolledCourses;
    }

    //setters

    /**
     * Sets the student's credits
     *
     * @param totalCredits total credits
     */
    public void setTotalCredits(int totalCredits) {
        this.totalCredits = totalCredits;
    }

    /**
     * Sets the enrolled Courses
     *
     * @param enrolledCourses enrolled courses
     */
    public void setEnrolledCourses(List<Long> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }
}
