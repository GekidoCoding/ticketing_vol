package mg.ticketing.model;

import java.sql.SQLException;

import lombok.Data;
import mg.ticketing.DAO.PromotionDAO;

@Data
public class PromoReservation {
    private Long id;
    private Long reservationId;
    private Long promotionId;
    private Integer nbSiegesPromo;

    public Promotion getPromotion() throws SQLException{
        return new PromotionDAO().findById(this.getPromotionId());
    }
    
}
