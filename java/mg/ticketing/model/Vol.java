package mg.ticketing.model;

import java.util.Date;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import lombok.Data;
import mg.sprint.framework.annotation.validation.DateFormat;
import mg.sprint.framework.annotation.validation.Required;
import mg.ticketing.DAO.AvionDAO;
import mg.ticketing.DAO.VilleDAO;

@Data
public class Vol {
    private Long id;
    
    @Required
    private Long avionId;
    
    @Required
    private Long villeDepartId;
    
    @Required
    private Long villeArriveeId;
    
    @Required
    @DateFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dateHeureDepart;
    
    
    public Date getDateDepartAsDate() {
        if (this.dateHeureDepart == null) return null;
        return Date.from(this.dateHeureDepart.atZone(ZoneId.systemDefault()).toInstant());
    }

    public String getIdString() { 
        return id != null ? id.toString() : ""; 
    }

    public String getAvionIdString() { 
        return avionId != null ? avionId.toString() : ""; 
    }

    public String getVilleDepartIdString() { 
        return villeDepartId != null ? villeDepartId.toString() : ""; 
    }

    public String getVilleArriveeIdString() { 
        return villeArriveeId != null ? villeArriveeId.toString() : ""; 
    }

    public String getDateHeureDepartString() {
        return dateHeureDepart != null 
            ? dateHeureDepart.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")) 
            : "";
    }

    public Ville getVilleDepart() throws SQLException{
        return new VilleDAO().findById(this.getVilleDepartId());
    }
    public Ville getVilleArrivee() throws SQLException{
        return new VilleDAO().findById(this.getVilleArriveeId());
    };
    public Avion getAvion()throws SQLException{
        return new AvionDAO().findById(this.getAvionId());
    }
}
