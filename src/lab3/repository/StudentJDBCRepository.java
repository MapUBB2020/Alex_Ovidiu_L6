package lab3.repository;
import lab3.model.Student;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentJDBCRepository implements ICrudRepository<Student> {

    private Statement statement;

    /**
     * Default constructor
     *
     * @throws Exception if connection fails
     */
    public StudentJDBCRepository() throws Exception {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
        Connection connection = DriverManager.getConnection("jdbc:sqlserver://DESKTOP-LV0L1J0\\SQLEXPRESS;databaseName=LabMAP;integratedSecurity=true");
        statement = connection.createStatement();
    }

    /**
     * @param id -the id of the entity to be returned id must not be null
     * @return the entity with the specified id or null - if there is no entity with the given id
     */
    @Override
    public Student findOne(Long id) {
        return this.databaseReadOne(id);
    }

    /**
     * @return all entities
     */
    @Override
    public Iterable<Student> findAll() {
        return this.databaseReadAll();
    }

    /**
     * @param entity entity must be not null
     * @return null- if the given entity is saved otherwise returns the entity (id already exists)
     * @throws Exception if sql commands fail
     */
    @Override
    public Student save(Student entity) throws Exception {
        if (this.findOne(entity.getStudentId()) == null) {
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
    public Student delete(Long id) throws Exception {
        Student deleted = this.findOne(id);
        if (deleted != null) {
            this.databaseDelete(id);
        }
        return deleted;
    }

    /**
     * @param entity entity must not be null
     * @return null - if the entity is updated, otherwise returns the entity - (e.g id does not exist).
     * @throws Exception if sql commands fail
     */
    @Override
    public Student update(Student entity) throws Exception {
        Student updated = this.findOne(entity.getStudentId());
        if (updated != null) {
            this.databaseUpdate(entity);
            return null;
        }
        return entity;
    }

    /**
     * Loads one student from the database
     */
    public Student databaseReadOne(Long id) {
        try {
            ResultSet resultSet = statement.executeQuery("Select * from Student where Student.studentId=" + id);

            if (resultSet.next()) {
                Student student = new Student(resultSet.getLong("studentId"), resultSet.getString("name"), resultSet.getString("surname"));
                student.setTotalCredits(resultSet.getInt("totalCredits"));
                try {
                    ResultSet courseIds = statement.executeQuery("Select courseId from Enrollment where Enrollment.studentId=" + student.getStudentId());
                    List<Long> enrolledCourses = new ArrayList<>();
                    while (courseIds.next()) {
                        enrolledCourses.add(courseIds.getLong("courseId"));
                    }
                    student.setEnrolledCourses(enrolledCourses);
                } catch (SQLException ignored) {
                }
                return student;
            }
        } catch (SQLException ignored) {
        }
        return null;
    }

    /**
     * Loads all courses from the database
     */
    public List<Student> databaseReadAll() {
        List<Student> students = new ArrayList<>();

        try {
            ResultSet resultSet = statement.executeQuery("Select * from Student");

            while (resultSet.next()) {
                Student student = new Student(resultSet.getLong("studentId"), resultSet.getString("name"), resultSet.getString("surname"));
                student.setTotalCredits(resultSet.getInt("totalCredits"));
                students.add(student);
            }
            students.forEach(student -> {
                ResultSet courseIds;
                try {
                    courseIds = statement.executeQuery("Select courseId from Enrollment where Enrollment.studentId=" + student.getStudentId());
                    List<Long> enrolledCourses = new ArrayList<>();
                    while (courseIds.next()) {
                        enrolledCourses.add(courseIds.getLong("courseId"));
                    }
                    student.setEnrolledCourses(enrolledCourses);
                } catch (SQLException ignored) {
                }
            });
        } catch (SQLException ignored) {
        }

        return students;
    }

    /**
     * Saves a student to the database
     *
     * @param student to be saved
     * @throws Exception if sql commands fail
     */
    public void databaseSave(Student student) throws Exception {
        statement.execute("Insert into Student values (" + student.getStudentId() + ",'" + student.getFirstName() + "','" + student.getLastName() + "'," + student.getTotalCredits() + ")");
    }

    /**
     * Deletes a student from the database
     *
     * @param id id of the student to be deleted
     * @throws Exception if sql commands fail
     */
    public void databaseDelete(Long id) throws Exception {
        statement.execute("Delete from Student where Student.studentId=" + id);
        statement.execute("Delete from Enrollment where Enrollment.studentId=" + id);
    }

    /**
     * Updates a student in the database
     *
     * @param student to be updated
     * @throws Exception if sql commands fail
     */
    public void databaseUpdate(Student student) throws Exception {
        statement.execute("Delete from Student where Student.studentId=" + student.getStudentId());
        statement.execute("Delete from Enrollment where Enrollment.studentId=" + student.getStudentId());
        statement.execute("Insert into Student values (" + student.getStudentId() + ",'" + student.getFirstName() + "','" + student.getLastName() + "'," + student.getTotalCredits() + ")");

        student.getEnrolledCourses().forEach(course -> {
            try {
                statement.execute("Insert into Enrollment values (" + student.getStudentId() + "," + course + ")");
            } catch (SQLException ignored) {
            }
        });
    }
}
