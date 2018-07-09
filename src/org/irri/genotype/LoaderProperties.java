package org.irri.genotype;

import java.io.Serializable;

public class LoaderProperties implements Serializable{

	private String username;
	private String databasename;
	private String port;
	private String hostname;
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDatabasename() {
		return databasename;
	}

	public void setDatabasename(String databasename) {
		this.databasename = databasename;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
