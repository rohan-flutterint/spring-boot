/*
 * Copyright 2012-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.actuate.autoconfigure.tracing.zipkin;

import zipkin2.reporter.Encoding;

import org.springframework.boot.actuate.autoconfigure.tracing.zipkin.ZipkinConfigurations.BraveConfiguration;
import org.springframework.boot.actuate.autoconfigure.tracing.zipkin.ZipkinConfigurations.OpenTelemetryConfiguration;
import org.springframework.boot.actuate.autoconfigure.tracing.zipkin.ZipkinConfigurations.SenderConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Zipkin.
 * <p>
 * It uses imports on {@link ZipkinConfigurations} to guarantee the correct configuration
 * ordering.
 *
 * @author Moritz Halbritter
 * @since 3.0.0
 */
@AutoConfiguration(after = RestTemplateAutoConfiguration.class)
@ConditionalOnClass(Encoding.class)
@Import({ SenderConfiguration.class, BraveConfiguration.class, OpenTelemetryConfiguration.class })
@EnableConfigurationProperties(ZipkinProperties.class)
public class ZipkinAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(ZipkinConnectionDetails.class)
	PropertiesZipkinConnectionDetails zipkinConnectionDetails(ZipkinProperties properties) {
		return new PropertiesZipkinConnectionDetails(properties);
	}

	@Bean
	@ConditionalOnMissingBean
	Encoding encoding(ZipkinProperties properties) {
		return switch (properties.getEncoding()) {
			case JSON -> Encoding.JSON;
			case PROTO3 -> Encoding.PROTO3;
		};
	}

}
