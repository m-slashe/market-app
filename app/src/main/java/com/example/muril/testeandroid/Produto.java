package com.example.muril.testeandroid;

public class Produto {
    String nome;
    Float preco;

    public Produto(String nome, Float preco){
        this.nome = nome;
        this.preco = preco;
    }

    public String toString(){
        return "Nome: " + this.nome + " Preco: " + this.preco;
    }
}
