package com.example.muril.testeandroid.amazonaws.models.nosql;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

@DynamoDBTable(tableName = "appmarket-mobilehub-34375625-Produto")

public class ProdutoDO {
    private String _id;
    private String _description;
    private Double _gtin;
    private String _name;

    public ProdutoDO(){}

    public ProdutoDO(String id, String nome, String descricao, Double gtin){
        this._id = id;
        this._name = nome;
        this._description = descricao;
        this._gtin = gtin;
    }

    @DynamoDBHashKey(attributeName = "id")
    @DynamoDBAttribute(attributeName = "id")
    public String getId() {
        return _id;
    }

    public void setId(final String _id) {
        this._id = _id;
    }
    @DynamoDBAttribute(attributeName = "description")
    public String getDescription() {
        return _description;
    }

    public void setDescription(final String _description) {
        this._description = _description;
    }
    @DynamoDBAttribute(attributeName = "gtin")
    public Double getGtin() {
        return _gtin;
    }

    public void setGtin(final Double _gtin) {
        this._gtin = _gtin;
    }
    @DynamoDBAttribute(attributeName = "name")
    public String getName() {
        return _name;
    }

    public void setName(final String _name) {
        this._name = _name;
    }

    public String toString(){
        return "Nome: " + _name + " Descrição: " + _description + " Gtin: " + _gtin;
    }

}
