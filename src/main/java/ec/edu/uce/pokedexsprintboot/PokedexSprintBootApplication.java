package ec.edu.uce.pokedexsprintboot;



import ec.edu.uce.pokedexsprintboot.interfasGrafica.IgBienvenida;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication

public class PokedexSprintBootApplication  {


    public static void main(String[] args) {

       // IgBienvenida app1 = new IgBienvenida();
        SpringApplication.run(PokedexSprintBootApplication.class, args);



    }

}
