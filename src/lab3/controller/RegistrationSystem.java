package lab3.controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lab3.exceptions.*;
import lab3.model.Course;
import lab3.model.Student;
import lab3.model.Teacher;
import lab3.repository.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class RegistrationSystem {

    private ICrudRepository<Course> courseRepository;
    private ICrudRepository<Student> studentRepository;
    private ICrudRepository<Teacher> teacherRepository;

    /**
     * Constructor
     *
     * @throws Exception if file i/o fails
     */
    public RegistrationSystem() throws Exception {
        this.courseRepository = new CourseJDBCRepository();
        this.teacherRepository = new TeacherJDBCRepository();
        this.studentRepository = new StudentJDBCRepository();
    }

    /**
     * Checks if a teacher exists
     *
     * @param teacherId teacher ID
     * @param firstName first name
     * @param lastName  last name
     * @return true if found, false otherwise
     */
    public boolean checkTeacher(long teacherId, String firstName, String lastName) {
        Teacher teacher = this.teacherRepository.findOne(teacherId);
        if (teacher != null) {
            return teacher.getFirstName().equals(firstName) && teacher.getLastName().equals(lastName);
        }
        return false;
    }

    /**
     * Checks if a student exists
     *
     * @param studentId student ID
     * @param firstName first name
     * @param lastName  last name
     * @return true if found, false otherwise
     */
    public boolean checkStudent(long studentId, String firstName, String lastName) {
        Student student = this.studentRepository.findOne(studentId);
        if (student != null) {
            return student.getFirstName().equals(firstName) && student.getLastName().equals(lastName);
        }
        return false;
    }

    /**
     * returns a specific student's credits
     * @param studentId student ID
     * @return Credit count, or 0 if the student Id is invalid
     */
    public int getCredits(long studentId){
        try {
            return this.studentRepository.findOne(studentId).getTotalCredits();
        }catch(Exception ex){
            return 0;
        }
    }

    /**
     * Creates an observable list of all courses
     *
     * @return list of courses
     */
    public ObservableList<Course> observableCourses() {
        ObservableList<Course> courses = FXCollections.observableArrayList();
        this.courseRepository.findAll().forEach(courses::add);
        return courses;
    }

    /**
     * Creates an observable list of a teacher's students
     *
     * @param id teacher ID
     * @return list of students
     */
    public ObservableList<Student> observableStudents(long id) {
        Teacher teacher = this.teacherRepository.findOne(id);
        ObservableList<Student> students = FXCollections.observableArrayList();
        this.studentRepository.findAll().forEach(student -> {
            AtomicBoolean intersection = new AtomicBoolean(false);
            student.getEnrolledCourses().forEach(course -> {
                if (teacher.getCourses().contains(course))
                    intersection.set(true);
            });
            if (intersection.get())
                students.add(student);
        });
        return students;
    }

    /**
     * Creates and inserts a new course
     *
     * @param courseId      course ID
     * @param name          name
     * @param maxEnrollment maximum enrollment
     * @param credits       credits
     * @throws Exception if file i/o fails
     */
    public void addCourse(long courseId, String name, int maxEnrollment, int credits) throws Exception {
        if (courseId >= 0) {
            Course course = new Course(courseId, name, maxEnrollment, credits);
            if (this.courseRepository.save(course) == course)
                throw new IDInUseException("Id is already in use.");
        } else
            throw new IDInvalidException("Invalid id.");
    }

    /**
     * Creates and inserts a new student
     *
     * @param studentId student ID
     * @param firstName first name
     * @param lastName  last name
     * @throws Exception if file i/o fails
     */
    public void addStudent(long studentId, String firstName, String lastName) throws Exception {
        if (studentId >= 0) {
            Student student = new Student(studentId, firstName, lastName);
            if (this.studentRepository.save(student) == student)
                throw new IDInUseException("Id is already in use.");
        } else
            throw new IDInvalidException("Invalid id.");
    }

    /**
     * Creates and inserts a new teacher
     *
     * @param teacherId teacher ID
     * @param firstName first name
     * @param lastName  last name
     * @throws Exception if file i/o fails
     */
    public void addTeacher(long teacherId, String firstName, String lastName) throws Exception {
        if (teacherId >= 0) {
            Teacher teacher = new Teacher(teacherId, firstName, lastName);
            if (this.teacherRepository.save(teacher) == teacher)
                throw new IDInUseException("Id is already in use.");
        } else
            throw new IDInvalidException("Invalid id.");
    }

    /**
     * Displays all courses
     */
    public List<String> showAllCourses() {
        List<String> courses = new ArrayList<>();
        courseRepository.findAll().forEach(course -> {
            if (course.getMaxEnrollment() - course.getStudentsEnrolled().size() < 0)
                throw new InvalidDataException("Invalid number of enrolled students.");
            courses.add(course.getCourseId() + " : " + course.getName()
                    + " | Credits: " + course.getCredits()
                    + " | Maximum enrollment: " + course.getMaxEnrollment()
                    + " | Available places: " + (course.getMaxEnrollment() - course.getStudentsEnrolled().size()));
        });
        return courses;
    }

    /**
     * Displays courses that are not fully enrolled
     */
    public List<String> showAvailableCourses() {
        List<String> courses = new ArrayList<>();
        courseRepository.findAll().forEach(course -> {
            if (course.getMaxEnrollment() - course.getStudentsEnrolled().size() < 0)
                throw new InvalidDataException("Invalid number of enrolled students.");
            if (course.getMaxEnrollment() - course.getStudentsEnrolled().size() > 0)
                courses.add(course.getCourseId() + " : " + course.getName()
                        + " | Credits: " + course.getCredits()
                        + " | Maximum enrollment: " + course.getMaxEnrollment()
                        + " | Available places: " + (course.getMaxEnrollment() - course.getStudentsEnrolled().size()));
        });
        return courses;
    }

    /**
     * Displays all teachers
     */
    public List<String> showAllTeachers() {
        List<String> teachers = new ArrayList<>();
        teacherRepository.findAll().forEach(teacher -> teachers.add(teacher.getTeacherId() + " : " + teacher.getFirstName() + " " + teacher.getLastName()));
        return teachers;
    }

    /**
     * Displays all students
     */
    public List<String> showAllStudents() {
        List<String> students = new ArrayList<>();
        studentRepository.findAll().forEach(student -> students.add(student.getStudentId() + " : "
                + student.getFirstName() + " "
                + student.getLastName() + " | Credits: "
                + student.getTotalCredits()));
        return students;
    }

    /**
     * Displays all students enrolled in a certain course
     *
     * @param courseId course ID
     */
    public List<String> showEnrolledStudents(long courseId) {
        List<String> students = new ArrayList<>();
        Course course = courseRepository.findOne(courseId);
        if (course != null) {
            course.getStudentsEnrolled().forEach(studentId -> {
                if (studentRepository.findOne(studentId) != null)
                    students.add((studentRepository.findOne(studentId)).getFirstName() + " "
                            + (studentRepository.findOne(studentId)).getLastName());
            });
        }
        return students;
    }

    public List<String> showSortedStudents() {
        List<String> students = new ArrayList<>();
        studentRepository.findAll().forEach(student -> students.add(student.getStudentId() + " : "
                + student.getFirstName() + " "
                + student.getLastName() + " | Credits: "
                + student.getTotalCredits()));

        students.sort((o1, o2) -> {
            String[] split1 = o1.split(": ");
            String[] split2 = o2.split(": ");
            int credits1 = Integer.parseInt(split1[split1.length - 1]);
            int credits2 = Integer.parseInt(split2[split2.length - 1]);
            return Integer.compare(credits2, credits1);
        });

        return students;
    }

    /**
     * Assigns a teacher to a course
     *
     * @param teacherId teacher ID
     * @param courseId  course ID
     * @throws Exception if file i/o fails
     */
    public void assignTeacherToCourse(long teacherId, long courseId) throws Exception {
        Teacher teacher = teacherRepository.findOne(teacherId);
        Course course = courseRepository.findOne(courseId);
        if (teacher != null && course != null) {
            if (teacher.getCourses().contains(courseId))
                throw new AssignationException("This course is already being taught by this teacher.");
            else if (course.getTeacher() != -1)
                throw new AssignationException("This course is already being taught by another teacher.");
            else {
                course.setTeacher((int) teacherId);
                List<Long> courses = teacher.getCourses();
                courses.add(courseId);
                teacher.setCourses(courses);

                this.teacherRepository.update(teacher);
                this.courseRepository.update(course);
            }

        } else {
            throw new IDInvalidException("Invalid course or teacher ID.");
        }
    }

    /**
     * Removes a teacher from a course
     *
     * @param courseId course ID
     * @throws Exception if file i/o fails
     */
    public void removeTeacherFromCourse(long courseId) throws Exception {
        Course course = courseRepository.findOne(courseId);
        if (course != null) {
            long teacherId = course.getTeacher();
            Teacher teacher = teacherRepository.findOne(teacherId);
            if (teacher != null) {
                List<Long> courses = teacher.getCourses();
                courses.remove(courseId);
                teacher.setCourses(courses);
                teacherRepository.update(teacher);
            } else {
                throw new AssignationException("No teacher is assigned to this course");
            }
            course.setTeacher(-1);
            courseRepository.update(course);
        } else {
            throw new IDInvalidException("Invalid course ID.");
        }
    }

    /**
     * Assigns a student to a course
     *
     * @param studentId student ID
     * @param courseId  course ID
     * @throws Exception if file i/o fails
     */
    public void assignStudentToCourse(long studentId, long courseId) throws Exception {
        Student student = studentRepository.findOne(studentId);
        Course course = courseRepository.findOne(courseId);
        if (student != null && course != null) {
            if (student.getEnrolledCourses().contains(courseId) || course.getStudentsEnrolled().contains(studentId))
                throw new AssignationException("This student is already enrolled in this course.");
            else if (course.getCredits() + student.getTotalCredits() > 30)
                throw new AssignationException("The total credit count is too high.");
            else if (course.getMaxEnrollment() - course.getStudentsEnrolled().size() == 0)
                throw new AssignationException("This course is already fully enrolled.");
            else {
                List<Long> courses = student.getEnrolledCourses();
                courses.add(courseId);
                student.setEnrolledCourses(courses);

                List<Long> students = course.getStudentsEnrolled();
                students.add(studentId);
                course.setStudentsEnrolled(students);

                student.setTotalCredits(student.getTotalCredits() + course.getCredits());

                this.studentRepository.update(student);
                this.courseRepository.update(course);
            }

        } else {
            throw new IDInvalidException("Invalid course or student ID.");
        }
    }

    /**
     * Removes a student from a course
     *
     * @param studentId student ID
     * @param courseId  course ID
     * @throws Exception if file i/o fails
     */
    public void removeStudentFromCourse(long studentId, long courseId) throws Exception {
        Student student = studentRepository.findOne(studentId);
        Course course = courseRepository.findOne(courseId);
        if (student != null && course != null) {
            if (!student.getEnrolledCourses().contains(courseId))
                throw new AssignationException("This student is not enrolled in this course.");
            else {
                List<Long> courses = student.getEnrolledCourses();
                courses.remove(courseId);
                student.setEnrolledCourses(courses);

                List<Long> students = course.getStudentsEnrolled();
                students.remove(studentId);
                course.setStudentsEnrolled(students);

                student.setTotalCredits(student.getTotalCredits() - course.getCredits());

                this.studentRepository.update(student);
                this.courseRepository.update(course);
            }

        } else {
            throw new IDInvalidException("Invalid course or student ID.");
        }
    }

    /**
     * Changes a course's credits
     *
     * @param courseId course ID
     * @param credits  new credit count
     * @throws Exception if file i/o fails
     */
    public void changeCredits(long courseId, int credits) throws Exception {
        if (credits >= 0) {
            Course course = courseRepository.findOne(courseId);
            if (course != null) {

                int delta = credits - course.getCredits();
                final int[] numberOfStudents = {course.getStudentsEnrolled().size()};
                final Long[][] studentsEnrolledArray = {new Long[numberOfStudents[0]]};
                studentsEnrolledArray[0] = course.getStudentsEnrolled().toArray(studentsEnrolledArray[0]);

                course.getStudentsEnrolled().forEach(studentId -> {
                    Student student = studentRepository.findOne(studentId);
                    if (student == null)
                        throw new InvalidDataException("Student with invalid ID enrolled in course.");
                    else {
                        student.setTotalCredits(student.getTotalCredits() + delta);
                        if (student.getTotalCredits() > 30) {
                            student.setTotalCredits(student.getTotalCredits() - credits);
                            List<Long> enrolledCourses = student.getEnrolledCourses();
                            enrolledCourses.remove(courseId);
                            student.setEnrolledCourses(enrolledCourses);

                            for (int i = 0; i < numberOfStudents[0]; i++) {
                                if (studentsEnrolledArray[0][i].equals(studentId)) {
                                    if (numberOfStudents[0] - 1 - i >= 0)
                                        System.arraycopy(studentsEnrolledArray[0], i + 1, studentsEnrolledArray[0], i, numberOfStudents[0] - 1 - i);
                                    numberOfStudents[0]--;
                                }
                            }
                        }
                        try {
                            studentRepository.update(student);
                        } catch (Exception ignored) {
                        }
                    }
                });

                List<Long> studentsEnrolled = new ArrayList<>(Arrays.asList(studentsEnrolledArray[0]).subList(0, numberOfStudents[0]));

                course.setStudentsEnrolled(studentsEnrolled);
                course.setCredits(credits);
                courseRepository.update(course);
            } else {
                throw new IDInvalidException("Invalid course ID.");
            }
        } else {
            throw new InvalidDataException("Negative credit count is not permitted.");
        }
    }

    /**
     * Deletes a course
     *
     * @param courseId course ID
     * @throws Exception if file i/o fails
     */
    public void deleteCourse(long courseId) throws Exception {
        Course course = courseRepository.findOne(courseId);
        if (course != null) {
            course.getStudentsEnrolled().forEach(studentId -> {
                Student student = studentRepository.findOne(studentId);
                if (student == null)
                    throw new InvalidDataException("Student with invalid ID enrolled in course.");
                else {
                    List<Long> enrolledCourses = student.getEnrolledCourses();
                    enrolledCourses.remove(courseId);
                    student.setEnrolledCourses(enrolledCourses);
                    student.setTotalCredits(student.getTotalCredits() - course.getCredits());
                }
                try {
                    studentRepository.update(student);
                } catch (Exception ignored) {
                }
            });
            courseRepository.delete(courseId);
        } else {
            throw new IDInvalidException("Invalid course ID.");
        }
    }

    /**
     * Deletes a teacher
     *
     * @param teacherId teacher ID
     * @throws Exception if file i/o fails
     */
    public void deleteTeacher(long teacherId) throws Exception {
        Teacher teacher = teacherRepository.findOne(teacherId);
        if (teacher != null) {
            teacher.getCourses().forEach(courseId -> {
                Course course = courseRepository.findOne(courseId);
                if (course != null) {
                    course.setTeacher(-1);
                    try {
                        courseRepository.update(course);
                    } catch (Exception ignored) {
                    }
                } else {
                    throw new InvalidDataException("Teacher assigned to non-existent course.");
                }
            });
            teacherRepository.delete(teacherId);
        } else {
            throw new IDInvalidException("Invalid teacher ID.");
        }
    }

    /**
     * Deletes a student
     *
     * @param studentId student ID
     * @throws Exception if file i/o fails
     */
    public void deleteStudent(long studentId) throws Exception {
        Student student = studentRepository.findOne(studentId);
        if (student != null) {
            student.getEnrolledCourses().forEach(courseId -> {
                Course course = courseRepository.findOne(courseId);
                if (course != null) {
                    List<Long> studentsEnrolled = course.getStudentsEnrolled();
                    studentsEnrolled.remove(studentId);
                    course.setStudentsEnrolled(studentsEnrolled);
                    try {
                        courseRepository.update(course);
                    } catch (Exception ignored) {
                    }
                } else {
                    throw new InvalidDataException("Student assigned to non-existent course.");
                }
            });
            studentRepository.delete(studentId);
        } else {
            throw new IDInvalidException("Invalid student ID.");
        }
    }
}