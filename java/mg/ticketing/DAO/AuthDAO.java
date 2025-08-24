package mg.ticketing.DAO;

import mg.ticketing.model.Admin;
import mg.ticketing.model.Utilisateur;
import mg.ticketing.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthDAO {

    public Utilisateur findUserByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM TICKETING_UTILISATEUR WHERE username = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Utilisateur user = new Utilisateur();
                user.setId(rs.getLong("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setNom(rs.getString("nom"));
                user.setNom(rs.getString("prenom"));
                user.setDtn(rs.getDate("dtn"));
                return user;
            }
            return null;
        }
    }

    public Admin findAdminByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM TICKETING_ADMIN WHERE username = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Admin admin = new Admin();
                admin.setId(rs.getLong("id"));
                admin.setUsername(rs.getString("username"));
                admin.setPassword(rs.getString("password"));
                return admin;
            }
            return null;
        }
    }

    public List<Utilisateur> findAllUsers() throws SQLException {
        List<Utilisateur> users = new ArrayList<>();
        String sql = "SELECT * FROM TICKETING_UTILISATEUR";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Utilisateur user = new Utilisateur();
                user.setId(rs.getLong("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                users.add(user);
            }
        }
        return users;
    }
}