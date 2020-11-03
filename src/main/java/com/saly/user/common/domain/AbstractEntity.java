package com.saly.user.common.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Function;

import static java.util.Optional.ofNullable;

@EqualsAndHashCode
@MappedSuperclass
public abstract class AbstractEntity<Entity extends AbstractEntity<Entity>> implements Serializable {

    @Id
    @Setter
    @Column(name = "id")
    @GeneratedValue
    private UUID id;

    @Getter
    @Setter
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Getter
    @Setter
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

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

