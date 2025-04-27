package msu.ru.webprac.dao;

import java.io.Serializable;
import java.util.List;

public interface BasicDAO<T, I extends Number> {
    public T getById(I id);

    public List<T> getAll();

    public I getMaxId();

    public void insert(T elem);

    public void delete(I id);

    public void update(I id, T elem);
}
