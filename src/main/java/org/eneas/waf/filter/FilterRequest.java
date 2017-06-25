package org.eneas.waf.filter;

import java.util.ArrayList;
import java.util.List;
import org.eneas.waf.bean.Clase;
import org.eneas.waf.dao.IProviderClases;
import org.eneas.waf.errores.ExceptionSecurity;

import de.daslaboratorium.machinelearning.classifier.Classification;
import de.daslaboratorium.machinelearning.classifier.Classifier;
import de.daslaboratorium.machinelearning.classifier.bayes.BayesClassifier;

public class FilterRequest implements IValidateRequest {
	private Classifier<String, String> learning;
	private IProviderClases providerClase;
	private float paranoiaLevel;

	private boolean mutex = false;
	private static Object Mut = new Object();

	public float getParanoiaLevel() {
		return paranoiaLevel;
	}

	public void setParanoiaLevel(float paranoiaLevel) {
		this.paranoiaLevel = paranoiaLevel;
	}

	public FilterRequest() {
		learning = new BayesClassifier<String, String>();
	}

	public IProviderClases getProviderClase() {
		return providerClase;
	}

	public void setProviderClase(IProviderClases providerClase) {
		this.providerClase = providerClase;
		registerLearning();
	}

	private void registerLearning() {

		synchronized (Mut) {
			if (!this.mutex) {
				this.learning.setMemoryCapacity(500);
				List<Clase> clases = providerClase.getClases();

				for (Clase clase : clases) {
					List<String> areglo = new ArrayList<String>();
					for (String param : clase.getAlleglas()) {
						for (String s : param.split("\\s")) {
							for (String aux : s.split("\\;"))
								for (String p : aux.split("\\)"))
									for (String h : p.split("\\("))
										areglo.add(h);
						}
					}
					this.learning.learn(clase.getName(), areglo);

				}
				mutex = true;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eneas.waf.filter.IValidateRequest#validateRequest(java.lang.String)
	 */
	@Override
	public void validateRequest(String... args) throws ExceptionSecurity {
		Classification<String, String> clasification = null;

		for (String element : args) {
			List<String> argument = cleanReglas(element);
			clasification = learning.classify(argument);
			if (clasification != null) {
				// Ya sabemos que tipo de ataque puede ser
				Clase clase = providerClase.getClase(clasification.getCategory());
				if (getProbabilidad(clase, element) > 40)
					throw new ExceptionSecurity("El argumento se detecto como un posible ataque: " + element + " del tipo: " + clasification.getCategory() );
			}
		}

	}

	private List<String> cleanReglas(String... args) {
		List<String> argument = new ArrayList<String>();
		for (String element : args) {
			for (String s : element.split("\\s")) {
				for (String aux : s.split("\\;"))
					for (String p : aux.split("\\)"))
						for (String h : p.split("\\("))
							argument.add(h);
			}
		}
		return argument;
	}

	private Float getProbabilidad(Clase clase, String argument) {
		List<String> salida = cleanReglas(argument);
		List<String> token = cleanReglas(clase.getAlleglas().toArray(new String[0]));

		Integer total = salida.size();
		Integer d = 0;

		for (String aux : salida) {
			if (token.contains(aux))
				d++;

		}
		

		return (d.floatValue() / total)  *  100;
	}

}
