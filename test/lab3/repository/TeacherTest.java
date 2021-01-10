package lab3.repository;
import lab3.model.Teacher;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class TeacherTest {

    private Teacher teacher1=new Teacher(1,"Brigitte","Breckner");
    private Teacher teacher2=new Teacher(2,"Catalin","Rusu");
    private Teacher teacher3=new Teacher(3,"Christian","Sacarea");
    private Teacher teacher4=new Teacher(4,"Gabriel","Mircea");

    @Test
    public void testFindOne() {
        TeacherRepository repository=new TeacherRepository();

        repository.save(this.teacher1);
        repository.save(this.teacher3);

        Assert.assertEquals(repository.findOne((long)1),this.teacher1);
        Assert.assertEquals(repository.findOne((long)3),this.teacher3);
        Assert.assertNull(repository.findOne((long)2));
        Assert.assertNull(repository.findOne((long)4));

        repository.delete((long)1);
        repository.delete((long)3);
    }

    @Test
    public void testFindAll() {
        TeacherRepository repository=new TeacherRepository();

        repository.save(this.teacher1);
        repository.save(this.teacher2);
        repository.save(this.teacher3);
        repository.save(this.teacher4);

        List<Teacher> result=new ArrayList<>();
        result.add(this.teacher1);
        result.add(this.teacher2);
        result.add(this.teacher3);
        result.add(this.teacher4);

        Assert.assertEquals(repository.findAll(),result);

        repository.delete((long)1);
        repository.delete((long)2);
        repository.delete((long)3);
        repository.delete((long)4);
    }

    @Test
    public void testSave(){
        TeacherRepository repository=new TeacherRepository();

        Assert.assertNull(repository.save(this.teacher1));
        Assert.assertEquals(repository.save(this.teacher1),this.teacher1);
        Assert.assertEquals(repository.findOne((long)1),this.teacher1);

        repository.delete((long)1);
    }

    @Test
    public void testDelete(){
        TeacherRepository repository=new TeacherRepository();

        repository.save(this.teacher1);

        Assert.assertEquals(repository.delete((long)1),this.teacher1);
        Assert.assertNull(repository.findOne((long)1));
    }

    @Test
    public void testUpdate(){
        TeacherRepository repository=new TeacherRepository();

        repository.save(this.teacher4);
        this.teacher4.setFirstName("Gabi");

        Assert.assertNull(repository.update(this.teacher4));
        Assert.assertEquals(repository.update(this.teacher1),this.teacher1);

        repository.delete((long)1);
    }
}
