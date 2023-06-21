package com.example.shipping.models;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "vans",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = "id")
		})
public class Van {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(name = "maxLoad")
	private double maxLoad;
	@Column(name = "license")
	private String license;
	@Column(name = "state") //0-空闲 1-使用 2-损坏
	private int state;
	@Column(name = "info")
	private String info;

	public Van(Long id, double maxLoad, String license, int state, String info) {
		this.id = id;
		this.maxLoad = maxLoad;
		this.license = license;
		this.state = state;
		this.info = info;
	}

	public Van(double maxLoad, String license, int state, String info) {
		this.maxLoad = maxLoad;
		this.license = license;
		this.state = state;
		this.info = info;
	}

	public Van() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getMaxLoad() {
		return maxLoad;
	}

	public void setMaxLoad(double maxLoad) {
		this.maxLoad = maxLoad;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	@Override
	public String toString() {
		return "Van{" +
				"id=" + id +
				", maxLoad=" + maxLoad +
				", license='" + license + '\'' +
				", state=" + state +
				", info='" + info + '\'' +
				'}';
	}
}
