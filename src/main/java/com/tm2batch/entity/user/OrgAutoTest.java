package com.tm2batch.entity.user;


import com.tm2batch.entity.purchase.Product;
import java.io.Serializable;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;


@Entity
@Table( name = "orgautotest" )
@XmlRootElement
@NamedQueries({
})
public class OrgAutoTest implements Serializable
{
    @Transient
    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="orgautotestid")
    private int orgAutoTestId;

    @Column(name="name")
    private String name;

    @Column(name="orgid")
    private int orgId;

    @Column(name="suborgid")
    private int suborgId;

    @Column( name = "productid" )
    private int productId;

    @Transient
    private Product product;

    public int getOrgAutoTestId() {
        return orgAutoTestId;
    }

    public void setOrgAutoTestId(int orgAutoTestId) {
        this.orgAutoTestId = orgAutoTestId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public int getSuborgId() {
        return suborgId;
    }

    public void setSuborgId(int suborgId) {
        this.suborgId = suborgId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
    
    
    
}
