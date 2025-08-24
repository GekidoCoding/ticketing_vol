package mg.ticketing.DAO;

import mg.ticketing.model.Avion;
import mg.ticketing.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AvionDAO {

    public List<Avion> findAll() throws SQLException {
        List<Avion> avions = new ArrayList<>();
        String sql = "SELECT * FROM TICKETING_AVION ORDER BY MODELE";

        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Avion avion = new Avion();
                avion.setId(rs.getLong("ID"));
                avion.setModele(rs.getString("MODELE"));
                avion.setDateFabrication(rs.getDate("DATE_FABRICATION"));
                avion.setCompanyId(rs.getLong("COMPANY_ID"));
                avions.add(avion);
            }
        }
        return avions;
    }
    public Avion findById(Long id) throws SQLException {
        String sql = "SELECT * FROM TICKETING_AVION WHERE ID=?";

        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Avion avion = new Avion();
                avion.setId(rs.getLong("ID"));
                avion.setModele(rs.getString("MODELE"));
                avion.setDateFabrication(rs.getDate("DATE_FABRICATION"));
                avion.setCompanyId(rs.getLong("COMPANY_ID"));
                return avion;
            }
        }
        return null;
    }

}
