package mg.ticketing.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import mg.ticketing.model.Utilisateur;
import mg.ticketing.util.DatabaseConnection;

public class UtilisateurDAO {
    public Utilisateur findById(Long id) throws SQLException {
        String sql = "SELECT * FROM TICKETING_UTILISATEUR WHERE ID=?";

        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql);
            ) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return mapResultSet(rs);
            }
        }
        return null;
    }
    public Utilisateur mapResultSet(ResultSet rs) throws SQLException{
        Utilisateur Utilisateur=new Utilisateur();
        Utilisateur.setId(rs.getLong("ID"));
        Utilisateur.setDtn(rs.getDate("DTN"));
        Utilisateur.setNom(rs.getString("NOM"));
        Utilisateur.setPrenom(rs.getString("PRENOM"));
        Utilisateur.setPassword(rs.getString("PASSWORD"));
        Utilisateur.setUsername(rs.getString("USERNAME"));

        return Utilisateur;
    }   

}
