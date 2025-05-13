package classes.model;

public class Preinscripcio {
    private String codiEstudi;
    private String codiCentre;
    private String dni;
    private String curs;
    private int prioritat;
    
    public Preinscripcio(String codiEstudi, String codiCentre, String dni, String curs, int prioritat) {
        this.codiEstudi = codiEstudi;
        this.codiCentre = codiCentre;
        this.dni = dni;
        this.curs = curs;
        this.prioritat = prioritat;
    }
    
    public String getCodiEstudi() {
        return codiEstudi;
    }
    
    public void setCodiEstudi(String codiEstudi) {
        this.codiEstudi = codiEstudi;
    }
    
    public String getCodiCentre() {
        return codiCentre;
    }
    
    public void setCodiCentre(String codiCentre) {
        this.codiCentre = codiCentre;
    }
    
    public String getDni() {
        return dni;
    }
    
    public void setDni(String dni) {
        this.dni = dni;
    }
    
    public String getCurs() {
        return curs;
    }
    
    public void setCurs(String curs) {
        this.curs = curs;
    }
    
    public int getPrioritat() {
        return prioritat;
    }
    
    public void setPrioritat(int prioritat) {
        this.prioritat = prioritat;
    }
    
    @Override
    public String toString() {
        return "Preinscripcio{" +
                "codiEstudi='" + codiEstudi + '\'' +
                ", codiCentre='" + codiCentre + '\'' +
                ", dni='" + dni + '\'' +
                ", curs='" + curs + '\'' +
                ", prioritat=" + prioritat +
                '}';
    }
}