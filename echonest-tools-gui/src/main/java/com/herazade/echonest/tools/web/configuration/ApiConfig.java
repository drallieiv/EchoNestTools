package com.herazade.echonest.tools.web.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.echonest.api.v4.EchoNestAPI;
import com.echonest.api.v4.EchoNestException;

@Configuration
public class ApiConfig {

	// Logger
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${ECHO_NEST_API_KEY}")
	private String apiKey;

	@Bean
	public EchoNestAPI getEchoNestAPI() throws EchoNestException {
		if (apiKey == null) {
			logger.warn("API Key not defined, no analysis can be run");
			return null;
		}

		return new EchoNestAPI(apiKey);
	}

	/**
	 * @return the apiKey
	 */
	public String getApiKey() {
		return apiKey;
	}

}
