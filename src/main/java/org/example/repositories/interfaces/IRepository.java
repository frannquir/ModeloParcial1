package org.example.repositories.interfaces;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IRepository<T> {
    public List<T> findAll() throws SQLException;
    public Optional<T> findByID(Integer id) throws SQLException;
    public Integer count() throws SQLException;
    public void save(T t) throws SQLException;
    public void deleteByID(Integer id) throws SQLException;
}
