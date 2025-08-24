package mg.ticketing.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import mg.ticketing.model.Classe;
import mg.ticketing.util.DatabaseConnection;

public class ClasseDAO {
     public List<Classe> findAll() throws SQLException {
        List<Classe> classes = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM TICKETING_CLASSE")) {
            while (rs.next()) {
                Classe classe = new Classe();
                classe.setId(rs.getLong("ID"));
                classe.setNom(rs.getString("NOM"));
                classes.add(classe);
            }
        }
        return classes;
    }    
    
    public Classe findById(Long id) throws SQLException {
        String query="SELECT * FROM TICKETING_CLASSE WHERE ID =?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query );) {
                stmt.setLong(1, id);     
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Classe classe = new Classe();
                    classe.setId(rs.getLong("ID"));
                    classe.setNom(rs.getString("NOM"));
                    return classe;
                }
        }
        return null;
    }    
}
