package lab3.repository;
import lab3.model.Teacher;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeacherJDBCRepository implements ICrudRepository<Teacher> {

    private Statement statement;

    /**
     * Default constructor
     *
     * @throws Exception if connection fails
     */
    public TeacherJDBCRepository() throws Exception {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
        Connection connection = DriverManager.getConnection("jdbc:sqlserver://DESKTOP-LV0L1J0\\SQLEXPRESS;databaseName=LabMAP;integratedSecurity=true");
        statement = connection.createStatement();
    }

    /**
     * @param id -the id of the entity to be returned id must not be null
     * @return the entity with the specified id or null - if there is no entity with the given id
     */
    @Override
    public Teacher findOne(Long id) {
        return this.databaseReadOne(id);
    }

    /**
     * @return all entities
     */
    @Override
    public Iterable<Teacher> findAll() {
        return this.databaseReadAll();
    }

    /**
     * @param entity entity must be not null
     * @return null- if the given entity is saved otherwise returns the entity (id already exists)
     * @throws Exception if sql commands fail
     */
    @Override
    public Teacher save(Teacher entity) throws Exception {
        if (this.findOne(entity.getTeacherId()) == null) {
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
    public Teacher delete(Long id) throws Exception {
        Teacher deleted = this.findOne(id);
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
    public Teacher update(Teacher entity) throws Exception {
        Teacher updated = this.findOne(entity.getTeacherId());
        if (updated != null) {
            this.databaseUpdate(entity);
            return null;
        }
        return entity;
    }

    /**
     * Loads one teacher from the database
     */
    public Teacher databaseReadOne(Long id) {
        try {
            ResultSet resultSet = statement.executeQuery("Select * from Teacher where Teacher.teacherId=" + id);

            if (resultSet.next()) {
                Teacher teacher = new Teacher(resultSet.getLong("teacherId"), resultSet.getString("name"), resultSet.getString("surname"));
                try {
                    ResultSet courseIds = statement.executeQuery("Select courseId from Course where Course.teacher=" + teacher.getTeacherId());
                    List<Long> courses = new ArrayList<>();
                    while (courseIds.next()) {
                        courses.add(courseIds.getLong("courseId"));
                    }
                    teacher.setCourses(courses);
                } catch (SQLException ignored) {
                }
                return teacher;
            }
        } catch (SQLException ignored) {
        }
        return null;
    }

    /**
     * Loads all teachers from the database
     */
    public List<Teacher> databaseReadAll() {
        List<Teacher> teachers = new ArrayList<>();

        try {
            ResultSet resultSet = statement.executeQuery("Select * from Teacher");

            while (resultSet.next()) {
                Teacher teacher = new Teacher(resultSet.getLong("teacherId"), resultSet.getString("name"), resultSet.getString("surname"));
                teachers.add(teacher);
            }
            teachers.forEach(teacher -> {
                ResultSet courseIds;
                try {
                    courseIds = statement.executeQuery("Select courseId from Course where Course.teacher=" + teacher.getTeacherId());
                    List<Long> courses = new ArrayList<>();
                    while (courseIds.next()) {
                        courses.add(courseIds.getLong("courseId"));
                    }
                    teacher.setCourses(courses);
                } catch (SQLException ignored) {
                }
            });
        } catch (SQLException ignored) {
        }

        return teachers;
    }

    /**
     * Saves a teacher to the database
     *
     * @param teacher to be saved
     * @throws Exception if sql commands fail
     */
    public void databaseSave(Teacher teacher) throws Exception {
        statement.execute("Insert into Teacher values (" + teacher.getTeacherId() + ",'" + teacher.getFirstName() + "','" + teacher.getLastName() + "')");
    }

    /**
     * Deletes a teacher from the database
     *
     * @param id id of the student to be deleted
     * @throws Exception if sql commands fail
     */
    public void databaseDelete(Long id) throws Exception {
        statement.execute("Delete from Teacher where Teacher.teacherId=" + id);
    }

    /**
     * Updates a teacher in the database
     *
     * @param teacher to be updated
     * @throws Exception if sql commands fail
     */
    public void databaseUpdate(Teacher teacher) throws Exception {
        statement.execute("Delete from Teacher where Teacher.teacherId=" + teacher.getTeacherId());
        statement.execute("Insert into Teacher values (" + teacher.getTeacherId() + ",'" + teacher.getFirstName() + "','" + teacher.getLastName() + "')");
    }
}
