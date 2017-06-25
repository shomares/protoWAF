package org.eneas.waf.filter;

import org.eneas.waf.errores.ExceptionSecurity;

public interface IValidateRequest {

	void validateRequest(String... args) throws ExceptionSecurity;

}