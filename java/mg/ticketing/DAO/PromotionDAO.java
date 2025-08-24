package mg.ticketing.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import mg.ticketing.model.Promotion;
import mg.ticketing.util.DatabaseConnection;

public class PromotionDAO {
    private final VolDAO volDAO = new VolDAO();
    
    public List<Promotion> findAll() throws SQLException {
        List<Promotion> promotions = new ArrayList<>();
        String sql = "SELECT * FROM TICKETING_PROMOTION";

        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Promotion promotion = mapResultSet(rs);
                promotions.add(promotion);
            }
        }
        return promotions;
    }

    public List<Promotion> findByVolId(Long volId) throws SQLException {
        List<Promotion> promotions = new ArrayList<>();
        String sql = "SELECT * FROM TICKETING_PROMOTION WHERE VOL_ID=? ORDER BY REDUCTION DESC";

        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql);
            ) {
                
            ps.setLong(1, volId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Promotion promotion = mapResultSet(rs);
                promotions.add(promotion);
            }
        }
        return promotions;
    }
      public Promotion findById(Long id) throws SQLException {
        String sql = "SELECT * FROM TICKETING_PROMOTION WHERE ID=?";

        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql);
            ) {
                
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Promotion promotion = mapResultSet(rs);
               return promotion;
            }
        }
        return null;
    }


    public void create(Promotion promotion) throws SQLException {
        String sql = "INSERT INTO TICKETING_PROMOTION "
                + "(VOL_ID, CLASSE_ID, NBR_SIEGE, REDUCTION, PRIX, DATE_BUTOIRE, NBR_SIEGE_DISPO) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            promotion.setNbrSiegeDispo(promotion.getNbrSiege());

            setPromotionParameters(stmt, promotion);

            stmt.executeUpdate();

        }
    }

    public void update(Promotion promotion) throws SQLException {
        String sql = "UPDATE TICKETING_PROMOTION SET "
                + "VOL_ID = ?, "
                + "CLASSE_ID = ?, "
                + "NBR_SIEGE = ?, "
                + "REDUCTION = ?, "
                + "PRIX = ?, "
                + "DATE_BUTOIRE = ?, "
                + "NBR_SIEGE_DISPO = ? "
                + "WHERE ID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            setPromotionParameters(stmt, promotion); 
            stmt.setLong(8, promotion.getId()); 

            stmt.executeUpdate();
        }
    }


    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM TICKETING_PROMOTION WHERE ID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
  
    public void setPromotionParameters(PreparedStatement stmt, Promotion promotion) throws SQLException {
        stmt.setLong(1, promotion.getVolId());
        stmt.setLong(2, promotion.getClasseId());
        stmt.setInt(3, promotion.getNbrSiege());
        stmt.setDouble(4, promotion.getReduction());
        stmt.setDouble(5, promotion.getPrix());
        stmt.setDate(6, promotion.getDateButoire());
        stmt.setInt(7, promotion.getNbrSiegeDispo());
    }
    
    public Promotion mapResultSet(ResultSet rs) throws SQLException{
        Promotion promotion=new Promotion();
        promotion.setId(rs.getLong("ID"));
        promotion.setVolId(rs.getLong("VOL_ID"));
        promotion.setClasseId(rs.getLong("CLASSE_ID"));
        promotion.setNbrSiege(rs.getInt("NBR_SIEGE"));
        promotion.setReduction(rs.getDouble("REDUCTION"));
        promotion.setPrix(rs.getDouble("PRIX"));
        promotion.setDateButoire(rs.getDate("DATE_BUTOIRE"));
        promotion.setNbrSiegeDispo(rs.getInt("NBR_SIEGE_DISPO"));
        return promotion;
    }

    public Promotion getPromotionForReservation(Long volId, Long classeId, Long placeNeeded) throws SQLException {
        int reservedCount = volDAO.getReservationCountWithClasse(volId, classeId);

        List<Promotion> promotions = this.findByVolId(volId)
            .stream()
            .filter(p -> classeId.equals(p.getClasseId())) 
            .collect(Collectors.toList());

        int cumulativeSeats = 0;
        for (Promotion promo : promotions) {
            cumulativeSeats += promo.getNbrSiege();

            if (reservedCount + placeNeeded <= cumulativeSeats) {
                Promotion result = new Promotion();
                result.setId(promo.getId());
                result.setClasseId(promo.getClasseId());
                result.setVolId(promo.getVolId());
                result.setReduction(promo.getReduction());

                int seatsRemainingInPromo = cumulativeSeats - reservedCount;
                //stockena ato aminy nombre de siege ato le nombre de siege en promo azony
                result.setNbrSiege((int)Math.min(seatsRemainingInPromo, placeNeeded));

                return result;
            }
        }
        return null;
    }

}
