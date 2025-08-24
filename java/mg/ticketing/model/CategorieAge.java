package mg.ticketing.model;

import lombok.Data;

@Data
public class CategorieAge {
    private Long id;
    private String nom;
    private Integer borneMin;
    private Integer borneMax;
    private Double pourcentagePayer;
}
