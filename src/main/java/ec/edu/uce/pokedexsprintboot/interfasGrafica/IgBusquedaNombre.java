package ec.edu.uce.pokedexsprintboot.interfasGrafica;

import ec.edu.uce.pokedexsprintboot.Client.PokemonClient;

import javax.swing.*;
import java.awt.*;

public class IgBusquedaNombre extends JFrame {
    private JTextField nameField; // Campo para ingresar el nombre del Pokémon
    private JLabel imageLabel;   // Etiqueta para mostrar la imagen del Pokémon
    private JTextArea infoArea;  // Área de texto para mostrar la información del Pokémon

    public IgBusquedaNombre() {
        setTitle("Buscar Pokémon por Nombre");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel Superior: Entrada del nombre del Pokémon
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        inputPanel.add(new JLabel("Nombre del Pokémon:"));
        nameField = new JTextField(15);
        inputPanel.add(nameField);
        JButton searchButton = new JButton("Buscar");
        inputPanel.add(searchButton);
        add(inputPanel, BorderLayout.NORTH);

        // Panel Central: Imagen e información del Pokémon
        JPanel displayPanel = new JPanel();
        displayPanel.setLayout(new BorderLayout());
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        displayPanel.add(imageLabel, BorderLayout.WEST);

        infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);
        displayPanel.add(new JScrollPane(infoArea), BorderLayout.CENTER);

        add(displayPanel, BorderLayout.CENTER);

        // Acción al pulsar el botón "Buscar"
        searchButton.addActionListener(e -> fetchPokemonByName());

        setVisible(true);
    }

    private void fetchPokemonByName() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Por favor, ingresa un nombre de Pokémon.");
            return;
        }

        try {
            // Usar el cliente para obtener los datos
            String jsonResponse = PokemonClient.getPokemonByName(name);

            // Extraer datos manualmente
            if (jsonResponse.contains("id")) {
                String nameValue = extractValue(jsonResponse, "name");
                String habitatValue = extractValue(jsonResponse, "habitat");
                String typePokemonValue = extractValue(jsonResponse, "typePokemon");
                String abilityValue = extractValue(jsonResponse, "ability");
                String weaknessValue = extractValue(jsonResponse, "weakness");
                String imageUrl = extractValue(jsonResponse, "imageUrl");

                // Mostrar información del Pokémon
                infoArea.setText(

                        "Nombre: " + nameValue + "\n" +
                        "Hábitat: " + habitatValue + "\n" +
                        "Tipo: " + typePokemonValue + "\n" +
                        "Habilidad: " + abilityValue + "\n" +
                        "Debilidad: " + weaknessValue);

                // Mostrar imagen si existe
                ImageIcon imageIcon = new ImageIcon(imageUrl);
                imageLabel.setIcon(new ImageIcon(imageIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
            } else {
                infoArea.setText("No se encontró ningún Pokémon con el nombre: " + name);
                imageLabel.setIcon(null);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            infoArea.setText("Error al obtener datos: " + ex.getMessage());
            imageLabel.setIcon(null);
        }
    }

    private String extractValue(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int startIndex = json.indexOf(searchKey) + searchKey.length();
        if (startIndex == -1) {
            return "";
        }
        int endIndex = json.indexOf(",", startIndex);
        if (endIndex == -1) {
            endIndex = json.indexOf("}", startIndex);
        }
        String value = json.substring(startIndex, endIndex).replace("\"", "").trim();
        return value;
    }

}
