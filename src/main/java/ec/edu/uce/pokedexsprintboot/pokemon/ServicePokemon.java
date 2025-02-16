package ec.edu.uce.pokedexsprintboot.pokemon;

import ec.edu.uce.pokedexsprintboot.Repository.PokemonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicePokemon {
    @Autowired
    private PokemonRepository pokemonRepository;

    // Persistir los datos de los pokemones
    public void savePokemon(Pokemon pokemon) {
        pokemonRepository.save(pokemon);
    }

    public List<Pokemon> findByTypePokemon(String name) {

        return pokemonRepository.findByTypePokemon(name);
    }

    public Pokemon findByName(String name) {
        return pokemonRepository.findByName(name);
    }
}


