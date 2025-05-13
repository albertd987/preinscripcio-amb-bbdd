package classes.model;


public class Estudi {
    private String codiEstudi;
    private String nomEstudi;
    
    public Estudi(String codiEstudi, String nomEstudi) {
        this.codiEstudi = codiEstudi;
        this.nomEstudi = nomEstudi;
    }
    
    public String getCodiEstudi() {
        return codiEstudi;
    }
    
    public void setCodiEstudi(String codiEstudi) {
        this.codiEstudi = codiEstudi;
    }
    
    public String getNomEstudi() {
        return nomEstudi;
    }
    
    public void setNomEstudi(String nomEstudi) {
        this.nomEstudi = nomEstudi;
    }
    
    @Override
    public String toString() {
        return codiEstudi + " - " + nomEstudi;
    }
}