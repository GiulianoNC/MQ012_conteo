package com.quantum.parseo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cuerpo {

    //parametros
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("Numero_Conteo")
    @Expose
    private String nroConteo;
    @SerializedName("ITEM")
    @Expose
    private String item;
    @SerializedName("Numero_Serie")
    @Expose
    private String nroSerie;
    @SerializedName("Cantidad_Fisica")
    @Expose
    private String cantidadFisica;
    @SerializedName("Ubicación")
    @Expose
    private String ubicacion;

    //json body
    @SerializedName("Numero_Pallet")
    @Expose
    private String nroPallet;
    @SerializedName("Estado")
    @Expose
    private boolean estado;



    public String getCantidadFisica() {
        return cantidadFisica;
    }

    public void setCantidadFisica(String cantidadFisica) {
        this.cantidadFisica = cantidadFisica;
    }

    public Cuerpo(String item) {
        this.item = item;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNroConteo() {
        return nroConteo;
    }

    public void setNroConteo(String nroConteo) {
        this.nroConteo = nroConteo;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getNroSerie() {
        return nroSerie;
    }

    public void setNroSerie(String nroSerie) {
        this.nroSerie = nroSerie;
    }

    public String getNroPallet() {
        return nroPallet;
    }

    public void setNroPallet(String nroPallet) {
        this.nroPallet = nroPallet;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }


    public Cuerpo(String username, String password, String nroConteo, String item, String nroSerie) {
        this.username = username;
        this.password = password;
        this.nroConteo = nroConteo;
        this.item = item;
        this.nroSerie = nroSerie;
    }
    public Cuerpo(String username, String password, String nroConteo, String ubicacion ,String item, String nroSerie, String cantidadFisica) {
        this.username = username;
        this.password = password;
        this.nroConteo = nroConteo;
        this.ubicacion = ubicacion;
        this.item = item;
        this.nroSerie = nroSerie;
        this.cantidadFisica = cantidadFisica;
    }
}
