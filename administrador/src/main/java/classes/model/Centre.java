package classes.model;

public class Centre {

    private String codiCentre;
    private String nomCentre;
    
    public Centre(String codiCentre, String nomCentre) {
        this.codiCentre = codiCentre;
        this.nomCentre = nomCentre;
    }

    public String getCodiCentre() {
        return codiCentre;
    }
    
    public void setCodiCentre(String codiCentre) {
        this.codiCentre = codiCentre;
    }
    
    public String getNomCentre() {
        return nomCentre;
    }
    
    public void setNomCentre(String nomCentre) {
        this.nomCentre = nomCentre;
    }
    
    @Override
    public String toString() {
        return codiCentre + " - " + nomCentre;
    }
}