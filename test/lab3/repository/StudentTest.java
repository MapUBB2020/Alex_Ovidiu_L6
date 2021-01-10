package lab3.repository;
import lab3.model.Student;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class StudentTest {

    private Student student1=new Student(1,"Ovidiu","Muntean");
    private Student student2=new Student(2,"Stefan","Dobrescu");
    private Student student3=new Student(3,"Andreea","Coaja");
    private Student student4=new Student(4,"Teodora","Diaconu");

    @Test
    public void testFindOne() {
        StudentRepository repository=new StudentRepository();

        repository.save(this.student1);
        repository.save(this.student3);

        Assert.assertEquals(repository.findOne((long)1),this.student1);
        Assert.assertEquals(repository.findOne((long)3),this.student3);
        Assert.assertNull(repository.findOne((long)2));
        Assert.assertNull(repository.findOne((long)4));

        repository.delete((long)1);
        repository.delete((long)3);
    }

    @Test
    public void testFindAll() {
        StudentRepository repository=new StudentRepository();

        repository.save(this.student1);
        repository.save(this.student2);
        repository.save(this.student3);
        repository.save(this.student4);

        List<Student> result=new ArrayList<>();
        result.add(this.student1);
        result.add(this.student2);
        result.add(this.student3);
        result.add(this.student4);

        Assert.assertEquals(repository.findAll(),result);

        repository.delete((long)1);
        repository.delete((long)2);
        repository.delete((long)3);
        repository.delete((long)4);
    }

    @Test
    public void testSave(){
        StudentRepository repository=new StudentRepository();

        Assert.assertNull(repository.save(this.student1));
        Assert.assertEquals(repository.save(this.student1),this.student1);
        Assert.assertEquals(repository.findOne((long)1),this.student1);

        repository.delete((long)1);
    }

    @Test
    public void testDelete(){
        StudentRepository repository=new StudentRepository();

        repository.save(this.student1);

        Assert.assertEquals(repository.delete((long)1),this.student1);
        Assert.assertNull(repository.findOne((long)1));
    }

    @Test
    public void testUpdate(){
        StudentRepository repository=new StudentRepository();

        repository.save(this.student1);
        this.student1.setFirstName("Oridis");
        this.student1.setLastName("Oribil");

        Assert.assertNull(repository.update(this.student1));
        Assert.assertEquals(repository.update(this.student4),this.student4);

        repository.delete((long)1);
    }
}
