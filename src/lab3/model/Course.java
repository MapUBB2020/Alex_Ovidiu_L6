package lab3.model;
import java.util.ArrayList;
import java.util.List;

public class Course {
    private long courseId;
    private String name;
    private int maxEnrollment;
    private int credits;
    private long teacher;
    private List<Long> studentsEnrolled;

    //constructors

    /**
     * Default constructor
     */
    public Course() {
        this.courseId = -1;
        this.name = "";
        this.maxEnrollment = -1;
        this.credits = -1;
        this.teacher = -1;
        this.studentsEnrolled = new ArrayList<>();
    }

    /**
     * Constructor with parameters
     *
     * @param courseId      course id
     * @param name          name
     * @param maxEnrollment max enrollment
     * @param credits       credits
     */
    public Course(long courseId, String name, int maxEnrollment, int credits) {
        this.courseId = courseId;
        this.name = name;
        this.maxEnrollment = maxEnrollment;
        this.credits = credits;
        this.teacher = -1;
        this.studentsEnrolled = new ArrayList<>();
    }

    //getters

    /**
     * Returns the course's ID
     *
     * @return Course ID
     */
    public long getCourseId() {
        return courseId;
    }

    /**
     * Returns the name of the course
     *
     * @return Name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the maximum enrollment
     *
     * @return Maximum enrollment
     */
    public int getMaxEnrollment() {
        return maxEnrollment;
    }

    /**
     * Returns the course's number of credits
     *
     * @return Credits
     */
    public int getCredits() {
        return credits;
    }

    /**
     * Returns the course's teacher
     *
     * @return Teacher
     */
    public long getTeacher() {
        return teacher;
    }

    /**
     * Returns the list of enrolled students
     *
     * @return Enrolled students
     */
    public List<Long> getStudentsEnrolled() {
        return studentsEnrolled;
    }

    //setters

    /**
     * Sets the name of the course
     *
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the maximum enrollment
     *
     * @param maxEnrollment max enrollment
     */
    public void setMaxEnrollment(int maxEnrollment) {
        this.maxEnrollment = maxEnrollment;
    }

    /**
     * Sets the course's number of credits
     *
     * @param credits credits
     */
    public void setCredits(int credits) {
        this.credits = credits;
    }

    /**
     * Sets the course's teacher
     *
     * @param teacher teacher
     */
    public void setTeacher(long teacher) {
        this.teacher = teacher;
    }

    /**
     * Sets the list of enrolled students
     *
     * @param studentsEnrolled students enrolled
     */
    public void setStudentsEnrolled(List<Long> studentsEnrolled) {
        this.studentsEnrolled = studentsEnrolled;
    }
}
