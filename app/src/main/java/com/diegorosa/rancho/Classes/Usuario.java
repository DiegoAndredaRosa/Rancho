package com.diegorosa.rancho.Classes;

import com.google.firebase.database.Exclude;

public class Usuario {
    private String keyUsuario;
    private String email;
    private String senha;

    public Usuario(){}

    public Usuario(String keyUsuario, String email, String senha) {
        this.email = email;
        this.senha = senha;
        this.keyUsuario = keyUsuario;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getKeyUsuario() {
        return keyUsuario;
    }

    public void setKeyUsuario(String keyUsuario) {
        this.keyUsuario = keyUsuario;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }
    @Exclude
    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String toString() {
        return "Usuario{" +
            ", email='" + email + '\'' +
            '}';
    }
}