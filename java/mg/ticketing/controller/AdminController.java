package mg.ticketing.controller;

import mg.sprint.framework.annotation.auth.AuthController;
import mg.sprint.framework.annotation.controller.Controller;
import mg.sprint.framework.annotation.http.Get;
import mg.sprint.framework.annotation.method.Url;
import mg.sprint.framework.core.object.ModelView;

@Controller
@AuthController(level = 2) 
public class AdminController {
    
    @Get
    @Url(path = "/admin/home")
    public ModelView adminHome() {
        ModelView mv= new ModelView("/admin.jsp" , "GET");
        mv.addData("message", "Welcome Admin");
        return mv;
    }

}