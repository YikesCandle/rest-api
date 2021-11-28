package cz.cvut.fit.tjv.bednaji2.tournamens.business;

import cz.cvut.fit.tjv.bednaji2.tournamens.error.EntityStateException;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;


public abstract class AbstractCrudService<E, K, R extends JpaRepository<E, K>> {
    protected final R repository;

    protected AbstractCrudService(R repository) {
        this.repository = repository;
    }

    protected abstract boolean exists(E entity);

    public E findById(K id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found."));
    }

    public boolean existsById(K id) {return repository.existsById(id);
    }

    public List<E> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void update(E entity) throws EntityStateException {
        if (exists(entity))
            repository.save(entity);
        else
            throw new EntityStateException(entity);
    }

    public void deleteById(K id) {
        if (!existsById(id))
            throw new EntityNotFoundException("Entity not found.");
        repository.deleteById(id);
    }
}
