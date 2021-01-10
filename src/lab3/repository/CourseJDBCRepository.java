package lab3.repository;
import lab3.model.Course;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class CourseJDBCRepository implements ICrudRepository<Course> {

    private Statement statement;

    /**
     * Default constructor
     *
     * @throws Exception if connection fails
     */
    public CourseJDBCRepository() throws Exception {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
        Connection connection = DriverManager.getConnection("jdbc:sqlserver://DESKTOP-LV0L1J0\\SQLEXPRESS;databaseName=LabMAP;integratedSecurity=true");
        statement = connection.createStatement();
    }

    /**
     * @param id -the id of the entity to be returned id must not be null
     * @return the entity with the specified id or null - if there is no entity with the given id
     */
    @Override
    public Course findOne(Long id) {
        return this.databaseReadOne(id);
    }

    /**
     * @return all entities
     */
    @Override
    public Iterable<Course> findAll() {
        return this.databaseReadAll();
    }

    /**
     * @param entity entity must be not null
     * @return null- if the given entity is saved otherwise returns the entity (id already exists)
     * @throws Exception if sql commands fail
     */
    @Override
    public Course save(Course entity) throws Exception {
        if (this.findOne(entity.getCourseId()) == null) {
            this.databaseSave(entity);
            return null;
        }
        return entity;
    }

    /**
     * removes the entity with the specified id
     *
     * @param id id must be not null
     * @return the removed entity or null if there is no entity with the given id
     * @throws Exception if sql commands fail
     */
    @Override
    public Course delete(Long id) throws Exception {
        Course deleted = this.findOne(id);
        if (deleted != null) {
            this.databaseDelete(id);
        }
        return deleted;
    }

    /**
     * @param entity entity must be not null
     * @return null - if the entity is updated, otherwise returns the entity - (e.g id does not exist).
     * @throws Exception if sql commands fail
     */
    @Override
    public Course update(Course entity) throws Exception {
        Course updated = this.findOne(entity.getCourseId());
        if (updated != null) {
            this.databaseUpdate(entity);
            return null;
        }
        return entity;
    }

    /**
     * Loads one course from the database
     */
    public Course databaseReadOne(Long id) {
        try {
            ResultSet resultSet = statement.executeQuery("Select * from Course where Course.courseId=" + id);

            if (resultSet.next()) {
                Course course = new Course(resultSet.getLong("courseId"), resultSet.getString("name"), resultSet.getInt("maxEnrollment"), resultSet.getInt("credits"));
                course.setTeacher(resultSet.getLong("teacher"));
                try {
                    ResultSet studentIds = statement.executeQuery("Select studentId from Enrollment where Enrollment.courseId=" + course.getCourseId());
                    List<Long> studentsEnrolled = new ArrayList<>();
                    while (studentIds.next()) {
                        studentsEnrolled.add(studentIds.getLong("studentId"));
                    }
                    course.setStudentsEnrolled(studentsEnrolled);
                } catch (SQLException ignored) {
                }
                return course;
            }
        } catch (SQLException ignored) {
        }
        return null;
    }

    /**
     * Loads all courses from the database
     */
    public List<Course> databaseReadAll() {
        List<Course> courses = new ArrayList<>();

        try {
            ResultSet resultSet = statement.executeQuery("Select * from Course");

            while (resultSet.next()) {
                Course course = new Course(resultSet.getLong("courseId"), resultSet.getString("name"), resultSet.getInt("maxEnrollment"), resultSet.getInt("credits"));
                course.setTeacher(resultSet.getLong("teacher"));
                courses.add(course);
            }
            courses.forEach(course -> {
                ResultSet studentIds;
                try {
                    studentIds = statement.executeQuery("Select studentId from Enrollment where Enrollment.courseId=" + course.getCourseId());
                    List<Long> studentsEnrolled = new ArrayList<>();
                    while (studentIds.next()) {
                        studentsEnrolled.add(studentIds.getLong("studentId"));
                    }
                    course.setStudentsEnrolled(studentsEnrolled);
                } catch (SQLException ignored) {
                }
            });
        } catch (SQLException ignored) {
        }

        return courses;
    }

    /**
     * Saves a course to the database
     *
     * @param course to be saved
     * @throws Exception if sql commands fail
     */
    private void databaseSave(Course course) throws Exception {
        statement.execute("Insert into Course values (" + course.getCourseId() + ",'" + course.getName() + "'," + course.getMaxEnrollment() + "," + course.getCredits() + "," + course.getTeacher() + ")");
    }

    /**
     * Deletes a course from the database
     *
     * @param id id of the course to be deleted
     * @throws Exception if sql commands fail
     */
    private void databaseDelete(Long id) throws Exception {
        statement.execute("Delete from Course where Course.courseId=" + id);
        statement.execute("Delete from Enrollment where Enrollment.courseId=" + id);
    }

    /**
     * Updates a course in the database
     *
     * @param course to be updated
     * @throws Exception if sql commands fail
     */
    private void databaseUpdate(Course course) throws Exception {
        statement.execute("Delete from Course where Course.courseId=" + course.getCourseId());
        statement.execute("Delete from Enrollment where Enrollment.courseId=" + course.getCourseId());
        statement.execute("Insert into Course values (" + course.getCourseId() + ",'" + course.getName() + "'," + course.getMaxEnrollment() + "," + course.getCredits() + "," + course.getTeacher() + ")");

        course.getStudentsEnrolled().forEach(student -> {
            try {
                statement.execute("Insert into Enrollment values (" + student + "," + course.getCourseId() + ")");
            } catch (SQLException ignored) {
            }
        });
    }
}
