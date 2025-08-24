package mg.ticketing.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import mg.ticketing.model.Company;
import mg.ticketing.util.DatabaseConnection;

public class CompanyDAO {
    public Company findById(Long id) throws SQLException {
        String sql = "SELECT * FROM TICKETING_COMPANY WHERE ID=?";

        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Company Company = new Company();
                Company.setId(rs.getLong("ID"));
                Company.setNom(rs.getString("NOM"));
                return Company;
            }
        }
        return null;
    }
    
}