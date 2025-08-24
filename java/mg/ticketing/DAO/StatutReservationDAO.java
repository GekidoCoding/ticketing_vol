package mg.ticketing.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import mg.ticketing.model.StatutReservation;
import mg.ticketing.util.DatabaseConnection;

public class StatutReservationDAO {
    public StatutReservation findById(Long id) throws SQLException {
        String sql = "SELECT * FROM TICKETING_STATUT_RESERVATION WHERE ID=?";

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
    public StatutReservation mapResultSet(ResultSet rs) throws SQLException{
        StatutReservation StatutReservation=new StatutReservation();
        StatutReservation.setId(rs.getLong("ID"));
        StatutReservation.setNom(rs.getString("NOM"));

        return StatutReservation;
    }   

}
