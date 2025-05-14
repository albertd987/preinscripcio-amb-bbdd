package classes.controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import classes.model.Alumne;
import classes.model.Centre;
import classes.model.Estudi;
import classes.model.ModelUsuari;
import classes.model.Preinscripcio;
import classes.vista.VistaUsuari;

public class ControladorUsuari {
    private ModelUsuari model;
    private VistaUsuari vista;
    
    private Alumne alumneActual;
    private String modePantalla = "CONSULTA"; // ALTA, BAIXA, MODIFICACIO, CONSULTA
    private List<Preinscripcio> preinscripcionsAEliminar = new ArrayList<>();
    
    //variable aux x evitar un problema de recursió infinita amb el combobox
    private boolean actualitzantComboAlumne = false;
    
    public ControladorUsuari(ModelUsuari model, VistaUsuari vista) {
        this.model = model;
        this.vista = vista;

        configurarListeners();

        try {

            carregarCentres();
            

            carregarAlumnes();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void configurarListeners() {

        vista.comboAlumne.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED && 
                    vista.comboAlumne.getSelectedItem() != null && 
                    !actualitzantComboAlumne) {
                    try {
                        String dniSeleccionat = (String) vista.comboAlumne.getSelectedItem();
                        carregarDadesAlumne(dniSeleccionat);
                    } catch (Exception ex) {
                        System.err.println(ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            }
        });
        

        configurarListenerCentre(vista.comboCentre1, vista.comboEstudis1);
        configurarListenerCentre(vista.comboCentre2, vista.comboEstudis2);
        configurarListenerCentre(vista.comboCentre3, vista.comboEstudis3);
        

        vista.btnValidar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validarFormulari();
            }
        });
        

        vista.itemAlta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modePantalla = "ALTA";
                prepararPantallaAlta();
            }
        });
        
        vista.itemBaixa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modePantalla = "BAIXA";
                prepararPantallaBaixa();
            }
        });
        
        vista.itemModificacio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modePantalla = "MODIFICACIO";
                prepararPantallaModificacio();
            }
        });
        
        vista.itemConsulta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modePantalla = "CONSULTA";
                prepararPantallaConsulta();
            }
        });
        
        vista.itemSortir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
    
    private void configurarListenerCentre(JComboBox<String> comboCentre, JComboBox<String> comboEstudis) {
        comboCentre.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED && comboCentre.getSelectedItem() != null) {
                    String centreSel = (String) comboCentre.getSelectedItem();
                    carregarEstudisByCentre(centreSel, comboEstudis);
                }
            }
        });
    }
    
    private void carregarAlumnes() {
        try {
            vista.comboAlumne.removeAllItems();
            List<Alumne> alumnes = model.obtenirAlumnes();
            
            //Verificar que hi han alumnes
            if (alumnes == null || alumnes.isEmpty()) {
                System.out.println("No s'han trobat alumnes a la bbdd:");
                return;
            }
            
            for (Alumne alumne : alumnes) {
                vista.comboAlumne.addItem(alumne.getDni());
            }
            

            if (vista.comboAlumne.getItemCount() > 0) {
                actualitzantComboAlumne = true;
                vista.comboAlumne.setSelectedIndex(0);
                actualitzantComboAlumne = false;
                

                String primerDni = (String) vista.comboAlumne.getSelectedItem();
                if (primerDni != null) {
                    carregarDadesAlumne(primerDni);
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void carregarCentres() {
        try {

            vista.comboCentre1.removeAllItems();
            vista.comboCentre2.removeAllItems();
            vista.comboCentre3.removeAllItems();
            
            List<Centre> centres = model.obtenirCentres();

            if (centres == null || centres.isEmpty()) {
                System.out.println("No s'han trobat centres a la bbdd:");

                vista.comboCentre1.addItem("");
                vista.comboCentre2.addItem("");
                vista.comboCentre3.addItem("");
                return;
            }
            

            vista.comboCentre1.addItem("");
            vista.comboCentre2.addItem("");
            vista.comboCentre3.addItem("");
            
            for (Centre centre : centres) {
                vista.comboCentre1.addItem(centre.getNomCentre());
                vista.comboCentre2.addItem(centre.getNomCentre());
                vista.comboCentre3.addItem(centre.getNomCentre());
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void carregarEstudisByCentre(String nomCentre, JComboBox<String> comboEstudis) {
        try {
            comboEstudis.removeAllItems();
            
            if (nomCentre == null || nomCentre.isEmpty()) {
                comboEstudis.addItem("");
                return;
            }
            

            Centre centre = null;
            List<Centre> centres = model.obtenirCentres();
            for (Centre c : centres) {
                if (c.getNomCentre().equals(nomCentre)) {
                    centre = c;
                    break;
                }
            }
            
            if (centre == null) {

                comboEstudis.addItem("");
                return;
            }

            comboEstudis.addItem("");

            List<Estudi> estudis = model.obtenirEstudisByCentre(centre.getCodiCentre());
            

            if (estudis == null || estudis.isEmpty()) {
                System.out.println("No s'han trobat cicles a la bbdd: " + nomCentre);
                return;
            }
            
            for (Estudi estudi : estudis) {
                comboEstudis.addItem(estudi.getNomEstudi());
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void carregarDadesAlumne(String dni) {
        try {
            vista.lblNomAlumne.setText("");

            if (vista.comboCentre1.getItemCount() > 0) {
                vista.comboCentre1.setSelectedIndex(0);
            }
            
            if (vista.comboCentre2.getItemCount() > 0) {
                vista.comboCentre2.setSelectedIndex(0);
            }
            
            if (vista.comboCentre3.getItemCount() > 0) {
                vista.comboCentre3.setSelectedIndex(0);
            }
            
            vista.comboEstudis1.removeAllItems();
            vista.comboEstudis2.removeAllItems();
            vista.comboEstudis3.removeAllItems();
            
            if (vista.comboOpcio1.getItemCount() > 0) {
                vista.comboOpcio1.setSelectedIndex(-1);
            }
            
            if (vista.comboOpcio2.getItemCount() > 0) {
                vista.comboOpcio2.setSelectedIndex(-1);
            }
            
            if (vista.comboOpcio3.getItemCount() > 0) {
                vista.comboOpcio3.setSelectedIndex(-1);
            }
            
            vista.lblMissatge.setText("");
            

            alumneActual = model.obtenirAlumne(dni);
            if (alumneActual != null) {
                vista.lblNomAlumne.setText(alumneActual.getNomAlumne());
                
                List<Preinscripcio> preinscripcions = model.obtenirPreinscripcionsPerAlumne(dni);
                mostrarPreinscripcions(preinscripcions);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void mostrarPreinscripcions(List<Preinscripcio> preinscripcions) {
        try {
            // Netejar seccions previes en cas d'haver-hi elements
            if (vista.comboCentre1.getItemCount() > 0) {
                vista.comboCentre1.setSelectedIndex(0);
            }
            
            if (vista.comboCentre2.getItemCount() > 0) {
                vista.comboCentre2.setSelectedIndex(0);
            }
            
            if (vista.comboCentre3.getItemCount() > 0) {
                vista.comboCentre3.setSelectedIndex(0);
            }
            
            vista.comboEstudis1.removeAllItems();
            vista.comboEstudis2.removeAllItems();
            vista.comboEstudis3.removeAllItems();

            if (vista.comboOpcio1.getItemCount() > 0) {
                vista.comboOpcio1.setSelectedIndex(-1);
            }
            
            if (vista.comboOpcio2.getItemCount() > 0) {
                vista.comboOpcio2.setSelectedIndex(-1);
            }
            
            if (vista.comboOpcio3.getItemCount() > 0) {
                vista.comboOpcio3.setSelectedIndex(-1);
            }
            
            if (preinscripcions == null || preinscripcions.isEmpty()) {
                return;
            }
            

            for (Preinscripcio p : preinscripcions) {
                Centre centre = model.obtenirCentre(p.getCodiCentre());
                Estudi estudi = model.obtenirEstudi(p.getCodiEstudi());
                
                if (centre != null && estudi != null) {
                    switch (p.getPrioritat()) {
                        case 1:
                            mostrarPreinscripcioEnFila(vista.comboCentre1, vista.comboEstudis1, vista.comboOpcio1, 
                                    centre.getNomCentre(), estudi.getNomEstudi(), p.getPrioritat());
                            break;
                        case 2:
                            mostrarPreinscripcioEnFila(vista.comboCentre2, vista.comboEstudis2, vista.comboOpcio2, 
                                    centre.getNomCentre(), estudi.getNomEstudi(), p.getPrioritat());
                            break;
                        case 3:
                            mostrarPreinscripcioEnFila(vista.comboCentre3, vista.comboEstudis3, vista.comboOpcio3, 
                                    centre.getNomCentre(), estudi.getNomEstudi(), p.getPrioritat());
                            break;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void mostrarPreinscripcioEnFila(JComboBox<String> comboCentre, JComboBox<String> comboEstudis, 
            JComboBox<String> comboOpcio, String nomCentre, String nomEstudi, int prioritat) {
        try {
            if (comboCentre.getItemCount() > 0) {
                comboCentre.setSelectedItem(nomCentre);
            }

            boolean trobat = false;
            for (int i = 0; i < comboEstudis.getItemCount(); i++) {
                if (comboEstudis.getItemAt(i).equals(nomEstudi)) {
                    trobat = true;
                    comboEstudis.setSelectedIndex(i);
                    break;
                }
            }
            
            if (!trobat) {
                comboEstudis.addItem(nomEstudi);
                comboEstudis.setSelectedItem(nomEstudi);
            }
            
            if (comboOpcio.getItemCount() > 0) {
                comboOpcio.setSelectedItem(String.valueOf(prioritat));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void prepararPantallaAlta() {
        vista.setModePantalla("ALTA");
        vista.lblMissatge.setText("Selecciona un alumne i completa les dades per a la preinscripció");

        vista.lblNomAlumne.setText("");
        

        if (vista.comboCentre1.getItemCount() > 0) {
            vista.comboCentre1.setSelectedIndex(0);
        }
        
        if (vista.comboCentre2.getItemCount() > 0) {
            vista.comboCentre2.setSelectedIndex(0);
        }
        
        if (vista.comboCentre3.getItemCount() > 0) {
            vista.comboCentre3.setSelectedIndex(0);
        }
        
        vista.comboEstudis1.removeAllItems();
        vista.comboEstudis2.removeAllItems();
        vista.comboEstudis3.removeAllItems();
        

        if (vista.comboOpcio1.getItemCount() > 0) {
            vista.comboOpcio1.setSelectedIndex(-1);
        }
        
        if (vista.comboOpcio2.getItemCount() > 0) {
            vista.comboOpcio2.setSelectedIndex(-1);
        }
        
        if (vista.comboOpcio3.getItemCount() > 0) {
            vista.comboOpcio3.setSelectedIndex(-1);
        }
        

        vista.btnValidar.setText("Crear");

        if (alumneActual != null) {
            String dni = alumneActual.getDni();
            carregarDadesAlumne(dni);
        }
    }
    
    private void prepararPantallaBaixa() {
        vista.setModePantalla("BAIXA");
        vista.lblMissatge.setText("Selecciona un alumne i una preinscripció per eliminar");

        vista.lblNomAlumne.setText("");
        
        if (vista.comboCentre1.getItemCount() > 0) {
            vista.comboCentre1.setSelectedIndex(0);
        }
        
        if (vista.comboCentre2.getItemCount() > 0) {
            vista.comboCentre2.setSelectedIndex(0);
        }
        
        if (vista.comboCentre3.getItemCount() > 0) {
            vista.comboCentre3.setSelectedIndex(0);
        }
        
        vista.comboEstudis1.removeAllItems();
        vista.comboEstudis2.removeAllItems();
        vista.comboEstudis3.removeAllItems();

        if (vista.comboOpcio1.getItemCount() > 0) {
            vista.comboOpcio1.setSelectedIndex(-1);
        }
        
        if (vista.comboOpcio2.getItemCount() > 0) {
            vista.comboOpcio2.setSelectedIndex(-1);
        }
        
        if (vista.comboOpcio3.getItemCount() > 0) {
            vista.comboOpcio3.setSelectedIndex(-1);
        }

        vista.btnValidar.setText("Eliminar");

        if (alumneActual != null) {
            String dni = alumneActual.getDni();
            carregarDadesAlumne(dni);
        }
    }
    
    private void prepararPantallaModificacio() {
        vista.setModePantalla("MODIFICACIÓ");
        vista.lblMissatge.setText("Selecciona un alumne i modifica les seves preinscripcions");

        vista.lblNomAlumne.setText("");

        if (vista.comboCentre1.getItemCount() > 0) {
            vista.comboCentre1.setSelectedIndex(0);
        }
        
        if (vista.comboCentre2.getItemCount() > 0) {
            vista.comboCentre2.setSelectedIndex(0);
        }
        
        if (vista.comboCentre3.getItemCount() > 0) {
            vista.comboCentre3.setSelectedIndex(0);
        }
        
        vista.comboEstudis1.removeAllItems();
        vista.comboEstudis2.removeAllItems();
        vista.comboEstudis3.removeAllItems();

        if (vista.comboOpcio1.getItemCount() > 0) {
            vista.comboOpcio1.setSelectedIndex(-1);
        }
        
        if (vista.comboOpcio2.getItemCount() > 0) {
            vista.comboOpcio2.setSelectedIndex(-1);
        }
        
        if (vista.comboOpcio3.getItemCount() > 0) {
            vista.comboOpcio3.setSelectedIndex(-1);
        }
        
        vista.btnValidar.setText("Modificar");

        if (alumneActual != null) {
            String dni = alumneActual.getDni();
            carregarDadesAlumne(dni);
        }
    }
    
    private void prepararPantallaConsulta() {
        vista.setModePantalla("CONSULTA");
        vista.lblMissatge.setText("Selecciona un alumne per veure les seves preinscripcions");

        vista.lblNomAlumne.setText("");

        if (vista.comboCentre1.getItemCount() > 0) {
            vista.comboCentre1.setSelectedIndex(0);
        }
        
        if (vista.comboCentre2.getItemCount() > 0) {
            vista.comboCentre2.setSelectedIndex(0);
        }
        
        if (vista.comboCentre3.getItemCount() > 0) {
            vista.comboCentre3.setSelectedIndex(0);
        }
        
        vista.comboEstudis1.removeAllItems();
        vista.comboEstudis2.removeAllItems();
        vista.comboEstudis3.removeAllItems();

        if (vista.comboOpcio1.getItemCount() > 0) {
            vista.comboOpcio1.setSelectedIndex(-1);
        }
        
        if (vista.comboOpcio2.getItemCount() > 0) {
            vista.comboOpcio2.setSelectedIndex(-1);
        }
        
        if (vista.comboOpcio3.getItemCount() > 0) {
            vista.comboOpcio3.setSelectedIndex(-1);
        }
        

        vista.btnValidar.setText("Consultar");
        

        if (alumneActual != null) {
            String dni = alumneActual.getDni();
            carregarDadesAlumne(dni);
        }
    }
    
    private void validarFormulari() {
        if (alumneActual == null) {
            mostrarError("Cal seleccionar un alumne");
            return;
        }
        
        switch (modePantalla) {
            case "ALTA":
                processarAlta();
                break;
            case "BAIXA":
                processarBaixa();
                break;
            case "MODIFICACIO":
                processarModificacio();
                break;
            case "CONSULTA":
                carregarDadesAlumne(alumneActual.getDni());
                break;
        }
    }
    
    private void processarAlta() {

        if (vista.comboCentre1.getSelectedIndex() <= 0 || vista.comboEstudis1.getSelectedIndex() <= 0 ||
            vista.comboOpcio1.getSelectedIndex() < 0) {
            mostrarError("Cal completar almenys la primera fila amb centre, estudi i prioritat");
            return;
        }
        

        processarFilaPreinscripcio(vista.comboCentre1, vista.comboEstudis1, vista.comboOpcio1);
        processarFilaPreinscripcio(vista.comboCentre2, vista.comboEstudis2, vista.comboOpcio2);
        processarFilaPreinscripcio(vista.comboCentre3, vista.comboEstudis3, vista.comboOpcio3);
        

        carregarDadesAlumne(alumneActual.getDni());
        vista.lblMissatge.setText("Preinscripcions guardades correctament");
    }
    
    private void processarFilaPreinscripcio(JComboBox<String> comboCentre, JComboBox<String> comboEstudis, 
            JComboBox<String> comboOpcio) {
        

        if (comboCentre.getSelectedIndex() <= 0 || comboEstudis.getSelectedIndex() <= 0 || 
            comboOpcio.getSelectedIndex() < 0) {
            return;
        }
        
        String nomCentre = (String) comboCentre.getSelectedItem();
        String nomEstudi = (String) comboEstudis.getSelectedItem();
        int prioritat = Integer.parseInt((String) comboOpcio.getSelectedItem());

        String codiCentre = obtenirCodiCentre(nomCentre);
        String codiEstudi = obtenirCodiEstudi(nomEstudi);
        
        if (codiCentre == null || codiEstudi == null) {
            mostrarError("Error al obtenir els codis de centre o estudi");
            return;
        }
        

        Preinscripcio preinscripcio = new Preinscripcio(codiEstudi, codiCentre, alumneActual.getDni(), 
                "2025-2026", prioritat);
        

        if (model.existeixPreinscripcio(codiEstudi, codiCentre, alumneActual.getDni())) {

            model.modificarPreinscripcio(preinscripcio);
        } else {

            boolean resultat = model.altaPreinscripcio(preinscripcio);
            if (!resultat) {
                mostrarError("Error al crear la preinscripció. Comprova que no existeixi ja una preinscripció amb la mateixa prioritat.");
            }
        }
    }
    
    private void processarBaixa() {

        preinscripcionsAEliminar.clear();
        
        afegirPreinscripcioSiSeleccionada(vista.comboCentre1, vista.comboEstudis1, preinscripcionsAEliminar);
        afegirPreinscripcioSiSeleccionada(vista.comboCentre2, vista.comboEstudis2, preinscripcionsAEliminar);
        afegirPreinscripcioSiSeleccionada(vista.comboCentre3, vista.comboEstudis3, preinscripcionsAEliminar);
        
        if (preinscripcionsAEliminar.isEmpty()) {
            mostrarError("Cal seleccionar almenys una preinscripció per eliminar");
            return;
        }
        

        int resposta = JOptionPane.showConfirmDialog(vista, 
                "Estàs segur que vols eliminar " + preinscripcionsAEliminar.size() + " preinscripcions?",
                "Confirmació", JOptionPane.YES_NO_OPTION);
        
        if (resposta == JOptionPane.YES_OPTION) {

            for (Preinscripcio p : preinscripcionsAEliminar) {
                model.baixaPreinscripcio(p.getCodiEstudi(), p.getCodiCentre(), p.getDni());
            }
            

            carregarDadesAlumne(alumneActual.getDni());
            vista.lblMissatge.setText("Preinscripcions eliminades correctament");
        }
    }
    
    private void afegirPreinscripcioSiSeleccionada(JComboBox<String> comboCentre, JComboBox<String> comboEstudis, 
            List<Preinscripcio> preinscripcions) {
        
        if (comboCentre.getSelectedIndex() <= 0 || comboEstudis.getSelectedIndex() <= 0) {
            return;
        }
        
        String nomCentre = (String) comboCentre.getSelectedItem();
        String nomEstudi = (String) comboEstudis.getSelectedItem();
        
        String codiCentre = obtenirCodiCentre(nomCentre);
        String codiEstudi = obtenirCodiEstudi(nomEstudi);
        
        if (codiCentre != null && codiEstudi != null) {
            Preinscripcio p = model.obtenirPreinscripcio(codiEstudi, codiCentre, alumneActual.getDni());
            if (p != null) {
                preinscripcions.add(p);
            }
        }
    }
    
    private void processarModificacio() {
        // Validar que hi ha com a mínim una fila complerta
        if (vista.comboCentre1.getSelectedIndex() <= 0 || vista.comboEstudis1.getSelectedIndex() <= 0 ||
            vista.comboOpcio1.getSelectedIndex() < 0) {
            mostrarError("Cal completar almenys la primera fila amb centre, estudi i prioritat");
            return;
        }
        
        // Obtener preinscripcions actuals
        List<Preinscripcio> preinscripcionsActuals = model.obtenirPreinscripcionsPerAlumne(alumneActual.getDni());
        
        // Processar cada fila vàlida (actualizar o crear)
        processarFilaPreinscripcio(vista.comboCentre1, vista.comboEstudis1, vista.comboOpcio1);
        processarFilaPreinscripcio(vista.comboCentre2, vista.comboEstudis2, vista.comboOpcio2);
        processarFilaPreinscripcio(vista.comboCentre3, vista.comboEstudis3, vista.comboOpcio3);

        carregarDadesAlumne(alumneActual.getDni());
        vista.lblMissatge.setText("Preinscripcions actualitzades correctament");
    }
    
    private String obtenirCodiCentre(String nomCentre) {
        List<Centre> centres = model.obtenirCentres();
        for (Centre centre : centres) {
            if (centre.getNomCentre().equals(nomCentre)) {
                return centre.getCodiCentre();
            }
        }
        return null;
    }
    
    private String obtenirCodiEstudi(String nomEstudi) {
        List<Estudi> estudis = model.obtenirEstudis();
        for (Estudi estudi : estudis) {
            if (estudi.getNomEstudi().equals(nomEstudi)) {
                return estudi.getCodiEstudi();
            }
        }
        return null;
    }
    
    private void mostrarError(String missatge) {
        vista.lblMissatge.setText(missatge);
        JOptionPane.showMessageDialog(vista, missatge, "Error", JOptionPane.ERROR_MESSAGE);
    }
}