package com.herazade.echonest.tools.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan
@Import(value = EchoNestAPIConfig.class)
public class EntCoreConfiguration {

}
