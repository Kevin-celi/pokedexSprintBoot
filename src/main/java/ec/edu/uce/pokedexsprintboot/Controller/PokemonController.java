package ec.edu.uce.pokedexsprintboot.Controller;


import ec.edu.uce.pokedexsprintboot.pokemon.Pokemon;
import ec.edu.uce.pokedexsprintboot.pokemon.ServicePokemon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/pokemon")
public class PokemonController {
    @Autowired
    private ServicePokemon servicePokemon;

    // Endpoint para consultar nombre
    @GetMapping("/name-pokemon-ar/{namePokemon}")
    public String findByName(@PathVariable String namePokemon, Model model) {
        Pokemon pokemon = servicePokemon.findByName(namePokemon);
        if (pokemon == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pokémon no encontrado");
        }
        model.addAttribute("pokemon", pokemon); // Envía el Pokémon a la vista
        return "pokemon-details"; // Nombre de la plantilla Thymeleaf (pokemon-details.html)
    }
}
