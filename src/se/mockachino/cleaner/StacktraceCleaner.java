package se.mockachino.cleaner;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

public class StacktraceCleaner {

	public static <T extends Throwable> T cleanError(T e) {
		e.setStackTrace(convert(getCleanStacktrace(e)));
		return e;
	}

	private static List<StackTraceElement> getCleanStacktrace(Throwable e) {
		StackTraceElement[] stackTraceElements = e.getStackTrace();
		List<StackTraceElement> list = new ArrayList<StackTraceElement>();
		for (StackTraceElement stackTraceElement : stackTraceElements) {
			if (isClean(stackTraceElement)) {
				list.add(stackTraceElement);
			}
		}
		return list;

	}

	private static StackTraceElement[] convert(List<StackTraceElement> list) {
		StackTraceElement[] ret = new StackTraceElement[list.size()];
		int i = 0;
		for (StackTraceElement stackTraceElement : list) {
			ret[i++] = stackTraceElement;
		}
		return ret;
	}

	private static final Collection<String> bannedNames = new ArrayList<String>(){{
		add("se.mockachino.");
		add("$Proxy");
		add("sun.reflect.");
		add("java.lang.reflect.");
		add("org.junit.");
		add("com.intellij.rt.execution.");
		add("com.intellij.junit4.");
		add("net.sf.cglib.");		
	}};
	private static boolean isClean(StackTraceElement stackTraceElement) {
		String s = stackTraceElement.getClassName();
		if (s.contains("Test")) {
			return true;
		}
		for (String bannedName : bannedNames) {
			if (s.startsWith(bannedName)) {
				return false;
			}
		}
		return true;
	}
}
