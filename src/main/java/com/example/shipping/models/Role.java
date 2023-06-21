package com.example.shipping.models;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer roleId;

	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private ERole roleName;

	public Role() {

	}

	@Override
	public String toString() {
		return "Role{" +
				"roleId=" + roleId +
				", roleName=" + roleName +
				'}';
	}

	public Role(ERole name) {
		this.roleName = name;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer id) {
		this.roleId = id;
	}

	public ERole getRoleName() {
		return roleName;
	}

	public void setRoleName(ERole name) {
		this.roleName = name;
	}
}
