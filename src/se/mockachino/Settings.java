package se.mockachino;

public class Settings {
	public static MockSettings newSettings() {
		return new MockSettings();
	}

	public static MockSettings spyOn(Object impl) {
		return new MockSettings().spyOn(impl);
	}

	public static MockSettings fallback(CallHandler fallback) {
		return new MockSettings().fallback(fallback);
	}

	public static MockSettings quick() {
		return new MockSettings().quick();
	}

	public static MockSettings notQuick() {
		return new MockSettings().notQuick();
	}

	public static MockSettings name(String name) {
		return new MockSettings().name(name);
	}

}
