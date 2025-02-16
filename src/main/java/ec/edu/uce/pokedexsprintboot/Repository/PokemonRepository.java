package ec.edu.uce.pokedexsprintboot.Repository;

import ec.edu.uce.pokedexsprintboot.pokemon.Pokemon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PokemonRepository extends JpaRepository<Pokemon, Long> {
    List<Pokemon> findByTypePokemon(String typePokemon);

    public Pokemon findByName(String name);

//    List<Pokemon> findByHabitatPokemon(String habitatPokemon);
}
