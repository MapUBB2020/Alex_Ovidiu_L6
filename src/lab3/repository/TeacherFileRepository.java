package lab3.repository;
import lab3.model.Teacher;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TeacherFileRepository implements ICrudRepository<Teacher> {

    private List<Teacher> teachers = new ArrayList<>();
    private File file;

    /**
     * Default constructor
     *
     * @throws Exception if the file cannot be opened
     */
    public TeacherFileRepository() throws Exception {
        this.file = new File("files/teachers.txt");
        this.readFromFile();
    }

    /**
     * @param id -the id of the entity to be returned id must not be null
     * @return the entity with the specified id or null - if there is no entity with the given id
     */
    @Override
    public Teacher findOne(Long id) {
        for (Teacher teacher : this.teachers) {
            if (teacher.getTeacherId() == id)
                return teacher;
        }
        return null;
    }

    /**
     * @return all entities
     */
    @Override
    public Iterable<Teacher> findAll() {
        return this.teachers;
    }

    /**
     * @param entity entity must be not null
     * @return null- if the given entity is saved otherwise returns the entity (id already exists)
     * @throws Exception if file i/o fails
     */
    @Override
    public Teacher save(Teacher entity) throws Exception {
        for (Teacher teacher : this.teachers) {
            if (teacher.getTeacherId() == (entity).getTeacherId())
                return entity;
        }
        this.teachers.add(entity);
        this.saveToFile();
        return null;
    }

    /**
     * removes the entity with the specified id
     *
     * @param id id must be not null
     * @return the removed entity or null if there is no entity with the given id
     * @throws Exception if file i/o fails
     */
    @Override
    public Teacher delete(Long id) throws Exception {
        for (Teacher teacher : this.teachers) {
            if (teacher.getTeacherId() == id) {
                this.teachers.remove(teacher);
                this.saveToFile();
                return teacher;
            }
        }
        return null;
    }

    /**
     * @param entity entity must not be null
     * @return null - if the entity is updated, otherwise returns the entity - (e.g id does not exist).
     * @throws Exception if file i/o fails
     */
    @Override
    public Teacher update(Teacher entity) throws Exception {
        for (int i = 0; i < this.teachers.size(); i++) {
            if (teachers.get(i).getTeacherId() == (entity).getTeacherId()) {
                this.teachers.set(i, entity);
                this.saveToFile();
                return null;
            }
        }
        return entity;
    }

    /**
     * saves all teachers to a file
     *
     * @throws Exception if the file path is invalid
     */
    private void saveToFile() throws Exception {
        PrintWriter output = new PrintWriter(file);
        for (Teacher teacher : this.teachers) {
            output.write(teacher.getTeacherId() + ";"
                    + teacher.getFirstName() + ";"
                    + teacher.getLastName() + ";");
            if (teacher.getCourses().size() == 0) {
                output.write("-");
            } else {
                for (long course : teacher.getCourses()) {
                    output.write(course + ",");
                }
            }
            output.write("\n");
        }
        output.close();
    }

    /**
     * Loads teacher information from a file
     *
     * @throws Exception if the file path is invalid
     */
    private void readFromFile() throws Exception {
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String[] teacherData = scanner.nextLine().split(";");

            Teacher teacher = new Teacher(Long.parseLong(teacherData[0]), teacherData[1], teacherData[2]);

            if (teacherData[3].compareTo("-") != 0) {
                String[] courseData = teacherData[3].split(",");
                List<Long> courses = new ArrayList<>();
                for (String course : courseData) {
                    courses.add(Long.parseLong(course));
                }
                teacher.setCourses(courses);
            }

            this.save(teacher);
        }
        scanner.close();
    }
}
