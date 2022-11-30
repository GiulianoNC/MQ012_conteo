package com.quantum.entidades;

public class Contactos {

    private int id;
    private String nombre;
    private String Item;
    private String Numero_Serie;
    private String resultado;

    private boolean isSelected = false;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getItem() {
        return Item;
    }

    public String setItem(String item) {
        Item = item;
        return item;
    }

    public int getId() {
        return id;
    }

    public int setId(int id) {
        this.id = id;
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumero_Serie() {
        return Numero_Serie;
    }

    public String setNumero_Serie(String numero_Serie) {
        Numero_Serie = numero_Serie;
        return numero_Serie;
    }

    public String getResultado() {
        return resultado;
    }

    public String setResultado(String resultado) {
        this.resultado = resultado;
        return resultado;
    }
}
