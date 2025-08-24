package mg.ticketing.model;

import lombok.Data;

@Data
public class Siege {
    private Long id;
    private Long avionId;
    private Integer nbr;
    private Long classeId;
    private Double prix ;
}
