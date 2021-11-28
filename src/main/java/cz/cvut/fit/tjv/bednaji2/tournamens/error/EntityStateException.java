package cz.cvut.fit.tjv.bednaji2.tournamens.error;

public class EntityStateException extends RuntimeException {
    public <E> EntityStateException(E entity) {
        super("Illegal state of entity " + entity);
    }
}
