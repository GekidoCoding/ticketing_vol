package mg.ticketing.controller;

import mg.sprint.framework.annotation.controller.BaseUrl;
import mg.sprint.framework.annotation.controller.Controller;
import mg.sprint.framework.annotation.http.Get;
import mg.sprint.framework.annotation.method.Url;
import mg.sprint.framework.core.object.ModelView;
import mg.ticketing.DAO.PromoReservationDAO;
import mg.ticketing.DAO.ReservationDAO;
import mg.ticketing.model.PromoReservation;

import java.sql.SQLException;
import java.util.List;

import mg.sprint.framework.annotation.arg.RequestParam;
import mg.sprint.framework.annotation.auth.AuthController;
import mg.sprint.framework.annotation.auth.AuthMethod;

@Controller
@AuthController(level = 1) 
@BaseUrl(path = "/promotion/reservation")
public class PromotionReservationController {
    private final PromoReservationDAO promoReservationDAO = new PromoReservationDAO();
    private final ReservationDAO reservationDAO = new ReservationDAO();

    @Get
    @Url(path = "")
    @AuthMethod(level = 1)
    public ModelView getPromotionReservationByReservation(@RequestParam("reservationId") Long reservationId) throws SQLException {
        ModelView mv = new ModelView("/promotion/reservation/promotionReservation.jsp" , "GET");
        List<PromoReservation> promotions = promoReservationDAO.findByReservationId(reservationId);
        mv.addData("promotions", promotions);
        mv.addData("reservation",reservationDAO.findById(reservationId) );
        return mv;
    }
}
