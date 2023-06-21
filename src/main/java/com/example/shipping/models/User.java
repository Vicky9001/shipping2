package com.example.shipping.models;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(	name = "users",
		uniqueConstraints = {
			@UniqueConstraint(columnNames = "username"),
			@UniqueConstraint(columnNames = "phone")
		})
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	@Size(max = 20)
	private String username;

	@NotBlank
	@Size(max = 20)
	private String realName;

	@NotBlank
	@Size(max = 50)
	private String phone;

	private int sex;//0-男 1-女

	private LocalDate entryTime;

	private LocalDate birthday;

	private int age;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(	name = "user_role",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	@NotBlank
	@Size(max = 120)
	private String password;

	public User(String username, String realName, String phone, int sex, LocalDate entryTime, LocalDate birthday, String password) {
		this.username = username;
		this.realName = realName;
		this.phone = phone;
		this.sex = sex;
		this.entryTime = entryTime;
		this.birthday = birthday;
		this.password = password;
		int age = birthday.until(LocalDate.now()).getYears();
		this.age = age;
	}

	public User(String username, String realName, String phone, String password) {
		this.username = username;
		this.realName = realName;
		this.phone = phone;
		this.password = password;
	}

	public User() {

	}

	public User(String username, String phone, String encode) {
		this.username = username;
		this.phone = phone;
		this.password = encode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public LocalDate getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(LocalDate entryTime) {
		this.entryTime = entryTime;
	}

	public LocalDate getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", username='" + username + '\'' +
				", realName='" + realName + '\'' +
				", phone='" + phone + '\'' +
				", sex=" + sex +
				", entryTime=" + entryTime +
				", birthday=" + birthday +
				", age=" + age +
				", roles=" + roles +
				", password='" + password + '\'' +
				'}';
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
