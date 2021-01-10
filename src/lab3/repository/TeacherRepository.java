package lab3.repository;
import lab3.model.Teacher;
import java.util.ArrayList;
import java.util.List;

public class TeacherRepository implements ICrudRepository<Teacher> {

    private List<Teacher> teachers = new ArrayList<>();

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
     */
    @Override
    public Teacher save(Teacher entity) {
        for (Teacher teacher : this.teachers) {
            if (teacher.getTeacherId() == (entity).getTeacherId())
                return entity;
        }
        this.teachers.add(entity);
        return null;
    }

    /**
     * removes the entity with the specified id
     *
     * @param id id must be not null
     * @return the removed entity or null if there is no entity with the given id
     */
    @Override
    public Teacher delete(Long id) {
        for (Teacher teacher : this.teachers) {
            if (teacher.getTeacherId() == id) {
                this.teachers.remove(teacher);
                return teacher;
            }
        }
        return null;
    }

    /**
     * @param entity entity must not be null
     * @return null - if the entity is updated, otherwise returns the entity - (e.g id does not exist).
     */
    @Override
    public Teacher update(Teacher entity) {
        for (int i = 0; i < this.teachers.size(); i++) {
            if (teachers.get(i).getTeacherId() == entity.getTeacherId()) {
                this.teachers.set(i, entity);
                return null;
            }
        }
        return entity;
    }
}
