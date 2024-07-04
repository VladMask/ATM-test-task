package org.example.repository;

public interface CrudRepository<E, ID> {
    void insert(E entity);
    void update(E entity);
    void delete(E entity);
    E findById(ID id);
}
