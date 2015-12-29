package com.herazade.echonest.tools.web.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.herazade.echonest.tools.core.project.EntProjectManager;
import com.herazade.echonest.tools.storage.StorageApi;
import com.herazade.echonest.tools.storage.StorageException;
import com.herazade.echonest.tools.storage.impl.FileSystemStorage;

@Configuration
public class StorageConfiguration {

	// Logger
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Value(value = "${project.folder:#{null}}")
	private String workDirectory;

	@Bean
	public EntProjectManager getEntProjectManager() {
		return new EntProjectManager();
	}

	@Bean
	public StorageApi getStorageApi() throws StorageException {

		if (workDirectory == null) {
			// Use current Folder
			workDirectory = System.getProperty("user.dir");
			logger.debug("Use current folder as work directory : {}", workDirectory);
		}

		FileSystemStorage storage = new FileSystemStorage(workDirectory);

		return storage;
	}

}
