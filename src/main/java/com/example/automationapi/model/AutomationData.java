package com.example.automationapi.model;

import jakarta.persistence.*;

@Entity
@Table(name = "automation_data")
public class AutomationData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String url;
	private String username;
	private String password;
	private String server;
	private String tanent;
	
	private String user_pass_combined;

	// Getters & Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getTanent() {
		return tanent;
	}

	public void setTanent(String tanent) {
		this.tanent = tanent;
	}
	
	public String getUser_pass_combined() {
		return user_pass_combined;
	}

	public void setUser_pass_combined(String user_pass_combined) {
		this.user_pass_combined = user_pass_combined;
	}


}
