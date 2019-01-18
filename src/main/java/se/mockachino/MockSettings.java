package se.mockachino;

import se.mockachino.exceptions.UsageError;
import se.mockachino.spy.SpyHandler;

import java.util.HashSet;
import java.util.Set;

public class MockSettings {
	private boolean quick;
	private CallHandler fallback = Mockachino.DEFAULT_VALUES;
	private String name;

	/**
	 * Sets the fallback handler to be a spy of impl
	 *
	 * @param impl
	 * @return the same mocksettings object
	 */
	public MockSettings spyOn(Object impl) {
		if (impl == null) {
			throw new UsageError("impl can not be null");
		}
		fallback = new SpyHandler(impl);
		return this;
	}

	/**
	 * Sets the fallback handler.
	 *
	 * @param fallback
	 * @return the same mocksettings object
	 */
	public MockSettings fallback(CallHandler fallback) {
		if (fallback == null) {
			throw new UsageError("fallback can not be null");
		}
		this.fallback = fallback;
		return this;
	}

	/**
	 * Sets the name of the mock
	 *
	 * @param name
	 * @return the same mocksettings object
	 */
	public MockSettings name(String name) {
		if (name == null) {
			throw new UsageError("name can not be null");
		}
		this.name = name;
		return this;
	}

	/**
	 * Sets the mock to be quick.
	 * <p></p>
	 * A quick mock simply does not record the stacktrace of calls made.
	 *
	 * @return the same mocksettings object
	 */
	public MockSettings quick() {
		quick = true;
		return this;
	}

	/**
	 * Sets the mock to not be quick.
	 * <p></p>
	 * A quick mock simply does not record the stacktrace of calls made.
	 *
	 * @return the same mocksettings object
	 */
	public MockSettings notQuick() {
		quick = false;
		return this;
	}

	public CallHandler getFallback() {
		return fallback;
	}

	public boolean isQuick() {
		return quick;
	}

	public String getName() {
		return name;
	}
}
