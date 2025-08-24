package mg.ticketing.DAO;

import mg.ticketing.model.Ville;
import mg.ticketing.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class VilleDAO {

    public List<Ville> findAll() throws SQLException {
        List<Ville> villes = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT ID, NOM FROM TICKETING_VILLE")) {
            while (rs.next()) {
                Ville ville = new Ville();
                ville.setId(rs.getLong("ID"));
                ville.setNom(rs.getString("NOM"));
                villes.add(ville);
            }
        }
        return villes;
    }
     public Ville findById(Long id) throws SQLException {
        String query="SELECT * FROM TICKETING_VILLE WHERE ID=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)
            ) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Ville ville = new Ville();
                ville.setId(rs.getLong("ID"));
                ville.setNom(rs.getString("NOM"));
                return ville;
            }
        }
        return null;
    }
}