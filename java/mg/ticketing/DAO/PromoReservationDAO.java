package mg.ticketing.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import mg.ticketing.model.PromoReservation;
import mg.ticketing.model.Promotion;
import mg.ticketing.util.DatabaseConnection;

public class PromoReservationDAO {
    private final PromotionDAO promotionDAO = new PromotionDAO();

    public List<PromoReservation> findByReservationId(Long reservationId) throws SQLException{
        String query ="SELECT * FROM TICKETING_PROMO_RESERVATION WHERE RESERVATION_ID = ?";
         List<PromoReservation> PromoReservations = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, reservationId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                PromoReservation PromoReservation = new PromoReservation();
                PromoReservation.setId(rs.getLong("ID"));
                PromoReservation.setNbSiegesPromo(rs.getInt("NB_SIEGES_PROMO"));
                PromoReservation.setPromotionId(rs.getLong("PROMOTION_ID"));
                PromoReservation.setReservationId(rs.getLong("RESERVATION_ID"));
                PromoReservations.add(PromoReservation);
            }
        }
        return PromoReservations;
    }

    public void create(PromoReservation promoReservation) throws SQLException {
        String sql = "INSERT INTO TICKETING_PROMO_RESERVATION " +
                    "(RESERVATION_ID, PROMOTION_ID, NB_SIEGES_PROMO) " +
                    "VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (promoReservation.getReservationId() != null) {
                stmt.setLong(1, promoReservation.getReservationId());
            } else {
                stmt.setNull(1, java.sql.Types.NUMERIC);
            }

            if (promoReservation.getPromotionId() != null) {
                stmt.setLong(2, promoReservation.getPromotionId());
            } else {
                stmt.setNull(2, java.sql.Types.NUMERIC);
            }

            stmt.setInt(3, promoReservation.getNbSiegesPromo());

            stmt.executeUpdate();
        }
    }
    public List<PromoReservation> getPromoReservationForReservation(Long volId,Long classeId,Integer placeNeed, Date dateReservation) throws SQLException {
        List<PromoReservation> results = new ArrayList<>();

        List<Promotion> promotions = promotionDAO.findByVolId(volId).stream()
                .filter(p -> classeId.equals(p.getClasseId()))
                .filter(p -> p.getDateButoire() == null || !dateReservation.after(p.getDateButoire())) 
                .collect(Collectors.toList());

        int remainingToReserve = placeNeed;

        for (Promotion promo : promotions) {
            if (remainingToReserve <= 0) break;

            int seatsAvailableInPromo = promo.getNbrSiegeDispo();

            int seatsToTake = Math.min(seatsAvailableInPromo, remainingToReserve);

            if (seatsToTake > 0) {
                PromoReservation pr = new PromoReservation();
                pr.setPromotionId(promo.getId());
                pr.setNbSiegesPromo(seatsToTake);
                results.add(pr);

                promo.setNbrSiegeDispo(seatsAvailableInPromo - seatsToTake);
                promotionDAO.update(promo);

                remainingToReserve -= seatsToTake;
            }
        }


        return results;
    }
    
    public void delete(PromoReservation promo) throws SQLException {
        if (promo == null || promo.getId() == null) {
            throw new IllegalArgumentException("PromoReservation ou son ID est null, suppression impossible.");
        }

        String sql = "DELETE FROM TICKETING_PROMO_RESERVATION WHERE ID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, promo.getId());
            stmt.executeUpdate();
        }
    }

    public void reInit(PromoReservation promo) throws SQLException{
        Promotion promotion = promo.getPromotion();
        promotion.setNbrSiegeDispo(promo.getNbSiegesPromo()+promotion.getNbrSiegeDispo());
        promotionDAO.update(promotion);
        this.delete(promo);
    }

    
}
