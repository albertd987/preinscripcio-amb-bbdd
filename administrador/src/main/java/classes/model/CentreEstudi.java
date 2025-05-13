package classes.model;


/**
 * Classe que representa la relació entre un centre i un estudi.
 * Conté el codi del centre i el codi de l'estudi.
 */
public class CentreEstudi {
    private String codiCentre;
    private String codiEstudi;
    
    public CentreEstudi(String codiCentre, String codiEstudi) {
        this.codiCentre = codiCentre;
        this.codiEstudi = codiEstudi;
    }
    
    public String getCodiCentre() {
        return codiCentre;
    }
    
    public void setCodiCentre(String codiCentre) {
        this.codiCentre = codiCentre;
    }
    
    public String getCodiEstudi() {
        return codiEstudi;
    }
    
    public void setCodiEstudi(String codiEstudi) {
        this.codiEstudi = codiEstudi;
    }
    
    @Override
    public String toString() {
        return "Centre: " + codiCentre + ", Estudi: " + codiEstudi;
    }
}