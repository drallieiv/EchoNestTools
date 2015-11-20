package com.herazade.echonest.tools.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.echonest.api.v4.EchoNestAPI;
import com.echonest.api.v4.EchoNestException;

@Configuration
public class EchoNestAPIConfig {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Bean
	public EchoNestAPI getEchoNestAPI() throws EchoNestException {
		try {
			return new EchoNestAPI();
		} catch (EchoNestException e) {
			if (e.getCode() == EchoNestException.ERR_NO_KEY) {
				logger.error("Missing ECHO_NEST_API_KEY System or Environement Property. See README.md");
			}
			throw e;
		}
	}
}
