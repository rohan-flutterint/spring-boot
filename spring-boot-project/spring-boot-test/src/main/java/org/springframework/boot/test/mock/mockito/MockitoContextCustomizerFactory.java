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

package org.springframework.boot.test.mock.mockito;

import java.util.List;

import org.springframework.test.context.ContextConfigurationAttributes;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.ContextCustomizerFactory;
import org.springframework.test.context.TestContextAnnotationUtils;

/**
 * A {@link ContextCustomizerFactory} to add Mockito support.
 *
 * @author Phillip Webb
 * @deprecated since 3.4.0 for removal in 4.0.0
 */
@SuppressWarnings("removal")
@Deprecated(since = "3.4.0", forRemoval = true)
class MockitoContextCustomizerFactory implements ContextCustomizerFactory {

	@Override
	public ContextCustomizer createContextCustomizer(Class<?> testClass,
			List<ContextConfigurationAttributes> configAttributes) {
		// We gather the explicit mock definitions here since they form part of the
		// MergedContextConfiguration key. Different mocks need to have a different key.
		DefinitionsParser parser = new DefinitionsParser();
		parseDefinitions(testClass, parser);
		return new MockitoContextCustomizer(parser.getDefinitions());
	}

	private void parseDefinitions(Class<?> testClass, DefinitionsParser parser) {
		parser.parse(testClass);
		if (TestContextAnnotationUtils.searchEnclosingClass(testClass)) {
			parseDefinitions(testClass.getEnclosingClass(), parser);
		}
	}

}
