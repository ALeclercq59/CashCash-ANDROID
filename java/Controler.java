package com.example.myfirstapp;

public class Controler {

    private String n_serie;
    private String id_intervention;
    private String temps;
    private String commentaire;

    Controler(){}

    Controler (String n_serie, String id_intervention, String temps, String commentaire)
    {
        this.n_serie = n_serie;
        this.id_intervention = id_intervention;
        this.temps = temps;
        this.commentaire = commentaire;
    }

    public void setN_serie(String n_serie) {
        this.n_serie = n_serie;
    }

    public void setId_intervention(String id_intervention) {
        this.id_intervention = id_intervention;
    }

    public void setTemps(String temps) {
        this.temps = temps;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public String getN_serie() {
        return n_serie;
    }

    public String getId_intervention() {
        return id_intervention;
    }

    public String getTemps() {
        return temps;
    }

    public String getCommentaire() {
        return commentaire;
    }

}
