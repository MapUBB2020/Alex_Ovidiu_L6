package lab3.repository;

/**
 * CRUD operations repository interface
 */
public interface ICrudRepository<E> {

    /**
     * @param id -the id of the entity to be returned id must not be null
     * @return the entity with the specified id or null - if there is no entity with the given id
     */
    E findOne(Long id);

    /**
     * @return all entities
     */
    Iterable<E> findAll();

    /**
     * @param entity entity must be not null
     * @return null- if the given entity is saved otherwise returns the entity (id already exists)
     * @throws Exception if file i/o fails
     */
    E save(E entity) throws Exception;

    /**
     * removes the entity with the specified id
     *
     * @param id id must be not null
     * @return the removed entity or null if there is no entity with the given id
     * @throws Exception if file i/o fails
     */
    E delete(Long id) throws Exception;

    /**
     * @param entity entity must not be null
     * @return null - if the entity is updated, otherwise returns the entity - (e.g id does not exist).
     * @throws Exception if file i/o fails
     */
    E update(E entity) throws Exception;
}
