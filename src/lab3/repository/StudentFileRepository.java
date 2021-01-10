package lab3.repository;
import lab3.model.Student;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StudentFileRepository implements ICrudRepository<Student> {

    private List<Student> students = new ArrayList<>();
    private File file;

    /**
     * Default constructor
     *
     * @throws Exception if the file cannot be opened
     */
    public StudentFileRepository() throws Exception {
        this.file = new File("files/students.txt");
        this.readFromFile();
    }

    /**
     * @param id -the id of the entity to be returned id must not be null
     * @return the entity with the specified id or null - if there is no entity with the given id
     */
    @Override
    public Student findOne(Long id) {
        for (Student student : this.students) {
            if (student.getStudentId() == id)
                return student;
        }
        return null;
    }

    /**
     * @return all entities
     */
    @Override
    public Iterable<Student> findAll() {
        return this.students;
    }

    /**
     * @param entity entity must be not null
     * @return null- if the given entity is saved otherwise returns the entity (id already exists)
     * @throws Exception if file i/o fails
     */
    @Override
    public Student save(Student entity) throws Exception {
        for (Student student : this.students) {
            if (student.getStudentId() == (entity).getStudentId())
                return entity;
        }
        this.students.add(entity);
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
    public Student delete(Long id) throws Exception {
        for (Student student : this.students) {
            if (student.getStudentId() == id) {
                this.students.remove(student);
                this.saveToFile();
                return student;
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
    public Student update(Student entity) throws Exception {
        for (int i = 0; i < this.students.size(); i++) {
            if (students.get(i).getStudentId() == (entity).getStudentId()) {
                this.students.set(i, entity);
                this.saveToFile();
                return null;
            }
        }
        return entity;
    }

    /**
     * saves all students to a file
     *
     * @throws Exception if the file path is invalid
     */
    private void saveToFile() throws Exception {
        PrintWriter output = new PrintWriter(file);
        for (Student student : this.students) {
            output.write(student.getStudentId() + ";"
                    + student.getFirstName() + ";"
                    + student.getLastName() + ";"
                    + student.getTotalCredits() + ";");
            if (student.getEnrolledCourses().size() == 0) {
                output.write("-");
            } else {
                for (long course : student.getEnrolledCourses()) {
                    output.write(course + ",");
                }
            }
            output.write("\n");
        }
        output.close();
    }

    /**
     * Loads student information from a file
     *
     * @throws Exception if the file path is invalid
     */
    private void readFromFile() throws Exception {
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String[] studentData = scanner.nextLine().split(";");

            Student student = new Student(Long.parseLong(studentData[0]), studentData[1], studentData[2]);
            student.setTotalCredits(Integer.parseInt(studentData[3]));

            if (studentData[4].compareTo("-") != 0) {
                String[] courseData = studentData[4].split(",");
                List<Long> enrolledCourses = new ArrayList<>();
                for (String course : courseData) {
                    enrolledCourses.add(Long.parseLong(course));
                }
                student.setEnrolledCourses(enrolledCourses);
            }

            this.save(student);
        }
        scanner.close();
    }
}
