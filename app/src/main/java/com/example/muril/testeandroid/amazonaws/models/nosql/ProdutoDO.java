package com.example.muril.testeandroid.amazonaws.models.nosql;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.List;

@DynamoDBTable(tableName = "appmarket-mobilehub-34375625-Produto")

public class ProdutoDO {
    private String _id;
    private String _description;
    private Double _gtin;
    private String _name;
    private String _type;
    private String _fornecedor;
    private List<Double> _localizacao;

    public ProdutoDO() {
    }

    public ProdutoDO(String id, String nome, String descricao, Double gtin, String type, String fornecedor) {
        this._id = id;
        this._name = nome;
        this._description = descricao;
        this._gtin = gtin;
        this._type = type;
        this._fornecedor = fornecedor;
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

    @DynamoDBIndexHashKey(attributeName = "gtin", globalSecondaryIndexName = "Gtin")
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

    @DynamoDBIndexHashKey(attributeName = "type", globalSecondaryIndexName = "Type")
    @DynamoDBAttribute(attributeName = "type")
    public String getType() {
        return _type;
    }

    public void setType(String type) {
        this._type = type;
    }

    @DynamoDBAttribute(attributeName = "fornecedor")
    public String getFornecedor() {
        return _fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this._fornecedor = fornecedor;
    }

    @DynamoDBAttribute(attributeName = "localizacao")
    public List<Double> getLocalizacao(){
        return _localizacao;
    }

    public void setLocalizacao(List<Double> localizacao){
        this._localizacao = localizacao;
    }

    public String toString() {
        return _name;
    }

}
