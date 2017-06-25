package org.eneas.waf.dao;

import java.util.ArrayList;
import java.util.List;

import org.eneas.waf.bean.Clase;

public class ProviderClaseMemory implements IProviderClases {

	private List<Clase> clases = new ArrayList<Clase>();

	@Override
	public List<Clase> getClases() {
		// TODO Auto-generated method stub

		List<String> reglas = new ArrayList<String>();
		Clase clase = new Clase();
		clase.setName("XSSjs");
		reglas.add("/><script>alert(");
		reglas.add("/><script>");
		reglas.add("/><script>alert(");
		reglas.add("/><script>alert(document.cookie)<script>&param=value");
		reglas.add(";</script>"); 
		reglas.add("/><script>document.href=");
		reglas.add("/><script>document");
		reglas.add("/><script>for(;;)</script>");
		reglas.add("<meta%20http-equiv=refresh%20content=0;>");
		reglas.add("/><img><script>");
		reglas.add("/><script>while</script>");
		reglas.add("document.getElementyById");
		reglas.add("/><script>window.href=");
			

		clase.setAlleglas(reglas);
		clases.add(clase);

		Clase clase2= new Clase();
		List<String> reglas2 = new ArrayList<String>();
		clase2.setName("InjectionSQL");
		reglas2.add("--INSERT INTO");
		reglas2.add("--OR 1=1--");
		reglas2.add("--AND 1=1");
		reglas2.add("--UPDATE FROM");
		reglas2.add("--1=1");
		reglas2.add("DELETE FROM");
		reglas2.add("--");
		clase2.setAlleglas(reglas2);
		clases.add(clase2);

		return clases;

	}

	@Override
	public boolean validate(String category) {
		// TODO Auto-generated method stub
		return category == "JSATACK" || category == "SQLATACK";
	}

	@Override
	public Clase getClase(String category) {
		// TODO Auto-generated method stub
		for (Clase clase : this.clases) {
			if (clase.getName().equals(category)){
				return clase;
			}
		}
		return null;
	}

}
