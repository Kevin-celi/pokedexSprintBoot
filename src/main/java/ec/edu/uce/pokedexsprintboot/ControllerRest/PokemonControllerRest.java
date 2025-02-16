package ec.edu.uce.pokedexsprintboot.ControllerRest;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.edu.uce.pokedexsprintboot.pokemon.Pokemon;
import ec.edu.uce.pokedexsprintboot.pokemon.ServicePokemon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequestMapping("/api/pokemon") // Ruta base del controlador
public class PokemonControllerRest {
        @Autowired
        private ServicePokemon servicePokemon;

        // Endpoint para buscar Pokémon por tipo
        @GetMapping("/type/{typePokemon}")
        public List<Pokemon> findByTypePokemon(@PathVariable String typePokemon) {
                return servicePokemon.findByTypePokemon(typePokemon);
        }

        // Endpoint para buscar nombre de pokemon
        @GetMapping("/name-pokemon/{namePokemon}")
        public Pokemon findByName(@PathVariable String namePokemon) {
                Pokemon pokemon = servicePokemon.findByName(namePokemon);
                if (pokemon == null) {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pokémon no encontrado");
                }
                return pokemon;
        }

       // Endpoint que me devuelve el pokemon
        @GetMapping("/name-pokemon-var/{namePokemon}")
        public String findByNameJson(@PathVariable String namePokemon) {
                Pokemon pokemon = servicePokemon.findByName(namePokemon);

                if (pokemon == null) {
                        return "No se encontró ningún Pokémon con el nombre: " + namePokemon;
                }

                try {
                        // Convierte el objeto Pokemon a JSON
                        ObjectMapper objectMapper = new ObjectMapper();
                        String jsonPokemon = objectMapper.writeValueAsString(pokemon);
                        return jsonPokemon;
                } catch (Exception e) {
                        e.printStackTrace();
                        return "Error al convertir el Pokémon a JSON.";
                }
        }
}
