package org.eneas.waf.dao;

import java.util.List;

import org.eneas.waf.bean.Clase;



public interface IProviderClases {

	List<Clase> getClases();

	boolean validate(String category);

	Clase getClase(String category);

}
