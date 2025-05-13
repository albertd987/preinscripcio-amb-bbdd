package classes.vista;

import java.awt.Color;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class VistaUsuari extends JFrame {
    // Components per a la selecció d'alumne
    public JLabel lblAlumne;
    public JComboBox<String> comboAlumne;
    public JLabel lblNomAlumne;
    
    // Components per a les opcions de preinscripció
    public JLabel lblCentre;
    public JLabel lblEstudis;
    public JLabel lblOpcio;
    
    // Files de selecció
    public JLabel lblFila1;
    public JComboBox<String> comboCentre1;
    public JComboBox<String> comboEstudis1;
    public JComboBox<String> comboOpcio1;
    
    public JLabel lblFila2;
    public JComboBox<String> comboCentre2;
    public JComboBox<String> comboEstudis2;
    public JComboBox<String> comboOpcio2;
    
    public JLabel lblFila3;
    public JComboBox<String> comboCentre3;
    public JComboBox<String> comboEstudis3;
    public JComboBox<String> comboOpcio3;
    
    // Missatge d'error o èxit
    public JLabel lblMissatge;
    
    // Botons
    public JButton btnValidar;
    public JButton btnAccions;
    
    // Menú d'accions
    public JPopupMenu menuAccions;
    public JMenuItem itemAlta;
    public JMenuItem itemBaixa;
    public JMenuItem itemModificacio;
    public JMenuItem itemConsulta;
    public JMenuItem itemSortir;
    
    public VistaUsuari() {
        setTitle("Gestió de Preinscripcions");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 400);
        setLocationRelativeTo(null);
        
        // Inicialitzar components
        inicialitzarComponents();
        
        // Mostrar la vista
        setVisible(true);
    }
    
    private void inicialitzarComponents() {
        // Layout null per a controlar la posició dels components manualment
        getContentPane().setLayout(null);
        
        // Secció d'alumne
        lblAlumne = new JLabel("Alumne:");
        lblAlumne.setBounds(100, 30, 70, 25);
        getContentPane().add(lblAlumne);
        
        comboAlumne = new JComboBox<>();
        comboAlumne.setBounds(200, 30, 150, 25);
        getContentPane().add(comboAlumne);
        
        lblNomAlumne = new JLabel("Cognom1 Cognom2, Nom");
        lblNomAlumne.setBounds(360, 30, 200, 25);
        getContentPane().add(lblNomAlumne);
        
        // Capçaleres de les columnes
        lblCentre = new JLabel("Centre");
        lblCentre.setBounds(180, 70, 100, 25);
        getContentPane().add(lblCentre);
        
        lblEstudis = new JLabel("Estudis");
        lblEstudis.setBounds(390, 70, 100, 25);
        getContentPane().add(lblEstudis);
        
        lblOpcio = new JLabel("Opció");
        lblOpcio.setBounds(560, 70, 50, 25);
        getContentPane().add(lblOpcio);
        
        // Primera fila de selecció
        lblFila1 = new JLabel("1.");
        lblFila1.setBounds(30, 100, 20, 25);
        getContentPane().add(lblFila1);
        
        comboCentre1 = new JComboBox<>();
        comboCentre1.setBounds(60, 100, 250, 25);
        getContentPane().add(comboCentre1);
        
        comboEstudis1 = new JComboBox<>();
        comboEstudis1.setBounds(320, 100, 200, 25);
        getContentPane().add(comboEstudis1);
        
        comboOpcio1 = new JComboBox<>();
        comboOpcio1.setBounds(530, 100, 60, 25);
        getContentPane().add(comboOpcio1);
        
        // Segona fila de selecció
        lblFila2 = new JLabel("2.");
        lblFila2.setBounds(30, 140, 20, 25);
        getContentPane().add(lblFila2);
        
        comboCentre2 = new JComboBox<>();
        comboCentre2.setBounds(60, 140, 250, 25);
        getContentPane().add(comboCentre2);
        
        comboEstudis2 = new JComboBox<>();
        comboEstudis2.setBounds(320, 140, 200, 25);
        getContentPane().add(comboEstudis2);
        
        comboOpcio2 = new JComboBox<>();
        comboOpcio2.setBounds(530, 140, 60, 25);
        getContentPane().add(comboOpcio2);
        
        // Tercera fila de selecció
        lblFila3 = new JLabel("3.");
        lblFila3.setBounds(30, 180, 20, 25);
        getContentPane().add(lblFila3);
        
        comboCentre3 = new JComboBox<>();
        comboCentre3.setBounds(60, 180, 250, 25);
        getContentPane().add(comboCentre3);
        
        comboEstudis3 = new JComboBox<>();
        comboEstudis3.setBounds(320, 180, 200, 25);
        getContentPane().add(comboEstudis3);
        
        comboOpcio3 = new JComboBox<>();
        comboOpcio3.setBounds(530, 180, 60, 25);
        getContentPane().add(comboOpcio3);
        
        // Missatge d'error o èxit
        lblMissatge = new JLabel("Missatge d'èxit o error al validar");
        lblMissatge.setForeground(Color.RED);
        lblMissatge.setBounds(30, 240, 300, 25);
        getContentPane().add(lblMissatge);
        
        // Botó de validar
        btnValidar = new JButton("Validar");
        btnValidar.setBounds(530, 240, 90, 30);
        getContentPane().add(btnValidar);
        
        // Botó d'accions i menú d'accions
        btnAccions = new JButton("Accions");
        btnAccions.setBounds(10, 5, 100, 20);
        getContentPane().add(btnAccions);
        
        // Menú d'accions desplegable
        menuAccions = new JPopupMenu();
        
        itemAlta = new JMenuItem("Alta");
        itemBaixa = new JMenuItem("Baixa");
        itemModificacio = new JMenuItem("Modificació");
        itemConsulta = new JMenuItem("Consulta");
        itemSortir = new JMenuItem("Sortir");
        
        menuAccions.add(itemAlta);
        menuAccions.add(itemBaixa);
        menuAccions.add(itemModificacio);
        menuAccions.add(itemConsulta);
        menuAccions.addSeparator();
        menuAccions.add(itemSortir);
        
        // Listener per a mostrar el menú al fer clic al botó d'accions
        btnAccions.addActionListener(e -> {
            menuAccions.show(btnAccions, 0, btnAccions.getHeight());
        });
        
        // Inicialitzar combo d'opcions (1, 2, 3)
        String[] opcions = {"1", "2", "3"};
        comboOpcio1.setModel(new DefaultComboBoxModel<>(opcions));
        comboOpcio2.setModel(new DefaultComboBoxModel<>(opcions));
        comboOpcio3.setModel(new DefaultComboBoxModel<>(opcions));
    }
    
    // Mètode per afegir un alumne al comboBox
    public void afegirAlumne(String dni, String nom) {
        comboAlumne.addItem(dni);
    }
    
    // Mètode per afegir un centre al comboBox
    public void afegirCentre(String codi, String nom, JComboBox<String> combo) {
        combo.addItem(nom);
    }
    
    // Mètode per afegir un estudi al comboBox
    public void afegirEstudi(String codi, String nom, JComboBox<String> combo) {
        combo.addItem(nom);
    }
    
    // Mètode per netejar tots els camps
    public void netejarCamps() {
        comboAlumne.setSelectedIndex(-1);
        lblNomAlumne.setText("");
        
        comboCentre1.setSelectedIndex(-1);
        comboEstudis1.setSelectedIndex(-1);
        comboOpcio1.setSelectedIndex(-1);
        
        comboCentre2.setSelectedIndex(-1);
        comboEstudis2.setSelectedIndex(-1);
        comboOpcio2.setSelectedIndex(-1);
        
        comboCentre3.setSelectedIndex(-1);
        comboEstudis3.setSelectedIndex(-1);
        comboOpcio3.setSelectedIndex(-1);
        
        lblMissatge.setText("");
    }
    public static void main(String[] args) {
        new VistaUsuari().setVisible(true);
    }
}