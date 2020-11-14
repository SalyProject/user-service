package com.saly.user.service.user;

import com.saly.user.common.auth.UserRole;
import com.saly.user.common.domain.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "saly_user")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserEntity extends AbstractEntity<UserEntity> {

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @Column(name="role")
    @CollectionTable(name="roles", joinColumns=@JoinColumn(name="user_id"))
    private Set<UserRole> roles;
}
