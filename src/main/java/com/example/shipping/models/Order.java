package com.example.shipping.models;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(	name = "orders",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = "id")
		})
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private LocalDate createDate;

	@ManyToOne
	@JoinColumn(name = "shipperId")
	private User shipper;
//	@ManyToOne
//	@JoinColumn(name = "carrierId")
//	private User carrier;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(	name = "order_carrier",
			joinColumns = @JoinColumn(name = "order_id"),
			inverseJoinColumns = @JoinColumn(name = "carrier_id"))
	private Set<User> carriers = new HashSet<>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(	name = "order_van",
			joinColumns = @JoinColumn(name = "order_id"),
			inverseJoinColumns = @JoinColumn(name = "van_id"))
	private Set<Van> vans = new HashSet<>();


	@OneToMany(fetch = FetchType.LAZY)
	@JoinTable(	name = "order_transfer",
			joinColumns = @JoinColumn(name = "order_id"),
			inverseJoinColumns = @JoinColumn(name = "transfer_id"))
	private Set<Transfer> transfers=new HashSet<>();

	private String destination;

	private int state;

	private String info;

	private double money;
	private String start;

	private double weight;

	private String receiver;

	public Order(User shipper, String start, String destination, String receiver, String info, double weight, int state, LocalDateTime createDate) {
		this.shipper = shipper;
		this.start= start;
		this.destination = destination;
		this.receiver = receiver;
		this.info = info;
		this.weight = weight;
		this.state = state;
		this.createDate = LocalDate.from(createDate);
	}

	public Order(User shipper, Set<User> carriers, String start, String destination, String receiver, String info, double weight, double money, int state, LocalDateTime createDate) {
		this.shipper = shipper;
		this.carriers = carriers;
		this.start= start;
		this.destination = destination;
		this.receiver = receiver;
		this.info = info;
		this.weight = weight;
		this.money = money;
		this.state = state;
		this.createDate = LocalDate.from(createDate);
	}

	@Override
	public String toString() {
		return "Order{" +
				"id=" + id +
				", createDate=" + createDate +
				", shipper=" + shipper +
				", carriers=" + carriers +
				", vans=" + vans +
				", transfers=" + transfers +
				", destination='" + destination + '\'' +
				", state=" + state +
				", info='" + info + '\'' +
				", money=" + money +
				", start='" + start + '\'' +
				", weight=" + weight +
				", receiver='" + receiver + '\'' +
				'}';
	}

//	@Override
//	public String toString() {
//		return "Order{" +
//				"id=" + id +
//				", createDate=" + createDate +
//				", shipper=" + shipper +
//				", carriers=" + carriers +
//				", vans=" + vans +
//				", destination='" + destination + '\'' +
//				", state=" + state +
//				", info='" + info + '\'' +
//				", money=" + money +
//				", start='" + start + '\'' +
//				", weight=" + weight +
//				", receiver='" + receiver + '\'' +
//				'}';
//	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Order() {

	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDate createDate) {
		this.createDate = createDate;
	}

	public User getShipper() {
		return shipper;
	}

	public void setShipper(User shipper) {
		this.shipper = shipper;
	}

	public Set<User> getCarriers() {
		return carriers;
	}

	public void setCarriers(Set<User> carriers) {
		this.carriers = carriers;
	}

	public Set<Van> getVans() {
		return vans;
	}

	public void setVans(Set<Van> vans) {
		this.vans = vans;
	}

	public Set<Transfer> getTransfers() {
		return transfers;
	}

	public void setTransfers(Set<Transfer> transfers) {
		this.transfers = transfers;
	}

	public int getState() {
		return state;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public void setState(int state) {
		this.state = state;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}
}
