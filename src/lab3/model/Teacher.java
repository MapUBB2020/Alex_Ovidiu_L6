package lab3.model;
import java.util.ArrayList;
import java.util.List;

public class Teacher extends Person {
    private long teacherId;
    private List<Long> courses;

    //constructors

    /**
     * Default constructor
     */
    public Teacher() {
        super();
        this.teacherId = 0;
        this.courses = new ArrayList<>();
    }

    /**
     * Constructor with parameters
     *
     * @param teacherId teacher id
     * @param firstName first name
     * @param lastName  last name
     */
    public Teacher(long teacherId, String firstName, String lastName) {
        super(firstName, lastName);
        this.teacherId = teacherId;
        this.courses = new ArrayList<>();
    }

    //getters

    /**
     * Returns the teacher's ID
     *
     * @return Teacher ID
     */
    public long getTeacherId() {
        return teacherId;
    }

    /**
     * Returns the teacher's Courses
     *
     * @return Courses
     */
    public List<Long> getCourses() {
        return courses;
    }

    //setters

    /**
     * Sets the teacher's Courses
     *
     * @param courses courses
     */
    public void setCourses(List<Long> courses) {
        this.courses = courses;
    }
}
