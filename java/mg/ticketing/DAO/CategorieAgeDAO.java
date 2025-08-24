package mg.ticketing.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mg.ticketing.model.CategorieAge;
import mg.ticketing.model.Utilisateur;
import mg.ticketing.util.DatabaseConnection;

public class CategorieAgeDAO {
      public List<CategorieAge> findAll() throws SQLException {
        String sql = "SELECT * FROM TICKETING_CATEGORIE_AGE";
        List<CategorieAge> categories=new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql);
            ) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CategorieAge categorie= mapResultSet(rs);
                categories.add(categorie);
            }
        }
        return categories;
    }
      public CategorieAge findById(Long id) throws SQLException {
        String sql = "SELECT * FROM TICKETING_CATEGORIE_AGE WHERE ID=?";

        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql);
            ) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return mapResultSet(rs);
            }
        }
        return null;
    }
    public CategorieAge findByAge(Integer age) throws SQLException {
        String sql = "SELECT * FROM TICKETING_CATEGORIE_AGE WHERE BORNE_MIN <= ? AND BORNE_MAX >= ?";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, age);
            ps.setInt(2, age);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        }
        return null;
    }
    
    public CategorieAge findByUtilisateur(Utilisateur utilisateur) throws SQLException{
        return this.findByAge(utilisateur.getAge());
    }

    public CategorieAge mapResultSet(ResultSet rs) throws SQLException{
        CategorieAge CategorieAge=new CategorieAge();
        CategorieAge.setId(rs.getLong("ID"));
        CategorieAge.setNom(rs.getString("NOM"));
        CategorieAge.setBorneMin(rs.getInt("BORNE_MIN"));
        CategorieAge.setBorneMax(rs.getInt("BORNE_MAX"));
        CategorieAge.setPourcentagePayer(rs.getDouble("POURCENTAGE_PAYER"));
        return CategorieAge;
    }   
}
