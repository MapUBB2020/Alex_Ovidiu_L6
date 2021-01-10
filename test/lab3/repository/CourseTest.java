package lab3.repository;
import lab3.model.Course;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class CourseTest {

    private Course course1=new Course(1,"mathematics",20,6);
    private Course course2=new Course(2,"computer science",20,5);
    private Course course3=new Course(3,"physics",20,7);
    private Course course4=new Course(4,"art",20,6);

    @Test
    public void testFindOne() {
        CourseRepository repository=new CourseRepository();

        repository.save(this.course1);
        repository.save(this.course3);

        Assert.assertEquals(repository.findOne((long)1),this.course1);
        Assert.assertEquals(repository.findOne((long)3),this.course3);
        Assert.assertNull(repository.findOne((long)2));
        Assert.assertNull(repository.findOne((long)4));

        repository.delete((long)1);
        repository.delete((long)3);
    }

    @Test
    public void testFindAll() {
        CourseRepository repository=new CourseRepository();

        repository.save(this.course1);
        repository.save(this.course2);
        repository.save(this.course3);
        repository.save(this.course4);

        List<Course> result=new ArrayList<>();
        result.add(this.course1);
        result.add(this.course2);
        result.add(this.course3);
        result.add(this.course4);

        Assert.assertEquals(repository.findAll(),result);

        repository.delete((long)1);
        repository.delete((long)2);
        repository.delete((long)3);
        repository.delete((long)4);
    }

    @Test
    public void testSave(){
        CourseRepository repository=new CourseRepository();

        Assert.assertNull(repository.save(this.course1));
        Assert.assertEquals(repository.save(this.course1),this.course1);
        Assert.assertEquals(repository.findOne((long)1),this.course1);

        repository.delete((long)1);
    }

    @Test
    public void testDelete(){
        CourseRepository repository=new CourseRepository();

        repository.save(this.course1);

        Assert.assertEquals(repository.delete((long)1),this.course1);
        Assert.assertNull(repository.findOne((long)1));
    }

    @Test
    public void testUpdate(){
        CourseRepository repository=new CourseRepository();

        repository.save(this.course1);
        this.course1.setCredits(5);
        this.course2.setMaxEnrollment(10);

        Assert.assertNull(repository.update(this.course1));
        Assert.assertEquals(repository.update(this.course4),this.course4);

        repository.delete((long)1);
    }
}
