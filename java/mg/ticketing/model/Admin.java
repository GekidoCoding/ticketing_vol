package mg.ticketing.model;

import mg.sprint.framework.annotation.auth.ClassLevel;
import lombok.Data;

@Data
@ClassLevel(2) 
public class Admin {
    private Long id;
    private String username;
    private String password;
    
}