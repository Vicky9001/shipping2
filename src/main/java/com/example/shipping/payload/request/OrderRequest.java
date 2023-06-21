package com.example.shipping.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

public class OrderRequest {

	//用户
	private Long shipperId;

	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	//驾驶员
	private Set<Long> carrierIds;

	//车辆
	private Set<Long> vanIds;

	//起点
	private String start;

	//目的地
	private String destination;

	//收货人
	private String receiver;

	//重量
	private double weight;

	//价格
	private double money;

	//订单状态
	private int state;

	//到达地点
	private String loca;

	private String info;

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getLoca() {
		return loca;
	}

	public void setLoca(String loca) {
		this.loca = loca;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public Long getShipperId() {
		return shipperId;
	}

	public void setShipperId(Long shipperId) {
		this.shipperId = shipperId;
	}

	public Set<Long> getCarrierIds() {
		return carrierIds;
	}

	public void setCarrierIds(Set<Long> carrierIds) {
		this.carrierIds = carrierIds;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public Set<Long> getVanIds() {
		return vanIds;
	}

	public void setVanIds(Set<Long> vanIds) {
		this.vanIds = vanIds;
	}
}
