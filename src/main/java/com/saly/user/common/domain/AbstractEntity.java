package com.saly.user.common.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;
import java.util.function.Function;

import static java.util.Optional.ofNullable;

@EqualsAndHashCode
@MappedSuperclass
public abstract class AbstractEntity<Entity extends AbstractEntity<Entity>> implements Serializable {

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator",
            parameters = {@Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy")}
    )
    @Column(name = "id")
    @Setter
    private UUID id;

    @Transient
    private ID<Entity> pkid;

    protected AbstractEntity() {
    }

    protected UUID getUUID() {
        return id;
    }

    public final ID<Entity> getID() {
        if (pkid == null) {
            pkid = ofNullable(getUUID()).map((Function<UUID, ID<Entity>>) ID::new).orElse(null);
        }

        return pkid;
    }

    @Getter
    public static class ID<H extends AbstractEntity<H>> extends PK<UUID, H> {

        protected ID() {
        }

        public ID(UUID uuid) {
            super(uuid);
        }

        @Override
        public String toString() {
            return String.valueOf(getId().toString());
        }
    }
}

