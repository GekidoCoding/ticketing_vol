package mg.ticketing.model;

import java.sql.Date;
import java.sql.SQLException;

import lombok.Data;
import mg.sprint.framework.annotation.validation.Decimal;
import mg.sprint.framework.annotation.validation.Max;
import mg.sprint.framework.annotation.validation.Min;
import mg.sprint.framework.annotation.validation.Required;
import mg.ticketing.DAO.ClasseDAO;

@Data
public class Promotion {
    private Long id;
    
    @Required
    private Long classeId;
    
    @Required
    private Long volId;
    
    @Required
    private Integer nbrSiege;

    @Decimal
    @Min(value = 0.0)
    @Max(value = 100.0)
    private Double reduction;

    private Double prix;

    private Date dateButoire;

    private Integer nbrSiegeDispo;
    
    

    public Classe getClasse() throws SQLException{
        return new ClasseDAO().findById(this.getClasseId());
    }

}
