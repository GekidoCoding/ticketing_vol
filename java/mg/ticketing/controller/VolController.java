package mg.ticketing.controller;

import mg.sprint.framework.annotation.arg.RequestObject;
import mg.sprint.framework.annotation.arg.RequestParam;
import mg.sprint.framework.annotation.auth.AuthController;
import mg.sprint.framework.annotation.auth.AuthMethod;
import mg.sprint.framework.annotation.controller.BaseUrl;
import mg.sprint.framework.annotation.controller.Controller;
import mg.sprint.framework.annotation.http.Get;
import mg.sprint.framework.annotation.http.Post;
import mg.sprint.framework.annotation.method.Url;
import mg.sprint.framework.core.object.ModelView;
import mg.ticketing.DAO.AvionDAO;
import mg.ticketing.DAO.VilleDAO;
import mg.ticketing.DAO.VolDAO;
import mg.ticketing.model.Vol;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;


@Controller
@BaseUrl(path = "/admin/vol")
@AuthController(level = 1)
public class VolController {

    private final VolDAO volDAO = new VolDAO();
    private final VilleDAO villeDAO = new VilleDAO();
    private final AvionDAO avionDAO = new AvionDAO();

    @Get
    @Url(path = "/")
    @AuthMethod(level = 1)
    public ModelView listVols() throws SQLException {
        List<Vol> vols = volDAO.findAll();
        ModelView mv = new ModelView("/vol/list.jsp","GET");
        mv.addData("vols", vols);
        mv.addData("avions", avionDAO.findAll());
        mv.addData("villes",villeDAO.findAll());
        return mv;
    }

    @Get
    @Url(path = "/search")
    public ModelView searchMultiCrtieria(
        @RequestParam("avionId") Long avionId, 
        @RequestParam("villeDepartId") Long villeDepartId ,
        @RequestParam("villeArriveeId") Long villeArriveeId ,
        @RequestParam("date1") Date date1,
        @RequestParam("date2") Date date2
    ) throws SQLException{
        List<Vol> vols = volDAO.criteria(avionId, villeDepartId, villeArriveeId, date1, date2);
        ModelView mv = new ModelView("/vol/list.jsp","GET");
        mv.addData("vols",vols );
        mv.addData("avions", avionDAO.findAll());
        mv.addData("villes",villeDAO.findAll());
        return mv;
    }

    @Get
    @Url(path = "/nouveau")
    @AuthMethod(level = 2)
    public ModelView showCreateForm() throws SQLException {
        ModelView mv = new ModelView("/vol/form.jsp","GET");
        mv.addData("vol", new Vol());
        mv.addData("action", "create");
        mv.addData("villes", villeDAO.findAll());
        mv.addData("avions", avionDAO.findAll()); 
        return mv;
    }

    @Get
    @Url(path = "/editer")
    @AuthMethod(level = 2)
    public ModelView showEditForm(@RequestParam(value = "id") Long id) throws SQLException {
        Vol vol = volDAO.findById(id);
        if (vol == null) {
            return new ModelView("/admin/vol/","GET");
        }

        ModelView mv = new ModelView("/vol/form.jsp","GET");

        mv.addData("vol.id", vol.getIdString());
        mv.addData("vol.avionId", vol.getAvionIdString());
        mv.addData("vol.villeDepartId", vol.getVilleDepartIdString());
        mv.addData("vol.villeArriveeId", vol.getVilleArriveeIdString());
        mv.addData("vol.dateHeureDepart", vol.getDateHeureDepartString());
     
        mv.addData("action", "update");
        mv.addData("villes", villeDAO.findAll());
        mv.addData("avions", avionDAO.findAll()); 

        return mv;
    }

    @Post
    @Url(path = "/creer")
    @AuthMethod(level = 2)
    public ModelView createVol(@RequestObject(name = "vol") Vol vol) throws SQLException {
        System.out.println("creating vol ... ");
        volDAO.create(vol);
        ModelView mv = new ModelView("/admin/vol/","GET");
        mv.addData("success", "Vol créé avec succès");
        return mv;
    }

    @Post
    @Url(path = "/modifier")
    @AuthMethod(level = 2)
    public ModelView updateVol(@RequestParam(value = "id") Long id, @RequestObject(name = "vol") Vol vol) throws SQLException {
        vol.setId(id);
        System.out.println("updating vol ... ");
        volDAO.update(vol);
        ModelView mv = new ModelView("/admin/vol/","GET");
        mv.addData("success", "Vol mis à jour avec succès");
        return mv;
    }

    @Get
    @Url(path = "/supprimer")
    @AuthMethod(level = 2)
    public ModelView deleteVol(@RequestParam(value = "id") Long id) throws SQLException {
        volDAO.delete(id);
        ModelView mv = new ModelView("/admin/vol/","GET");
        mv.addData("success", "Vol supprimé avec succès");
        return mv;
    }

    
}
