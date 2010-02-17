package se.mockachino;

import se.mockachino.exceptions.UsageError;
import se.mockachino.spy.SpyHandler;

public class MockSettings {
	private boolean quick;
	private CallHandler fallback = MockContext.DEFAULT_VALUES;
	private String name;

	public MockSettings spyOn(Object impl) {
		if (impl == null) {
			throw new UsageError("impl can not be null");
		}
		fallback = new SpyHandler(impl);
		return this;
	}

	public MockSettings fallback(CallHandler fallback) {
		if (fallback == null) {
			throw new UsageError("fallback can not be null");
		}
		this.fallback = fallback;
		return this;
	}

	public MockSettings name(String name) {
		if (name == null) {
			throw new UsageError("name can not be null");
		}
		this.name = name;
		return this;
	}

	public MockSettings quick() {
		quick = true;
		return this;
	}

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


	public static MockSettings newSettings() {
		return new MockSettings();
	}
}
