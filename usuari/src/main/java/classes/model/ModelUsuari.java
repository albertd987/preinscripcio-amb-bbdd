package classes.model;

import java.util.List;

import classes.dao.AlumneDAO;
import classes.dao.CentreDAO;
import classes.dao.CentreEstudiDAO;
import classes.dao.EstudiDAO;
import classes.dao.PreinscripcioDAO;

public class ModelUsuari {
    private AlumneDAO alumneDAO;
    private CentreDAO centreDAO;
    private EstudiDAO estudiDAO;
    private CentreEstudiDAO centreEstudiDAO;
    private PreinscripcioDAO preinscripcioDAO;

    public ModelUsuari() {
        this.alumneDAO = new AlumneDAO();
        this.centreDAO = new CentreDAO();
        this.estudiDAO = new EstudiDAO();
        this.centreEstudiDAO = new CentreEstudiDAO();
        this.preinscripcioDAO = new PreinscripcioDAO();
    }

    public boolean existeixPreinscripcio(String codiEstudi, String codiCentre, String dni) {
        return preinscripcioDAO.existeixPreinscripcio(codiEstudi, codiCentre, dni);
    }

    public Preinscripcio obtenirPreinscripcio(String codiEstudi, String codiCentre, String dni) {
        return preinscripcioDAO.obtenirPreinscripcio(codiEstudi, codiCentre, dni);
    }

    public int comptarPreinscripcionsPerAlumne(String dni) {
        return preinscripcioDAO.comptarPreinscripcionsPerAlumne(dni);
    }

    public List<Alumne> obtenirAlumnes() {
        return alumneDAO.obtenirTots();
    }

    public List<Centre> obtenirCentres() {
        return centreDAO.obtenirTots();
    }

    public List<Estudi> obtenirEstudis() {
        return estudiDAO.obtenirTots();
    }

    public List<CentreEstudi> obtenirCentreEstudis() {
        return centreEstudiDAO.obtenirTots();
    }

    public List<Preinscripcio> obtenirPreinscripcionsPerAlumne(String dni) {
        return preinscripcioDAO.obtenirPerAlumne(dni);
    }

    public List<Estudi> obtenirEstudisByCentre(String codiCentre) {
        return centreEstudiDAO.obtenirEstudisByCentre(codiCentre);
    }

    public List<Centre> obtenirCentresByEstudi(String codiEstudi) {
        return centreEstudiDAO.obtenirCentresByEstudi(codiEstudi);
    }

    public boolean altaPreinscripcio(Preinscripcio preinscripcio) {

        if (preinscripcio.getPrioritat() < 1 || preinscripcio.getPrioritat() > 3) {
            return false;
        }

        if (preinscripcioDAO.existeixPreinscripcio(preinscripcio.getCodiEstudi(),
                preinscripcio.getCodiCentre(),
                preinscripcio.getDni())) {
            return false;
        }

        if (preinscripcioDAO.existeixPreinscripcioMateixaPrioritat(preinscripcio.getDni(),
                preinscripcio.getPrioritat())) {
            return false;
        }

        return preinscripcioDAO.inserir(preinscripcio);
    }

    public boolean baixaPreinscripcio(String codiEstudi, String codiCentre, String dni) {
        return preinscripcioDAO.eliminar(codiEstudi, codiCentre, dni);
    }

    public boolean modificarPreinscripcio(Preinscripcio preinscripcio) {

        if (preinscripcio.getPrioritat() < 1 || preinscripcio.getPrioritat() > 3) {
            return false;
        }

        if (preinscripcioDAO.existeixAltrePreinscripcioMateixaPrioritat(
                preinscripcio.getDni(),
                preinscripcio.getPrioritat(),
                preinscripcio.getCodiEstudi(),
                preinscripcio.getCodiCentre())) {
            return false;
        }

        return preinscripcioDAO.actualitzar(preinscripcio);
    }

    public Alumne obtenirAlumne(String dni) {
        return alumneDAO.obtenirPerDni(dni);
    }

    public Centre obtenirCentre(String codiCentre) {
        return centreDAO.obtenirPerCodi(codiCentre);
    }

    public Estudi obtenirEstudi(String codiEstudi) {
        return estudiDAO.obtenirPerCodi(codiEstudi);
    }
}