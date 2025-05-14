package classes.controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
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
    
    // Estado actual
    private Alumne alumneActual;
    private String modePantalla = "CONSULTA"; // ALTA, BAIXA, MODIFICACIO, CONSULTA
    private List<Preinscripcio> preinscripcionsAEliminar = new ArrayList<>();
    
    public ControladorUsuari(ModelUsuari model, VistaUsuari vista) {
        this.model = model;
        this.vista = vista;
        
        // Cargar datos iniciales en la vista
        carregarAlumnes();
        carregarCentres();
        
        // Configurar listeners para la vista principal
        configurarListeners();
    }
    
    private void configurarListeners() {
        // Listener para el combo de alumnos
        vista.comboAlumne.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED && vista.comboAlumne.getSelectedItem() != null) {
                    String dniSeleccionat = (String) vista.comboAlumne.getSelectedItem();
                    carregarDadesAlumne(dniSeleccionat);
                }
            }
        });
        
        // Listeners para los combos de centros
        configurarListenerCentre(vista.comboCentre1, vista.comboEstudis1);
        configurarListenerCentre(vista.comboCentre2, vista.comboEstudis2);
        configurarListenerCentre(vista.comboCentre3, vista.comboEstudis3);
        
        // Listener para el botón de validar
        vista.btnValidar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validarFormulari();
            }
        });
        
        // Listeners para los items del menú de acciones
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
        vista.comboAlumne.removeAllItems();
        List<Alumne> alumnes = model.obtenirAlumnes();
        
        for (Alumne alumne : alumnes) {
            vista.comboAlumne.addItem(alumne.getDni());
        }
        
        if (vista.comboAlumne.getItemCount() > 0) {
            vista.comboAlumne.setSelectedIndex(0);
        }
    }
    
    private void carregarCentres() {
        // Limpiar combos
        vista.comboCentre1.removeAllItems();
        vista.comboCentre2.removeAllItems();
        vista.comboCentre3.removeAllItems();
        
        List<Centre> centres = model.obtenirCentres();
        
        // Añadir un item vacío al principio
        vista.comboCentre1.addItem("");
        vista.comboCentre2.addItem("");
        vista.comboCentre3.addItem("");
        
        for (Centre centre : centres) {
            vista.comboCentre1.addItem(centre.getNomCentre());
            vista.comboCentre2.addItem(centre.getNomCentre());
            vista.comboCentre3.addItem(centre.getNomCentre());
        }
    }
    
    private void carregarEstudisByCentre(String nomCentre, JComboBox<String> comboEstudis) {
        comboEstudis.removeAllItems();
        
        if (nomCentre == null || nomCentre.isEmpty()) {
            return;
        }
        
        // Buscar el centro por nombre
        Centre centre = null;
        List<Centre> centres = model.obtenirCentres();
        for (Centre c : centres) {
            if (c.getNomCentre().equals(nomCentre)) {
                centre = c;
                break;
            }
        }
        
        if (centre == null) {
            return;
        }
        
        // Añadir un item vacío al principio
        comboEstudis.addItem("");
        
        // Cargar estudios del centro
        List<Estudi> estudis = model.obtenirEstudisByCentre(centre.getCodiCentre());
        for (Estudi estudi : estudis) {
            comboEstudis.addItem(estudi.getNomEstudi());
        }
    }
    
    private void carregarDadesAlumne(String dni) {
        // Limpiar datos previos
        vista.netejarCamps();
        vista.comboAlumne.setSelectedItem(dni);
        
        // Obtener datos del alumno
        alumneActual = model.obtenirAlumne(dni);
        if (alumneActual != null) {
            vista.lblNomAlumne.setText(alumneActual.getNomAlumne());
            
            // Obtener las preinscripciones del alumno
            List<Preinscripcio> preinscripcions = model.obtenirPreinscripcionsPerAlumne(dni);
            
            // Mostrar preinscripciones en la vista
            mostrarPreinscripcions(preinscripcions);
        }
    }
    
    private void mostrarPreinscripcions(List<Preinscripcio> preinscripcions) {
        // Limpiar selecciones previas
        vista.comboCentre1.setSelectedIndex(0);
        vista.comboCentre2.setSelectedIndex(0);
        vista.comboCentre3.setSelectedIndex(0);
        vista.comboEstudis1.removeAllItems();
        vista.comboEstudis2.removeAllItems();
        vista.comboEstudis3.removeAllItems();
        vista.comboOpcio1.setSelectedIndex(-1);
        vista.comboOpcio2.setSelectedIndex(-1);
        vista.comboOpcio3.setSelectedIndex(-1);
        
        // Mostrar preinscripciones ordenadas por prioridad
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
    }
    
    private void mostrarPreinscripcioEnFila(JComboBox<String> comboCentre, JComboBox<String> comboEstudis, 
            JComboBox<String> comboOpcio, String nomCentre, String nomEstudi, int prioritat) {
        
        comboCentre.setSelectedItem(nomCentre);
        
        // Asegurarse de que el comboEstudis tenga el elemento
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
        
        comboOpcio.setSelectedItem(String.valueOf(prioritat));
    }
    
    private void prepararPantallaAlta() {
        vista.setModePantalla("ALTA");
        vista.lblMissatge.setText("Selecciona un alumne i completa les dades per a la preinscripció");
        
        // Limpiar selecciones
        vista.netejarCamps();
        
        // Cargar alumnos y centros
        carregarAlumnes();
        carregarCentres();
        
        // Configurar botón validar
        vista.btnValidar.setText("Crear");
    }
    
    private void prepararPantallaBaixa() {
        vista.setModePantalla("BAIXA");
        vista.lblMissatge.setText("Selecciona un alumne i una preinscripció per eliminar");
        
        // Limpiar selecciones
        vista.netejarCamps();
        
        // Cargar alumnos
        carregarAlumnes();
        
        // Configurar botón validar
        vista.btnValidar.setText("Eliminar");
    }
    
    private void prepararPantallaModificacio() {
        vista.setModePantalla("MODIFICACIÓ");
        vista.lblMissatge.setText("Selecciona un alumne i modifica les seves preinscripcions");
        
        // Limpiar selecciones
        vista.netejarCamps();
        
        // Cargar alumnos
        carregarAlumnes();
        
        // Configurar botón validar
        vista.btnValidar.setText("Modificar");
    }
    
    private void prepararPantallaConsulta() {
        vista.setModePantalla("CONSULTA");
        vista.lblMissatge.setText("Selecciona un alumne per veure les seves preinscripcions");
        
        // Limpiar selecciones
        vista.netejarCamps();
        
        // Cargar alumnos
        carregarAlumnes();
        
        // Configurar botón validar
        vista.btnValidar.setText("Consultar");
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
        // Validar que hay al menos una fila completa
        if (vista.comboCentre1.getSelectedIndex() <= 0 || vista.comboEstudis1.getSelectedIndex() <= 0 ||
            vista.comboOpcio1.getSelectedIndex() < 0) {
            mostrarError("Cal completar almenys la primera fila amb centre, estudi i prioritat");
            return;
        }
        
        // Procesar cada fila válida
        processarFilaPreinscripcio(vista.comboCentre1, vista.comboEstudis1, vista.comboOpcio1);
        processarFilaPreinscripcio(vista.comboCentre2, vista.comboEstudis2, vista.comboOpcio2);
        processarFilaPreinscripcio(vista.comboCentre3, vista.comboEstudis3, vista.comboOpcio3);
        
        // Refrescar datos
        carregarDadesAlumne(alumneActual.getDni());
        vista.lblMissatge.setText("Preinscripcions guardades correctament");
    }
    
    private void processarFilaPreinscripcio(JComboBox<String> comboCentre, JComboBox<String> comboEstudis, 
            JComboBox<String> comboOpcio) {
        
        // Si alguno de los combos no tiene selección, ignoramos esta fila
        if (comboCentre.getSelectedIndex() <= 0 || comboEstudis.getSelectedIndex() <= 0 || 
            comboOpcio.getSelectedIndex() < 0) {
            return;
        }
        
        String nomCentre = (String) comboCentre.getSelectedItem();
        String nomEstudi = (String) comboEstudis.getSelectedItem();
        int prioritat = Integer.parseInt((String) comboOpcio.getSelectedItem());
        
        // Buscar códigos de centro y estudio
        String codiCentre = obtenirCodiCentre(nomCentre);
        String codiEstudi = obtenirCodiEstudi(nomEstudi);
        
        if (codiCentre == null || codiEstudi == null) {
            mostrarError("Error al obtenir els codis de centre o estudi");
            return;
        }
        
        // Crear preinscripción
        Preinscripcio preinscripcio = new Preinscripcio(codiEstudi, codiCentre, alumneActual.getDni(), 
                "2025-2026", prioritat);
        
        // Guardar en la base de datos
        if (model.existeixPreinscripcio(codiEstudi, codiCentre, alumneActual.getDni())) {
            // Si ya existe, actualizamos
            model.modificarPreinscripcio(preinscripcio);
        } else {
            // Si no existe, creamos nueva
            boolean resultat = model.altaPreinscripcio(preinscripcio);
            if (!resultat) {
                mostrarError("Error al crear la preinscripció. Comprova que no existeixi ja una preinscripció amb la mateixa prioritat.");
            }
        }
    }
    
    private void processarBaixa() {
        // Limpiar lista anterior
        preinscripcionsAEliminar.clear();
        
        afegirPreinscripcioSiSeleccionada(vista.comboCentre1, vista.comboEstudis1, preinscripcionsAEliminar);
        afegirPreinscripcioSiSeleccionada(vista.comboCentre2, vista.comboEstudis2, preinscripcionsAEliminar);
        afegirPreinscripcioSiSeleccionada(vista.comboCentre3, vista.comboEstudis3, preinscripcionsAEliminar);
        
        if (preinscripcionsAEliminar.isEmpty()) {
            mostrarError("Cal seleccionar almenys una preinscripció per eliminar");
            return;
        }
        
        // Confirmar eliminación
        int resposta = JOptionPane.showConfirmDialog(vista, 
                "Estàs segur que vols eliminar " + preinscripcionsAEliminar.size() + " preinscripcions?",
                "Confirmació", JOptionPane.YES_NO_OPTION);
        
        if (resposta == JOptionPane.YES_OPTION) {
            // Eliminar preinscripciones
            for (Preinscripcio p : preinscripcionsAEliminar) {
                model.baixaPreinscripcio(p.getCodiEstudi(), p.getCodiCentre(), p.getDni());
            }
            
            // Refrescar datos
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
        
        // Buscar códigos de centro y estudio
        String codiCentre = obtenirCodiCentre(nomCentre);
        String codiEstudi = obtenirCodiEstudi(nomEstudi);
        
        if (codiCentre != null && codiEstudi != null) {
            // Verificar que la preinscripción existe
            Preinscripcio p = model.obtenirPreinscripcio(codiEstudi, codiCentre, alumneActual.getDni());
            if (p != null) {
                preinscripcions.add(p);
            }
        }
    }
    
    private void processarModificacio() {
        // Validar que hay al menos una fila completa
        if (vista.comboCentre1.getSelectedIndex() <= 0 || vista.comboEstudis1.getSelectedIndex() <= 0 ||
            vista.comboOpcio1.getSelectedIndex() < 0) {
            mostrarError("Cal completar almenys la primera fila amb centre, estudi i prioritat");
            return;
        }
        
        // Obtener las preinscripciones actuales
        List<Preinscripcio> preinscripcionsActuals = model.obtenirPreinscripcionsPerAlumne(alumneActual.getDni());
        
        // Procesar cada fila válida (actualizar o crear)
        processarFilaPreinscripcio(vista.comboCentre1, vista.comboEstudis1, vista.comboOpcio1);
        processarFilaPreinscripcio(vista.comboCentre2, vista.comboEstudis2, vista.comboOpcio2);
        processarFilaPreinscripcio(vista.comboCentre3, vista.comboEstudis3, vista.comboOpcio3);
        
        // Refrescar datos
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