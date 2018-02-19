package com.example.muril.testeandroid2.amazonaws.models.nosql;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.List;
import java.util.Map;
import java.util.Set;

@DynamoDBTable(tableName = "appmarket-mobilehub-34375625-Produtos")

public class ProdutosDO {
    private Double _productId;
    private String _productDescription;
    private Double _productGtin;
    private String _productName;
    private Double _timestamp;

    @DynamoDBHashKey(attributeName = "product_id")
    @DynamoDBAttribute(attributeName = "product_id")
    public Double getProductId() {
        return _productId;
    }

    public void setProductId(final Double _productId) {
        this._productId = _productId;
    }
    @DynamoDBAttribute(attributeName = "product_description")
    public String getProductDescription() {
        return _productDescription;
    }

    public void setProductDescription(final String _productDescription) {
        this._productDescription = _productDescription;
    }
    @DynamoDBAttribute(attributeName = "product_gtin")
    public Double getProductGtin() {
        return _productGtin;
    }

    public void setProductGtin(final Double _productGtin) {
        this._productGtin = _productGtin;
    }
    @DynamoDBAttribute(attributeName = "product_name")
    public String getProductName() {
        return _productName;
    }

    public void setProductName(final String _productName) {
        this._productName = _productName;
    }
    @DynamoDBAttribute(attributeName = "timestamp")
    public Double getTimestamp() {
        return _timestamp;
    }

    public void setTimestamp(final Double _timestamp) {
        this._timestamp = _timestamp;
    }

}
