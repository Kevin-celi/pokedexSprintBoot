const BASE_URL = "http://localhost:8084/api/pokemon";

async function fetchPokemonByName() {
    const nameField = document.getElementById("nameField");
    const pokemonImage = document.getElementById("pokemonImage");
    const infoArea = document.getElementById("infoArea");

    const name = nameField.value.trim().toUpperCase();
    if (!name) {
        alert("Por favor, ingresa un nombre de Pokémon.");
        return;
    }


    try {

        const response = await fetch(`${BASE_URL}/name-pokemon-ht/${name}`);
        if (!response.ok) {
            throw new Error(`Error: ${response.status} - ${response.statusText}`);
        }

        const pokemon = await response.json();

        // Mostrar la información del Pokémon
        infoArea.value = `
            Nombre: ${pokemon.name}
            Hábitat: ${pokemon.habitat}
            Tipo: ${pokemon.typePokemon}
            Habilidad: ${pokemon.ability}
            Debilidad: ${pokemon.weakness}
        `;

        // Mostrar la imagen del Pokémon
        if (pokemon.imageUrl) {
            pokemonImage.src = pokemon.imageUrl;
            pokemonImage.style.display = "block";
        } else {
            pokemonImage.src = "default-image.png"; // Imagen predeterminada
            pokemonImage.style.display = "block";
        }
    } catch (error) {
        console.error("Error al buscar Pokémon:", error);
        infoArea.value = "No se pudo encontrar el Pokémon. Intenta de nuevo.";
        pokemonImage.style.display = "none";
    }
}