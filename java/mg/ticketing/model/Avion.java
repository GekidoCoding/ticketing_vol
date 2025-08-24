package mg.ticketing.model;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import lombok.Data;
import mg.ticketing.DAO.CompanyDAO;
import mg.ticketing.DAO.SiegeDAO;

@Data
public class Avion {
    private Long id;
    private String modele;
    private Date dateFabrication;
    private Long companyId;

    public Company getCompany() throws SQLException{
        return new CompanyDAO().findById(this.getCompanyId());
    }
    public List<Siege> getSieges() throws SQLException{
        return new SiegeDAO().findByAvionId(this.getId());
    }
   public Siege getSiegesWithClasse(Long classeId) {
    try {
        return this.getSieges().stream()
                .filter(s -> classeId.equals(s.getClasseId()))
                .findFirst()
                .orElse(null);
    } catch (SQLException e) {
        throw new RuntimeException("Erreur lors de la récupération des sièges", e);
    }
}

}
