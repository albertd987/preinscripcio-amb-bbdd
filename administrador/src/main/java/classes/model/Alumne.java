package classes.model;


public class Alumne {
    private String dni;
    private String nomAlumne;
    
    public Alumne(String dni, String nomAlumne) {
        this.dni = dni;
        this.nomAlumne = nomAlumne;
    }
    
    public String getDni() {
        return dni;
    }
    
    public void setDni(String dni) {
        this.dni = dni;
    }
    
    public String getNomAlumne() {
        return nomAlumne;
    }
    
    public void setNomAlumne(String nomAlumne) {
        this.nomAlumne = nomAlumne;
    }
    
    @Override
    public String toString() {
        return dni + " - " + nomAlumne;
    }
}