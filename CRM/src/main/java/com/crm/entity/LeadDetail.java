package com.crm.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@DynamicUpdate
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LeadDetail implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String first_name;
	private String last_name;
	@Column(length = 10, unique = true)
	private Long mobile;
	@Column(unique = true)
	private String email;
	@Enumerated(EnumType.STRING)
	private Enum.LocationType location_type;
	@Enumerated(EnumType.STRING)
	private Enum.Status status;
	private String location_String;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public Long getMobile() {
		return mobile;
	}

	public void setMobile(Long mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Enum.LocationType getLocation_type() {
		return location_type;
	}

	public void setLocation_type(Enum.LocationType location_type) {
		this.location_type = location_type;
	}

	public Enum.Status getStatus() {
		return status;
	}

	public void setStatus(Enum.Status status) {
		this.status = status;
	}

	public String getLocation_String() {
		return location_String;
	}

	public void setLocation_String(String location_String) {
		this.location_String = location_String;
	}

}
