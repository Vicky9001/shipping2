package com.example.shipping.models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transfers",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = "id")
		})
public class Transfer {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private LocalDateTime transferTime;
	private String loca;
	@ManyToOne
	@JoinColumn(name = "orderId")
	private Order order;
	public Transfer(Order order, String loca, LocalDateTime time) {
		this.order = order;
		this.loca = loca;
		this.transferTime = time;
	}

	public Transfer() {

	}

	@Override
	public String toString() {
		return "Transfer{" +
				"id=" + id +
				", transferTime=" + transferTime +
				", loca='" + loca + '\'' +
				", order=" + order +
				'}';
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getTransferTime() {
		return transferTime;
	}

	public void setTransferTime(LocalDateTime transferTime) {
		this.transferTime = transferTime;
	}

	public String getLoca() {
		return loca;
	}

	public void setLoca(String loca) {
		this.loca = loca;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
}
