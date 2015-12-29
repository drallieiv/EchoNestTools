package com.herazade.echonest.tools.web.controller;

import javax.inject.Inject;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.echonest.api.v4.EchoNestException;
import com.herazade.echonest.tools.web.configuration.ApiConfig;
import com.herazade.echonest.tools.web.json.ApiConfigJson;

@RestController
@RequestMapping(value = "/api/config")
public class ConfigurationController {

	@Inject
	private ApiConfig apiConfig;

	@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiConfigJson index() throws EchoNestException {

		return new ApiConfigJson().setApiKey(apiConfig.getApiKey());
	}

}
