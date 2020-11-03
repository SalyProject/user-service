package com.saly.user.common.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Objects;

@EqualsAndHashCode
public class PK<ID, Entity extends AbstractEntity<Entity>> {

    @Getter
    private ID id;

    protected PK() {
    }

    public PK(ID id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }

}