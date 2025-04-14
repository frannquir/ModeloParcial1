package org.example.repositories.impl;

import lombok.Builder;
import org.example.config.DatabaseConnection;
import org.example.model.entities.BookEntity;
import org.example.repositories.interfaces.IRepository;

import java.awt.print.Book;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepository implements IRepository<BookEntity> {
    private static BookRepository instance;

    public static BookRepository getInstance() {
        if (instance == null) instance = new BookRepository();
        return instance;
    }

    private Optional<BookEntity> resultToBook(ResultSet rs) throws SQLException {
        return Optional.of(BookEntity.builder()
                .id(rs.getInt("id"))
                .title(rs.getString("titulo"))
                .author(rs.getString("autor"))
                .year(rs.getInt("anio_publicacion"))
                .stock(rs.getInt("unidades_disponibles"))
                .build());
    }

    @Override
    public List<BookEntity> findAll() throws SQLException {
        String sql = "SELECT * FROM libros";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<BookEntity> books = new ArrayList<>();
            while (rs.next()) {
                Optional<BookEntity> bookOpt = resultToBook(rs);
                bookOpt.ifPresent(books::add);
            }
            return books;
        }
    }

    @Override
    public Optional<BookEntity> findByID(Integer id) throws SQLException {
        String sql = "SELECT * FROM libros WHERE id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return resultToBook(rs);
            }
        }
        return Optional.empty();
    }

    @Override
    public Integer count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM libros";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    @Override
    public void save(BookEntity book) throws SQLException {
        String sql = "INSERT INTO libros (titulo, autor, anio_publicacion, unidades_disponibles)" +
                "VALUES (?, ?, ?, ?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setInt(3, book.getYear());
            ps.setInt(4, book.getStock());
            ps.executeUpdate();
        }

    }

    @Override
    public void deleteByID(Integer id) throws SQLException {
        String sql = "DELETE FROM libros WHERE id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
