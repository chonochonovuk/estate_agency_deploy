package com.ecoverde.estateagency.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "propertyTypes")
public class PropertyType extends BaseEntity {
   private String typeName;

    public PropertyType() {
    }

    @Column(name = "typeName",nullable = false)
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
