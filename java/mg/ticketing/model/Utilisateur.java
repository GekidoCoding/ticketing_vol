package mg.ticketing.model;

import mg.sprint.framework.annotation.auth.ClassLevel;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;

import lombok.Data;

@Data
@ClassLevel(1) 
public class Utilisateur {
    private Long id;
    private String username;
    private String password;
    private String nom ;
    private String prenom ;
    private Date dtn;
    
    public Integer getAge() {
        if (this.getDtn() == null) return null;

        LocalDate birthDate = dtn.toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();
        LocalDate today = LocalDate.now(); 

        return Period.between(birthDate, today).getYears();
    }
}
