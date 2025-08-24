package mg.ticketing.DAO;

import mg.ticketing.model.Avion;
import mg.ticketing.model.Siege;
import mg.ticketing.model.Vol;
import mg.ticketing.util.DatabaseConnection;

import java.util.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VolDAO {
    private final ReservationDAO reservationDAO=new ReservationDAO();

    public List<Vol> findAll() throws SQLException {
        System.out.println("findAll Vol ...");
        List<Vol> vols = new ArrayList<>();
        String sql = "SELECT * FROM TICKETING_VOL ORDER BY DATE_HEURE_DEPART";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                vols.add(mapResultSetToVol(rs));
            }
        }
        return vols;
    }

    public Vol findById(Long id) throws SQLException {
        String sql = "SELECT * FROM TICKETING_VOL WHERE ID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToVol(rs);
            }
            return null;
        }
    }

    public void create(Vol vol) throws SQLException {
        String sql = "INSERT INTO TICKETING_VOL (AVION_ID, VILLE_DEPART_ID, VILLE_ARRIVEE_ID, "
                   + "DATE_HEURE_DEPART) "
                   + "VALUES (  ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            setVolParameters(stmt, vol);
            stmt.executeUpdate();
            
        
        }
    }

    public void update(Vol vol) throws SQLException {
        String sql = "UPDATE TICKETING_VOL SET AVION_ID = ?, VILLE_DEPART_ID = ?, VILLE_ARRIVEE_ID = ?, "
                   + "DATE_HEURE_DEPART = ? "
                   + " WHERE ID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            setVolParameters(stmt, vol);
            stmt.setLong(5, vol.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM TICKETING_VOL WHERE ID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    private Vol mapResultSetToVol(ResultSet rs) throws SQLException {
        Vol vol = new Vol();
        vol.setId(rs.getLong("ID"));
        vol.setAvionId(rs.getLong("AVION_ID"));
        vol.setVilleDepartId(rs.getLong("VILLE_DEPART_ID"));
        vol.setVilleArriveeId(rs.getLong("VILLE_ARRIVEE_ID"));
        vol.setDateHeureDepart(rs.getTimestamp("DATE_HEURE_DEPART").toLocalDateTime());
       
        return vol;
    }

    public Integer getReservationCountWithClasse(Long volId , Long classeId) throws SQLException{
         return (int) reservationDAO.findAllByVolId(volId)
                .stream()
                .filter(r -> classeId.equals(r.getClasseId()))
                .filter(r -> r.getStatut() != null && !"ANNULEE".equalsIgnoreCase(r.getStatut().getNom()))
                .count();
    }

    public Integer getPlaceDispo(Long volId, Long classeId) throws SQLException {
        Vol vol = this.findById(volId);
        Avion avion = vol.getAvion();

        Siege siegeClasse = avion.getSieges()
                .stream()
                .filter(s -> classeId.equals(s.getClasseId()))
                .findFirst()
                .orElse(null);

        if (siegeClasse == null) {
            return 0; 
        }
        int nbrSiegeDispo = siegeClasse.getNbr() - this.getReservationCountWithClasse(volId , classeId);
        return Math.max(nbrSiegeDispo, 0); 
    }

    private void setVolParameters(PreparedStatement stmt, Vol vol ) throws SQLException {
        stmt.setLong(1, vol.getAvionId());
        stmt.setLong(2, vol.getVilleDepartId());
        stmt.setLong(3, vol.getVilleArriveeId());
        stmt.setTimestamp(4, Timestamp.valueOf(vol.getDateHeureDepart()));
       
      
    }

  public List<Vol> criteria(Long avionId, Long villeDepartId, Long villeArriveeId, Date date1, Date date2) throws SQLException {
    List<Vol> vols = this.findAll();

    return vols.stream()
            .filter(v -> avionId == null || avionId.equals(v.getAvionId()))
            .filter(v -> villeDepartId == null || villeDepartId.equals(v.getVilleDepartId()))
            .filter(v -> villeArriveeId == null || villeArriveeId.equals(v.getVilleArriveeId()))
            .filter(v -> {
                Date departDate = v.getDateDepartAsDate();
                if (date1 != null && departDate.before(date1)) return false;
                if (date2 != null && departDate.after(date2)) return false;
                return true;
            })
            .collect(Collectors.toList());
}

}