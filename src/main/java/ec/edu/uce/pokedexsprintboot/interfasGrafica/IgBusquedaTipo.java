package ec.edu.uce.pokedexsprintboot.interfasGrafica;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import com.google.gson.Gson;
import ec.edu.uce.pokedexsprintboot.Client.PokemonClient;
import ec.edu.uce.pokedexsprintboot.pokemon.Pokemon;

public class IgBusquedaTipo extends JFrame{

    private JTextField typeField;  // Campo de texto para ingresar el tipo de Pokémon
    private JPanel resultPanel;   // Panel para mostrar los resultados (información e imágenes)


    public IgBusquedaTipo() {
        setTitle("Buscar Pokemon por Tipo");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel Superior: Entrada del tipo de Pokémon
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        inputPanel.add(new JLabel("Tipo de Pokemon:"));
        typeField = new JTextField(15);
        inputPanel.add(typeField);
        JButton searchButton = new JButton("Buscar");
        inputPanel.add(searchButton);
        add(inputPanel, BorderLayout.NORTH);

        // Panel para mostrar resultados
        resultPanel = new JPanel();
        resultPanel.setLayout(new GridLayout(0, 1, 10, 10)); // Diseño de varias filas
        add(new JScrollPane(resultPanel), BorderLayout.CENTER);

        // Acción al pulsar el botón
        searchButton.addActionListener(e -> fetchPokemon());

        setVisible(true);
    }

    private void fetchPokemon() {
        String type = typeField.getText().trim();
        if (type.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingresa un tipo de Pokemon.");
            return;
        }

        try {
            // Llamar al cliente HTTP para obtener el JSON
            String jsonResponse = PokemonClient.getPokemonByType(type);

            // Usar Gson para convertir el JSON en un array de objetos Pokemon
            Gson gson = new Gson();
            Pokemon[] pokemons = gson.fromJson(jsonResponse, Pokemon[].class);

            resultPanel.removeAll(); // Limpiar resultados anteriores

            if (pokemons.length > 0) {
                for (Pokemon pokemon : pokemons) {
                    JPanel pokemonPanel = new JPanel();
                    pokemonPanel.setLayout(new BorderLayout());
                    pokemonPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

                    // Mostrar imagen del Pokémon
                    JLabel imageLabel = new JLabel();
                    File imageFile = new File(pokemon.getImageUrl());
                    if (imageFile.exists()) {
                        BufferedImage image = ImageIO.read(imageFile);
                        ImageIcon icon = new ImageIcon(image.getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                        imageLabel.setIcon(icon);
                    } else {
                        imageLabel.setText("Imagen no encontrada");
                        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    }
                    pokemonPanel.add(imageLabel, BorderLayout.WEST);

                    // Mostrar información del Pokémon
                    JTextArea infoArea = new JTextArea();
                    infoArea.setText(
                            "Nombre: " + pokemon.getName() + "\n" +
                            "Habitat: " + pokemon.getHabitat() + "\n" +
                            "Tipo: " + pokemon.getTypePokemon() + "\n" +
                            "Habilidad: " + pokemon.getAbility() + "\n" +
                            "Debilidad: " + pokemon.getWeakness());
                    infoArea.setEditable(false);
                    infoArea.setLineWrap(true);
                    infoArea.setWrapStyleWord(true);
                    pokemonPanel.add(infoArea, BorderLayout.CENTER);

                    resultPanel.add(pokemonPanel); // Agregar al panel de resultados
                }
            } else {
                JLabel noResultLabel = new JLabel("No se encontraron Pokemon del tipo: " + type);
                noResultLabel.setHorizontalAlignment(SwingConstants.CENTER);
                resultPanel.add(noResultLabel);
            }

            resultPanel.revalidate(); // Actualizar el panel
            resultPanel.repaint();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Error al obtener datos: " + ex.getMessage());
        }
    }

}
