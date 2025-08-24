package mg.ticketing.config;

import java.io.IOException;
import java.util.Properties;

public class TicketingConfig {
    private  String ticketingFile="ticketing.properties";
    
    public Integer getHeureAvantAnnulation() throws IOException{
        ConfigReader config = new ConfigReader(this.ticketingFile);
        Properties properties=config.getProps();
        return Integer.parseInt(properties.getProperty("heure_avant_annulation"));
    }

    public Integer getHeureAvantReservation() throws IOException{
        ConfigReader config = new ConfigReader(this.ticketingFile);
        Properties properties=config.getProps();
        return Integer.parseInt(properties.getProperty("heure_avant_reservation"));
    }
}
