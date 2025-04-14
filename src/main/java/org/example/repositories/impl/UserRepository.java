package org.example.repositories.impl;

import org.example.config.DatabaseConnection;
import org.example.model.entities.LoanEntity;
import org.example.model.entities.UserEntity;
import org.example.repositories.interfaces.IRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository implements IRepository<UserEntity> {
    private static UserRepository instance;
    public static UserRepository getInstance() {
        if(instance == null) instance = new UserRepository();
        return instance;
    }
    private Optional<UserEntity> resultToUser(ResultSet rs) throws SQLException {
        return Optional.of(UserEntity.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("nombre"))
                .email(rs.getString("email"))
                .build());
    }

    @Override
    public List<UserEntity> findAll() throws SQLException {
        String sql = "SELECT * FROM usuarios";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<UserEntity> users = new ArrayList<>();
            while(rs.next()) {
                Optional<UserEntity> userOpt = resultToUser(rs);
                userOpt.ifPresent(users::add);
            }
            return users;
        }
    }

    @Override
    public Optional<UserEntity> findByID(Integer id) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return resultToUser(rs);
            }
        }
        return Optional.empty();
    }

    @Override
    public Integer count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuarios";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if(rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    @Override
    public void save(UserEntity user) throws SQLException {
        String sql = "INSERT INTO usuarios (nombre, email) " +
                "VALUES (?, ?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.executeUpdate();
        }
    }

    @Override
    public void deleteByID(Integer id) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
