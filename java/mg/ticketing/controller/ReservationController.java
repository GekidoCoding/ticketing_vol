package mg.ticketing.controller;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import mg.sprint.framework.annotation.controller.BaseUrl;
import mg.sprint.framework.annotation.controller.Controller;
import mg.sprint.framework.annotation.http.Get;
import mg.sprint.framework.annotation.http.Post;
import mg.sprint.framework.annotation.method.Url;
import mg.sprint.framework.core.object.ModelView;
import mg.sprint.framework.core.object.MySession;
import mg.ticketing.DAO.CategorieAgeDAO;
import mg.ticketing.DAO.ClasseDAO;
import mg.ticketing.DAO.PromoReservationDAO;
import mg.ticketing.DAO.ReservationDAO;
import mg.ticketing.DAO.VolDAO;
import mg.ticketing.model.CategorieAge;
import mg.ticketing.model.PromoReservation;
import mg.ticketing.model.Reservation;
import mg.ticketing.model.Siege;
import mg.ticketing.model.Utilisateur;
import mg.ticketing.model.Vol;
import mg.sprint.framework.annotation.arg.RequestParam;
import mg.sprint.framework.annotation.auth.AuthController;


@Controller
@AuthController(level = 1) 
public class ReservationController {
    private final ReservationDAO reservationDAO = new ReservationDAO();
    private final PromoReservationDAO promoReservationDAO = new PromoReservationDAO();
    private final CategorieAgeDAO categorieAgeDAO = new CategorieAgeDAO();
    private final VolDAO volDAO = new VolDAO();
    private final ClasseDAO classeDao = new ClasseDAO();


    @Get
    @Url(path = "/utilisateur/reservation/reservations")
    public ModelView goToVosReservations(@RequestParam(value = "utilisateurId") Long utilisateurId) throws SQLException{
        ModelView mv = new ModelView("/reservation/reservations.jsp");
        List<Reservation> reservations = reservationDAO.findAllByUtilisateurId(utilisateurId);
        mv.addData("reservations", reservations);
        return mv;
    }

    @Get
    @Url(path = "/utilisateur/reservation")
    public ModelView goToReservation(@RequestParam(value = "volId") Long volId) throws SQLException{
        ModelView mv = new ModelView("/reservation/reserver.jsp");
        Vol vol = volDAO.findById(volId);
        mv.addData("vol", vol);
        mv.addData("classes",classeDao.findAll() );
        mv.addData("categorieAges", new CategorieAgeDAO().findAll());
        return mv;
    }   

    @Post
    @Url(path = "/utilisateur/reservation/reserver")
    public ModelView getReservation(MySession session,
                                    @RequestParam("volId") Long volId,
                                    @RequestParam("classeId") Long classeId,
                                    @RequestParam("placeNeed") Integer placeNeed,
                                    @RequestParam("categorieAgeId")Long categorieAgeId,
                                    @RequestParam("dateReservation") Date dateReservation) throws SQLException, IOException {
        ModelView mv = new ModelView("/utilisateur/reservation?volId="+volId);
        List<String> errors = new ArrayList<>();

        Utilisateur utilisateur = (Utilisateur) session.get("utilisateur");
        if (utilisateur == null) {
            errors.add("Aucun utilisateur connecté, retournez au login.");
            mv.setUrl("/login.jsp");
            mv.addData("errors", errors);
            return mv;
        }
        

        Vol vol = volDAO.findById(volId);

        if (!reservationDAO.isDispo(volId, classeId, placeNeed)) {
            errors.add("Plus de place pour " + placeNeed + " personne(s) !");
            mv.addData("errors", errors);
            return mv;
        }

        if(!reservationDAO.isOkHeureReservation(dateReservation, vol)){
            errors.add("Il est deja trop tard pour reserver ce vol !");
            mv.addData("errors", errors);
            return mv;
        }

        List<PromoReservation> promotions = promoReservationDAO.getPromoReservationForReservation(volId, classeId, placeNeed, dateReservation);
        if (promotions.isEmpty()) {
            System.out.println("aucune promotions ... ");
        }

        CategorieAge categorieAge = categorieAgeDAO.findById(categorieAgeId);

        Siege siegeAvionClasse = vol.getAvion().getSiegesWithClasse(classeId);
        if (siegeAvionClasse == null) {
            throw new SQLException("Pas de siège avec la classeId " + classeId + " pour l'avion " + vol.getAvionIdString());
        }
        double prixNormal = siegeAvionClasse.getPrix();

        Double totalPrix = reservationDAO.calculerTotalPrix(promotions, placeNeed, prixNormal ,categorieAge);

        Reservation reservation = new Reservation();
        reservation.setUtilisateurId(utilisateur.getId());
        reservation.setVolId(volId);
        reservation.setClasseId(classeId);
        reservation.setDateReservation(dateReservation);
        reservation.setNbSieges(placeNeed);
        reservation.setStatutId(1L);
        reservation.setPrix( totalPrix);

        if (categorieAge != null) {
            reservation.setCategorieAgeId(categorieAge.getId());
        }

        reservationDAO.create(reservation);
        Reservation reservationLast=reservationDAO.getLast();
        Long idReservation=reservationLast.getId();
        for (PromoReservation promo : promotions) {
            promo.setReservationId(idReservation);
            promoReservationDAO.create(promo);
        }

        mv.addData("totalPrix", totalPrix);
        mv.addData("reservationId", idReservation);
        mv.addData("success", "place reserver avec succes ! ");
        return mv;
    }

    @Get
    @Url(path = "/promotion/reservation/annuler")
    public ModelView annulationDeReservation(@RequestParam(value = "reservationId") Long reservationId) throws SQLException, IOException{
        List<String> errors = new ArrayList<>();
        Reservation reservation = reservationDAO.findById(reservationId);
        ModelView mv = new ModelView("/utilisateur/reservation/reservations?utilisateurId="+reservation.getUtilisateurId(),"GET");
        boolean isOkHeureAnnulation = reservationDAO.isOkHeureAnnulation(LocalDateTime.now(), reservation);
        if(isOkHeureAnnulation){
            reservationDAO.annulationReservation(reservation);
            mv.addData("success", "annulation reussi !");
            return mv;
        }else{
            errors.add("Trop tard pour annuler le reservation de votre vol ");
            mv.addData("errors", errors);
            return mv;
        }
    }
  
 
}
