package mg.ticketing.controller;

import java.sql.SQLException;
import mg.sprint.framework.annotation.auth.ClassLevel;
import mg.sprint.framework.annotation.controller.BaseUrl;
import mg.sprint.framework.annotation.controller.Controller;
import mg.sprint.framework.annotation.http.Get;
import mg.sprint.framework.annotation.http.Post;
import mg.sprint.framework.annotation.method.Url;
import mg.sprint.framework.core.object.ModelView;
import mg.sprint.framework.core.object.MySession;
import mg.ticketing.DAO.AuthDAO;
import mg.ticketing.model.Admin;
import mg.ticketing.model.Utilisateur;

@Controller
@BaseUrl(path = "/auth")
public class AuthController {
    
    private final AuthDAO authDAO = new AuthDAO();
    
    @Get
    @Url(path = "/login")
    public ModelView showLoginForm() {
        return new ModelView("/login.jsp" , "GET");
    }
    
    @Post
    @Url(path = "/login/process")
    public ModelView processLogin(
            String username, 
            String password,
            MySession session
    ) {
        try {
            Admin admin = authDAO.findAdminByUsername(username);
            if (admin != null) {
                if (password.equals(admin.getPassword())) {
                    session.add("userLevel", admin.getClass().getAnnotation(ClassLevel.class).value());
                    session.add("utilisateur", admin);
                    return new ModelView("/admin/vol/");
                } else {
                    ModelView mv = new ModelView("/login.jsp");
                    mv.addData("error", "Mot de passe incorrect");
                    mv.addData("usernameValue", username); 
                    return mv;
                }
            }
            
            Utilisateur user = authDAO.findUserByUsername(username);
            if (user != null) {
                if (password.equals(user.getPassword())) {
                    session.add("userLevel", user.getClass().getAnnotation(ClassLevel.class).value());
                    session.add("utilisateur", user);
                    return new ModelView("/utilisateur/vol/");
                } else {
                    ModelView mv = new ModelView("/login.jsp");
                    mv.addData("error", "Mot de passe incorrect");
                    mv.addData("usernameValue", username);
                    return mv;
                }
            }
            
            ModelView mv = new ModelView("/login.jsp");
            mv.addData("error", "Nom d'utilisateur inexistant");
            mv.addData("usernameValue", username);
            return mv;
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur de base de données", e);
        }
    }
    
    @Get
    @Url(path = "/logout")
    public ModelView logout(MySession session) {
        session.delete("userLevel");
        session.delete("username");
        return new ModelView("/auth/login");
    }
}