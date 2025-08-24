package mg.ticketing.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;



public class ConfigReader {
    private String file;

    public ConfigReader(String file){
        this.file=file;
    }

    public Properties getProps() throws IOException{
        Properties props = new Properties();
        try (InputStream input = this.getClass().getClassLoader().getResourceAsStream(this.file)) {
            if (input == null) {
                System.out.println("Fichier introuvable !");
                return props;
            }
            props.load(input);
        }
        return props;  
    }
}
