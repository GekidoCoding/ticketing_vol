package mg.ticketing.model;

import java.sql.Date;
import java.sql.SQLException;

import lombok.Data;
import mg.ticketing.DAO.CategorieAgeDAO;
import mg.ticketing.DAO.ClasseDAO;
import mg.ticketing.DAO.StatutReservationDAO;
import mg.ticketing.DAO.UtilisateurDAO;
import mg.ticketing.DAO.VolDAO;

@Data
public class Reservation {
    private Long id;
    private Long utilisateurId;
    private Long volId;
    private Date dateReservation;
    private Long statutId ;
    private Long categorieAgeId;
    private Integer nbSieges;
    private Long classeId;
    private Double prix;

    public Utilisateur getUtilisateur() throws SQLException{
        return new UtilisateurDAO().findById(this.getUtilisateurId());
    }

    public Classe getClasse() throws SQLException{
        return new ClasseDAO().findById(this.getClasseId());
    }

    public Vol getVol() throws SQLException{
        return new VolDAO().findById(this.getVolId());
    }
   
    public StatutReservation getStatut(){
        try {
            return new StatutReservationDAO().findById(this.getStatutId());     
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public CategorieAge getCategorieAge() throws SQLException{
        return new CategorieAgeDAO().findById(this.getCategorieAgeId());
    }
}
