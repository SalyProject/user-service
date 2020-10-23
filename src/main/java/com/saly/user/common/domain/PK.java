package com.saly.user.common.domain;

import lombok.Getter;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        final Object pkID = (o instanceof PK) ? ((PK) o).getId() : null;
        return o != null && Objects.equals(id, pkID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}