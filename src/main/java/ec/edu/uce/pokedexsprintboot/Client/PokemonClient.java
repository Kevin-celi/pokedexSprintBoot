package ec.edu.uce.pokedexsprintboot.Client;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PokemonClient {
    private static final String BASE_URL = "http://localhost:8084/api/pokemon";

    // Metodo para obtener Pokémon por tipo
    public static String getPokemonByType(String typePokemon) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/type/" + typePokemon))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body(); // Devuelve la lista de Pokémon en formato JSON
    }

//    // Metodo para obtener Pokémon por nombre
    public static String getPokemonByName(String namePokemon) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/name-pokemon/" + namePokemon))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body(); // Devuelve la información del Pokémon en formato JSON
    }

//    public static String getPokemonByName(String namePokemon) throws Exception {
//        String url = BASE_URL + "/name-pokemon/" + namePokemon; // Reemplaza {namePokemon} con el valor real
//        HttpClient client = HttpClient.newHttpClient();
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(url)) // Usa la URL correcta
//                .GET()
//                .build();
//
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        return response.body();
//    }


}
