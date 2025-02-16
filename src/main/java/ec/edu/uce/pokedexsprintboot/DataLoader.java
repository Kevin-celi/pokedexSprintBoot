package ec.edu.uce.pokedexsprintboot;

import ec.edu.uce.pokedexsprintboot.pokemon.Pokemon;

import ec.edu.uce.pokedexsprintboot.pokemon.ServicePokemon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ServicePokemon pokemonService;

    Executor executor = Executors.newFixedThreadPool(10);

    private Long parseIdFromUrl(String url) {
        String[] parts = url.split("/");
        return Long.parseLong(parts[parts.length - 1]);
    }

    // Cargar lista de pokemones
    private void loadPokemon() {
        String url = "https://pokeapi.co/api/v2/pokemon?offset=0&limit=500"; // Limitar para pruebas
        String response = new RestTemplate().getForObject(url, String.class);
        JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
        JsonArray results = jsonResponse.getAsJsonArray("results");

        results.forEach(element -> {

            executor.execute(() -> {
                JsonObject pokemonJson = element.getAsJsonObject();
                String pokemonDetailsUrl = pokemonJson.get("url").getAsString();
                String pokemonDetailsResponse = new RestTemplate().getForObject(pokemonDetailsUrl, String.class);
                JsonObject pokemonDetails = JsonParser.parseString(pokemonDetailsResponse).getAsJsonObject();

                // Crear el objeto Pokémon
                Pokemon pokemon = new Pokemon();
                pokemon.setId(pokemonDetails.get("id").getAsLong());
                pokemon.setName(pokemonDetails.get("name").getAsString());

                // Obtener URL de la imagen
                String imageUrl = pokemonDetails
                        .getAsJsonObject("sprites")
                        .get("front_default")
                        .getAsString();
                // Guardar la imagen localmente
                try {
                    String fileName = "pokemon-" + pokemon.getId() + ".png";
                    String localPath = saveImageLocally(imageUrl, fileName);
                    pokemon.setImageUrl(localPath); // Guardar la ruta local en la entidad
                } catch (Exception e) {
                    System.err.println("Error al guardar la imagen del Pokémon " +
                            pokemon.getName() + ": " +
                            e.getMessage());
                }

                // Extraer tipos del Pokemon
                JsonArray typesArray = pokemonDetails.getAsJsonArray("types");
                if (typesArray.size() > 0) {
                    // Obtener el primer tipo de la lista
                    JsonObject typeObject = typesArray.get(0).getAsJsonObject().getAsJsonObject("type");
                    String typeName = typeObject.get("name").getAsString();
                    pokemon.setTypePokemon(typeName); // Guardar solo un tipo
                } else {
                    pokemon.setTypePokemon("no tiene tipo"); // Manejar el caso donde no haya tipos
                }

                // Extraer habilidades del Pokemon
                JsonArray abilitiesArray = pokemonDetails.getAsJsonArray("abilities");
                StringBuilder abilities = new StringBuilder();

                abilitiesArray.forEach(abilityElement -> {
                    JsonObject abilityObject = abilityElement.getAsJsonObject().getAsJsonObject("ability");
                    String abilityName = abilityObject.get("name").getAsString();
                    if (abilities.length() > 0) abilities.append(", "); // Agregar coma si ya hay una habilidad
                    abilities.append(abilityName);
                });
                pokemon.setAbility(abilities.toString());

                // Extraer habitat del Pokemon
                try {
                    String habitatUrl =
                            pokemonDetails.getAsJsonObject("species").get("url").getAsString();
                    String speciesResponse =
                            new RestTemplate().getForObject(habitatUrl, String.class);

                    JsonObject speciesDetails = JsonParser.parseString(speciesResponse).getAsJsonObject();

                    if (speciesDetails.has("habitat") && !speciesDetails.get("habitat").isJsonNull()) {
                        pokemon.setHabitat(
                                speciesDetails
                                        .getAsJsonObject("habitat")
                                        .get("name").getAsString());
                    } else {
                        pokemon.setHabitat("unknown");
                    }
                } catch (Exception e) {
                    pokemon.setHabitat("unknown");
                    System.err.println("Error al obtener el hábitat para el Pokémon " + pokemon.getName() + ": " + e.getMessage());
                }

                // Determinar debilidades basadas en tipos
                String weaknesses = calculateWeaknesses(typesArray);
                pokemon.setWeakness(weaknesses);

                // Guardar el Pokemon en la base de datos
                // pokemonRepository.save(pokemon);
                pokemonService.savePokemon(pokemon);

            });
        });
    }
    private String calculateWeaknesses(JsonArray typesArray) {
        // Mapa de efectividades(debilidades)
        Map<String, List<String>> weaknessesMap = Map.of(
                "fire", List.of("water", "rock", "ground"),
                "water", List.of("electric", "grass"),
                "grass", List.of("fire", "ice", "poison", "flying", "bug"),
                "electric", List.of("ground"),
                "ground", List.of("water", "grass", "ice"),
                "bug",List.of("water", "rock", "iron"),
                "poison",List.of("land", "Psychic"),
                "normal",List.of("fighting type")
        );

        Set<String> weaknesses = new HashSet<>();

        typesArray.forEach(typeElement -> {
            String typeName =
                    typeElement.
                            getAsJsonObject().
                            getAsJsonObject("type").
                            get("name").getAsString();
            List<String> typeWeaknesses =
                    weaknessesMap.getOrDefault(typeName, List.of());
            weaknesses.addAll(typeWeaknesses);
        });

        return String.join(", ", weaknesses); // Devolver las debilidades como una cadena separada por comas
    }

    private String saveImageLocally(String imageUrl, String fileName) throws IOException {
        // Define el directorio donde se guardara las imágenes
        String directoryPath = "src/images";
        Path directory = Paths.get(directoryPath);

        // Crea el directorio si no existe
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }

        // Descarga la imagen desde la URL
        URL url = new URL(imageUrl);
        Path filePath = Paths.get(directoryPath, fileName);

        // Guarda la imagen en el directorio
        Files.copy(url.openStream(), filePath);

        return filePath.toString(); // Devuelve la ruta local de la imagen
    }


    @Override
    @Transactional
    public void run(String... args) throws Exception {
      //  loadPokemon();
    }
}
