/*
 * Copyright 2015-2016 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */

package extensions;

import org.junit.gen5.api.Assertions;
import org.junit.gen5.api.extension.AfterEachExtensionPoint;
import org.junit.gen5.api.extension.ExceptionHandlerExtensionPoint;
import org.junit.gen5.api.extension.ExtensionContext.Namespace;
import org.junit.gen5.api.extension.ExtensionContext.Store;
import org.junit.gen5.api.extension.TestExtensionContext;
import org.opentest4j.AssertionFailedError;

public class ExpectToFailExtension implements ExceptionHandlerExtensionPoint, AfterEachExtensionPoint {

	@Override
	public void handleException(TestExtensionContext context, Throwable throwable) throws Throwable {
		if (throwable instanceof AssertionFailedError) {
			getExceptionStore(context).put("exception", throwable);
			return;
		}
		throw throwable;
	}

	private Store getExceptionStore(TestExtensionContext context) {
		return context.getStore(Namespace.of(context));
	}

	@Override
	public void afterEach(TestExtensionContext context) throws Exception {
		if (getExceptionStore(context).get("exception") == null)
			Assertions.fail("Test should have failed");
	}
}