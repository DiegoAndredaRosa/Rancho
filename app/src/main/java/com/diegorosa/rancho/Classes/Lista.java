package com.diegorosa.rancho.Classes;

public class Lista {
    private String keyLista;
    private String keyListaUsuario;
    private String descricaoLista;
    private String quantidadeLista;
    private String valorLista;

    public Lista(){}

    public String getKeyListaUsuario() {
        return keyListaUsuario;
    }

    public void setKeyListaUsuario(String keyListaUsuario) {
        this.keyListaUsuario = keyListaUsuario;
    }


    public String getKeyLista() {
        return keyLista;
    }

    public void setKeyLista(String keyLista) {
        this.keyLista = keyLista;
    }


    public String getDescricaoLista() {
        return descricaoLista;
    }

    public void setDescricaoLista(String descricaoLista) {
        this.descricaoLista = descricaoLista;
    }

    public String getQuantidadeLista() {
        return quantidadeLista;
    }

    public void setQuantidadeLista(String quantidadeLista) {
        this.quantidadeLista = quantidadeLista;
    }

    public String getValorLista() {
        return valorLista;
    }

    public void setValorLista(String valorLista) {
        this.valorLista = valorLista;
    }

    public String toString() {
        return "Lista{" +
                ", descrição lista='" + descricaoLista + '\'' +
                '}';
    }
}
