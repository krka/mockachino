package se.mockachino;

import se.mockachino.exceptions.UsageError;
import se.mockachino.spy.SpyHandler;

import java.util.HashSet;
import java.util.Set;

public class MockSettings {
	private boolean quick;
	private CallHandler fallback = MockContext.DEFAULT_VALUES;
	private String name;
	private Set<Class<?>> interfaces = new HashSet<Class<?>>();

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

	public MockSettings add(Class<?>... interfaces) {
		for (Class<?> anInterface : interfaces) {
			this.interfaces.add(anInterface);
		}
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

	public Set<Class<?>> getExtraInterfaces() {
		return interfaces;
	}
}
