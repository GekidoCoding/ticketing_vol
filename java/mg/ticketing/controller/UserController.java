package mg.ticketing.controller;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import mg.sprint.framework.annotation.arg.RequestParam;
import mg.sprint.framework.annotation.auth.AuthController;
import mg.sprint.framework.annotation.controller.Controller;
import mg.sprint.framework.annotation.http.Get;
import mg.sprint.framework.annotation.method.Url;
import mg.sprint.framework.core.object.ModelView;
import mg.ticketing.DAO.AvionDAO;
import mg.ticketing.DAO.VilleDAO;
import mg.ticketing.DAO.VolDAO;
import mg.ticketing.model.Vol;

@Controller
@AuthController(level = 1) 
public class UserController {
  private final VolDAO volDAO = new VolDAO();
    private final VilleDAO villeDAO = new VilleDAO();
    private final AvionDAO avionDAO = new AvionDAO();

    @Get
    @Url(path = "/user/home")
    public ModelView userHome() {
        ModelView mv = new ModelView("/user.jsp","GET");
        mv.addData("message", "Welcome User");
        return mv;
    }
     

    @Get
    @Url(path = "/utilisateur/vol/")
    public ModelView volUtilisateur() throws SQLException {
       List<Vol> vols = volDAO.findAll();
        ModelView mv = new ModelView("/vol/listUtilisateur.jsp","GET");
        mv.addData("vols", vols);
         mv.addData("avions", avionDAO.findAll());
        mv.addData("villes",villeDAO.findAll());
        return mv;
    }

    @Get
    @Url(path = "/utilisateur/vol/search")
    public ModelView searchMultiCrtieria(
        @RequestParam("avionId") Long avionId, 
        @RequestParam("villeDepartId") Long villeDepartId ,
        @RequestParam("villeArriveeId") Long villeArriveeId ,
        @RequestParam("date1") Date date1,
        @RequestParam("date2") Date date2
    ) throws SQLException{
        List<Vol> vols = volDAO.criteria(avionId, villeDepartId, villeArriveeId, date1, date2);
        ModelView mv = new ModelView("/vol/listUtilisateur.jsp","GET");
        mv.addData("vols",vols );
        mv.addData("avions", avionDAO.findAll());
        mv.addData("villes",villeDAO.findAll());
        return mv;
    }

    
}