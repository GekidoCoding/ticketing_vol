package mg.ticketing.DAO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import mg.ticketing.config.TicketingConfig;
import mg.ticketing.model.CategorieAge;
import mg.ticketing.model.PromoReservation;
import mg.ticketing.model.Promotion;
import mg.ticketing.model.Reservation;
import mg.ticketing.model.Siege;
import mg.ticketing.model.Vol;
import mg.ticketing.util.DatabaseConnection;

public class ReservationDAO {
    private final TicketingConfig config = new TicketingConfig();

    public Reservation findById(Long id) throws SQLException{
        String sql = "SELECT * FROM TICKETING_RESERVATION WHERE ID=?";

        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql);
        ) {
                
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
               return  mapResultSet(rs);
            }
        }
        return null;
    }

    public List<Reservation> findAllByVolId(Long volId) throws SQLException{
         List<Reservation> Reservations = new ArrayList<>();
        String sql = "SELECT * FROM TICKETING_RESERVATION WHERE VOL_ID=?";

        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql);
            ) {
                
            ps.setLong(1, volId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Reservation Reservation = mapResultSet(rs);
                Reservations.add(Reservation);
            }
        }
        return Reservations;
    }
    
    public List<Reservation> getReservationAfterDateWithClasse(Reservation reservation) throws SQLException {
        return this.findAllByVolId(reservation.getVolId()).stream()
            .filter(r->!r.getId().equals(reservation.getId()))
            .filter(r -> r.getClasseId().equals(reservation.getClasseId()))
            .filter(r -> r.getDateReservation().after(reservation.getDateReservation())) 
            .filter(r->r.getStatutId()==1L )
            .sorted(Comparator.comparing(Reservation::getDateReservation))
            .collect(Collectors.toList());
    }

    public List<Reservation> findAllByUtilisateurId(Long utilisateurId) throws SQLException{
         List<Reservation> Reservations = new ArrayList<>();
        String sql = "SELECT * FROM TICKETING_RESERVATION WHERE UTILISATEUR_ID=?";

        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql);
            ) {
                
            ps.setLong(1, utilisateurId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Reservation Reservation = mapResultSet(rs);
                Reservations.add(Reservation);
            }
        }
        return Reservations;
    }

    public Reservation mapResultSet(ResultSet rs) throws SQLException{
        Reservation Reservation=new Reservation();
        Reservation.setId(rs.getLong("ID"));
        Reservation.setVolId(rs.getLong("VOL_ID"));
        Reservation.setClasseId(rs.getLong("CLASSE_ID"));
        Reservation.setPrix(rs.getDouble("PRIX"));
        Reservation.setCategorieAgeId(rs.getLong("CATEGORIE_AGE_ID"));
        Reservation.setDateReservation(rs.getDate("DATE_RESERVATION"));
        Reservation.setNbSieges(rs.getInt("NB_SIEGES"));
        Reservation.setPrix(rs.getDouble("PRIX"));
        Reservation.setStatutId(rs.getLong("STATUT_ID"));
        Reservation.setUtilisateurId(rs.getLong("UTILISATEUR_ID"));
        return Reservation;
    }

    public boolean isDispo(Long volId, Long classeId , Integer placeNeed) throws SQLException{
        Integer placeDispo = new VolDAO().getPlaceDispo(volId, classeId);
        return (placeDispo-placeNeed)>0;
    }
    public void update(Reservation reservation) throws SQLException {
        if (reservation == null || reservation.getId() == null) {
            throw new IllegalArgumentException("Reservation ou son ID est null, mise à jour impossible.");
        }

        String sql = "UPDATE TICKETING_RESERVATION SET " +
                    "UTILISATEUR_ID = ?, VOL_ID = ?, DATE_RESERVATION = ?, " +
                    "STATUT_ID = ?, CATEGORIE_AGE_ID = ?, NB_SIEGES = ?, " +
                    "CLASSE_ID = ?, PRIX = ? " +
                    "WHERE ID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, reservation.getUtilisateurId());
            stmt.setLong(2, reservation.getVolId());
            stmt.setDate(3, reservation.getDateReservation());
            stmt.setLong(4, reservation.getStatutId());
            stmt.setLong(5, reservation.getCategorieAgeId());
            stmt.setInt(6, reservation.getNbSieges());
            stmt.setLong(7, reservation.getClasseId());
            stmt.setDouble(8, reservation.getPrix());
            stmt.setLong(9, reservation.getId());

            stmt.executeUpdate();
        }
    }

    public void create(Reservation reservation) throws SQLException {
        String sql = "INSERT INTO TICKETING_RESERVATION " +
                    "(UTILISATEUR_ID, VOL_ID, DATE_RESERVATION, STATUT_ID, CATEGORIE_AGE_ID, NB_SIEGES, CLASSE_ID, PRIX) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ? , ?)";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, reservation.getUtilisateurId());
            stmt.setLong(2, reservation.getVolId());
            stmt.setDate(3, reservation.getDateReservation());
            stmt.setLong(4, reservation.getStatutId());
            stmt.setLong(5, reservation.getCategorieAgeId());
            stmt.setInt(6, reservation.getNbSieges());
            stmt.setLong(7, reservation.getClasseId());
            stmt.setDouble(8, reservation.getPrix());
            stmt.executeUpdate();

           
        }
    }
    public Reservation getLast() throws SQLException {
        String sql = "SELECT * FROM TICKETING_RESERVATION ORDER BY ID DESC FETCH FIRST 1 ROWS ONLY";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        }
        return null;
    }

    public boolean isOkHeureReservation(Date dateReservation, Vol vol) throws IOException {
        Integer heureAvant = config.getHeureAvantReservation();
        if (heureAvant == null) return true;

        LocalDateTime depart = vol.getDateHeureDepart();

        LocalDateTime reservation;
        if (dateReservation instanceof java.sql.Date) {
            reservation = ((java.sql.Date) dateReservation).toLocalDate()
                    .atStartOfDay(ZoneId.systemDefault())
                    .toLocalDateTime();
        } else {
            reservation = dateReservation.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
        }

        long heuresDiff = Duration.between(reservation, depart).toHours();
        return heuresDiff >= heureAvant;
    }
    
    public  double calculerTotalPrix(List<PromoReservation> promotions, int placeNeed, double prixNormal , CategorieAge categorieAge) throws SQLException {
        int nbPromo = 0;
        double total = 0.0;

        for (PromoReservation promo : promotions) {
            Promotion p = promo.getPromotion();
            total += promo.getNbSiegesPromo() * p.getPrix();
            nbPromo += promo.getNbSiegesPromo();
        }
        total += (placeNeed - nbPromo) * prixNormal;
        return (total)*(categorieAge.getPourcentagePayer()/100);
    }

    public boolean isOkHeureAnnulation(LocalDateTime dateAnnulation, Reservation reservation) throws IOException, SQLException {
        Integer heureAvant = config.getHeureAvantAnnulation();
        if (heureAvant == null) return true;

        LocalDateTime depart = reservation.getVol().getDateHeureDepart();

        long heuresDiff = Duration.between(dateAnnulation, depart).toHours();

        return heuresDiff >= heureAvant;
    }

    public Integer getPlacePromuForReservation(Reservation reserve) throws SQLException{
        List<PromoReservation> promoReserves = new PromoReservationDAO().findByReservationId(reserve.getId());
        Integer placePromu =0;
        for (PromoReservation promo : promoReserves) {
        placePromu+=promo.getNbSiegesPromo();
        }
        return placePromu;
    }


    public void annulationReservation(Reservation reservation) throws SQLException{
        if(reservation.getStatutId()!=2L){
             reservation.setStatutId(2L);
            this.update(reservation);
            List<PromoReservation> promotions = new PromoReservationDAO().findByReservationId(reservation.getId());
            List<Reservation> reservations =  this.getReservationAfterDateWithClasse(reservation);
            for (PromoReservation promo : promotions) {
                new PromoReservationDAO().reInit(promo);
            }
            for (Reservation reserve : reservations) {
                Integer placePromu = this.getPlacePromuForReservation(reserve);
                Integer placeNeed = reserve.getNbSieges()-placePromu;
                List<PromoReservation> newPromotions = new PromoReservationDAO().getPromoReservationForReservation(reserve.getVolId(), reserve.getClasseId(), placeNeed, reserve.getDateReservation());
                for (PromoReservation promo : newPromotions) {
                    promo.setReservationId(reserve.getId());
                    new PromoReservationDAO().create(promo);                
                }
                Vol vol = reservation.getVol();
                Siege siegeAvionClasse = vol.getAvion().getSiegesWithClasse(reserve.getClasseId());
                Double prixNormal = siegeAvionClasse.getPrix();
                Double totalPrix= this.calculerTotalPrix(newPromotions, reserve.getNbSieges(), prixNormal, reserve.getCategorieAge());
                reserve.setPrix(totalPrix);
                this.update(reserve);
            }
        }
    }

}
