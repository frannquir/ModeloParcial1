package org.example.repositories.impl;

import org.example.config.DatabaseConnection;
import org.example.model.entities.BookEntity;
import org.example.model.entities.LoanEntity;
import org.example.repositories.interfaces.IRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LoanRepository implements IRepository<LoanEntity> {
    private static LoanRepository instance;
    public static LoanRepository getInstance () {
        if(instance == null) instance = new LoanRepository();
        return instance;
    }
    private Optional<LoanEntity> resultToLoan(ResultSet rs) throws SQLException {
        LocalDate date = null;
        String dateString = rs.getString("fecha_devolucion");
        if(dateString != null)
            date = LocalDate.parse(dateString);
        return Optional.of(LoanEntity.builder()
                .bookId(rs.getInt("libro_id"))
                .userId(rs.getInt("usuario_id"))
                .dateLoan(LocalDate.parse(rs.getString("fecha_prestamo")))
                .datePaid(date)
                .build());
    }

    @Override
    public List<LoanEntity> findAll() throws SQLException {
        String sql = "SELECT * FROM prestamos";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<LoanEntity> loans = new ArrayList<>();
            while(rs.next()) {
                Optional<LoanEntity> loanOpt = resultToLoan(rs);
                loanOpt.ifPresent(loans::add);
            }
        }
        return List.of();
    }

    @Override
    public Optional<LoanEntity> findByID(Integer id) throws SQLException {
        String sql = "SELECT * FROM prestamos WHERE id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return resultToLoan(rs);
            }
        }
        return Optional.empty();
    }

    @Override
    public Integer count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM prestamos";
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
    public void save(LoanEntity loan) throws SQLException {
        String sql = "INSERT INTO prestamos (libro_id, usuario_id) " +
                "VALUES (?, ?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, loan.getBookId());
            ps.setInt(2, loan.getUserId());
            ps.executeUpdate();
        }

    }

    @Override
    public void deleteByID(Integer id) throws SQLException {
        String sql = "DELETE FROM prestamos WHERE id = ?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
