package org.eneas.waf.intercetor;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.eneas.waf.errores.ExceptionSecurity;
import org.eneas.waf.filter.IValidateRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class SecurityInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = Logger.getLogger(SecurityInterceptor.class);

	private IValidateRequest validate;

	// before the actual handler will be executed
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		Enumeration<?> str = request.getParameterNames();

		while (str.hasMoreElements()) {
			String value = request.getParameter((String) str.nextElement());
			try {
				validate.validateRequest(value);

			} catch (ExceptionSecurity ie) {
				logger.error(ie.getMessage());
				response.setStatus(503);
				return false;
			
			}
		}
		return true;
	}

	// after the handler is executed
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	
	}

	public IValidateRequest getValidate() {
		return validate;
	}

	public void setValidate(IValidateRequest validate) {
		this.validate = validate;
	}
}
