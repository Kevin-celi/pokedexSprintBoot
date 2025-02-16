package ec.edu.uce.pokedexsprintboot.interfasGrafica;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ec.edu.uce.pokedexsprintboot.interfasGrafica.IgBusquedaTipo;
import ec.edu.uce.pokedexsprintboot.interfasGrafica.IgBusquedaNombre;

public class IgBienvenida extends JFrame implements ActionListener {
    private JButton button, optionsButtonTwo, optionsButtonThree;

    public IgBienvenida() {
        // Configurar el marco principal
        setTitle("Bienvenida");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 700);

        // Crear el panel principal con un JLabel para fondo
        JPanel panel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon icon = new ImageIcon("src/wallpapers/pikacho-detectve.png");
                g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };

        // Crear el boton para mostrar la interfas de busquedas (Nombre)
        button = new JButton("Busqueda pokemon");
        button.setBounds(25, 60, 150, 20);
        button.addActionListener(this); // Registrar el evento
        panel.add(button);

        // Crear el boton para mostrar la interfas de busquedas (Tipos)
        optionsButtonTwo = new JButton("tipo-pokemon");
        optionsButtonTwo.setBounds(25, 100, 150, 20);
        optionsButtonTwo.addActionListener(this); // Registrar el evento
        panel.add(optionsButtonTwo);


        // Añadir el panel al marco
        add(panel);

        // Configurar la ventana
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla
        setVisible(true);// Hacer visible la ventana
    }


    @Override
    public void actionPerformed(ActionEvent e) {
      //
        if (e.getSource() == button) {
            // Acción para el botón "Presionar"
            IgBusquedaNombre app = new IgBusquedaNombre();
        } else if (e.getSource() == optionsButtonTwo) {
            // Acción para el JComboBox "Busqueda Tipos"
            IgBusquedaTipo app = new IgBusquedaTipo();
       }
    }
}
