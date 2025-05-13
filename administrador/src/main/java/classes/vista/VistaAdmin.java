package classes.vista;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
public class VistaAdmin extends JFrame {
    public JButton btnCarregar;
    public JButton btnSortir;
    public JComboBox<String> comboFitxer;
    public JLabel lblError;
    public JLabel lblFitxer;

    public VistaAdmin() {
        initComponents();
    }

    private void initComponents() {
        lblFitxer = new JLabel();
        comboFitxer = new JComboBox<>();
        btnCarregar = new JButton();
        btnSortir = new JButton();
        lblError = new JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        lblFitxer.setText("Fitxer:");
        getContentPane().add(lblFitxer);
        lblFitxer.setBounds(20, 20, 60, 25);


        getContentPane().add(comboFitxer);
        comboFitxer.setBounds(90, 20, 200, 25);

        btnCarregar.setText("Carregar");
        getContentPane().add(btnCarregar);
        btnCarregar.setBounds(20, 100, 100, 30);

        btnSortir.setText("Sortir");
        getContentPane().add(btnSortir);
        btnSortir.setBounds(190, 100, 100, 30);

        lblError.setForeground(new java.awt.Color(255, 0, 0));
        getContentPane().add(lblError);
        lblError.setBounds(20, 60, 270, 25);

        setSize(320, 180);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
