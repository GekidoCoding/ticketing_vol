package mg.ticketing.controller;

import mg.sprint.framework.annotation.controller.BaseUrl;
import mg.sprint.framework.annotation.controller.Controller;
import mg.sprint.framework.annotation.http.Get;
import mg.sprint.framework.annotation.http.Post;
import mg.sprint.framework.annotation.method.Url;
import mg.sprint.framework.core.object.ModelView;
import mg.ticketing.DAO.ClasseDAO;
import mg.ticketing.DAO.PromotionDAO;
import mg.ticketing.DAO.VolDAO;
import mg.ticketing.model.Classe;
import mg.ticketing.model.Promotion;
import mg.ticketing.model.Vol;

import java.sql.SQLException;
import java.util.List;

import mg.sprint.framework.annotation.arg.RequestObject;
import mg.sprint.framework.annotation.arg.RequestParam;
import mg.sprint.framework.annotation.auth.AuthController;
import mg.sprint.framework.annotation.auth.AuthMethod;

@Controller
@AuthController(level = 1) 
@BaseUrl(path = "/admin/promotion")
public class PromotionController {
    private final PromotionDAO promotionDAO = new PromotionDAO();
    private final VolDAO volDAO = new VolDAO();
    private final ClasseDAO classeDAO =new ClasseDAO();

    @Get
    @Url(path = "")
    @AuthMethod(level = 1)
    public ModelView listPromotionByVol(@RequestParam("id") Long id) throws SQLException {
        Vol vol = volDAO.findById(id);
        List<Promotion> promotions = promotionDAO.findByVolId(id);
        ModelView mv = new ModelView("/promotion/list.jsp" , "GET");
        mv.addData("promotions", promotions);
        mv.addData("vol", vol);
        return mv;
    }

    @Get
    @Url(path = "/nouveau")
    @AuthMethod(level = 2)
    public ModelView showCreatePromotion(@RequestParam("id") Long id) throws SQLException {
        Vol vol = volDAO.findById(id);
        List<Classe> classes = classeDAO.findAll();
        ModelView mv = new ModelView("/promotion/form.jsp" , "GET");
        mv.addData("vol", vol);
        mv.addData("classes", classes);
        return mv;
    }
    
    @Get
    @Url(path = "/supprimer")
    @AuthMethod(level = 2)
    public ModelView deletePromotion(@RequestParam("id") Long id) throws SQLException {
        Promotion promotion = promotionDAO.findById(id);
        promotionDAO.delete(id);
        return this.listPromotionByVol(promotion.getVolId());
    }

    @Post
    @Url(path = "/create")
    @AuthMethod(level = 2)
    public ModelView create(@RequestObject(name = "promotion") Promotion promotion) throws SQLException {
        promotionDAO.create(promotion);
        return  this.listPromotionByVol(promotion.getVolId());
    }
}
