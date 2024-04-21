package com.handle.api.repositories.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "secuence_names")
public class Secuence_names {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id_secuence;
	private int secuence;
	private int status;
	private String created;
	private String modified;
	
	public int getId_secuence() {
		return id_secuence;
	}
	public void setId_secuence(int id_secuence) {
		this.id_secuence = id_secuence;
	}
	public int getSecuence() {
		return secuence;
	}
	public void setSecuence(int secuence) {
		this.secuence = secuence;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getModified() {
		return modified;
	}
	public void setModified(String modified) {
		this.modified = modified;
	}
	
	
}
