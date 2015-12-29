package com.herazade.echonest.tools.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.oxm.support.AbstractMarshaller;
import org.springframework.oxm.xstream.XStreamMarshaller;

import com.echonest.api.v4.TimedEvent;
import com.herazade.echonest.tools.core.project.EntProject;
import com.herazade.echonest.tools.core.project.EntProjectManager;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Configuration
public class EntCoreConfiguration {

	@Autowired
	private ResourceLoader resourceLoader;

	@Bean
	public EntProjectManager getEntProjectManager() {
		return new EntProjectManager();
	}

	@Bean
	public AbstractMarshaller getMarshaller() throws ClassNotFoundException {
		XStreamMarshaller marshaller = new XStreamMarshaller();
		marshaller.setSupportedClasses(EntProject.class);
		Map<String, Object> aliases = new HashMap<String, Object>();
		aliases.put("TimedEvent", TimedEvent.class);
		aliases.put("project", EntProject.class);

		// Add alias from all Strategies
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(XStreamAlias.class));
		Set<BeanDefinition> strategies = scanner.findCandidateComponents("");

		for (BeanDefinition strategyDefinition : strategies) {
			Class<?> strategyClass = Class.forName(strategyDefinition.getBeanClassName());
			String alias = strategyClass.getAnnotation(XStreamAlias.class).value();
			aliases.put(alias, strategyClass);
		}

		marshaller.setAliases(aliases);
		return marshaller;
	}
}
