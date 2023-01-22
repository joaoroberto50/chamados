package br.com.bugbuilder.chamado.models;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "TB_ROLE")
public class RoleModel implements GrantedAuthority, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID roleId;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, unique = true)
	private RoleName roleName;
	
	@Override
	public String getAuthority() {
		// TODO Auto-generated method stub
		return this.roleName.toString();
	}
	
	public UUID getRoleId() {
		return roleId;
	}
	
	public void setRoleId(UUID roleId) {
		this.roleId = roleId;
	}
	
	public RoleName getRoleName() {
        return roleName;
    }

    public void setRoleName(RoleName roleName) {
        this.roleName = roleName;
    }
}
