package org.eneas.waf.bean;

import java.util.Collection;

public class Clase {

	private String name;
	private Collection<String>  Alleglas;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Collection<String> getAlleglas() {
		return Alleglas;
	}
	
	
	public void setAlleglas(Collection<String> alleglas) {
		Alleglas = alleglas;
	}
	
	
	

}
