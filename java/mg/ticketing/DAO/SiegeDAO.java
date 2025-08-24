package mg.ticketing.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mg.ticketing.model.Siege;
import mg.ticketing.util.DatabaseConnection;

public class SiegeDAO {
     public List<Siege> findByAvionId(Long avionId) throws SQLException {
        List<Siege> Sieges = new ArrayList<>();
        String sql = "SELECT * FROM TICKETING_SIEGE WHERE AVION_ID=?";

        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql);
            ) {
            ps.setLong(1, avionId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Siege Siege = mapResultSet(rs);
                Sieges.add(Siege);
            }
        }
        return Sieges;
    }
    

    public Siege mapResultSet(ResultSet rs) throws SQLException{
        Siege Siege=new Siege();
        Siege.setId(rs.getLong("ID"));
        Siege.setAvionId(rs.getLong("AVION_ID"));
        Siege.setNbr(rs.getInt("NBR"));
        Siege.setClasseId(rs.getLong("CLASSE_ID"));
        Siege.setPrix(rs.getDouble("PRIX"));
        return Siege;
    }   

}
