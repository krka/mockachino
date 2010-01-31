package se.mockachino.cleaner;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

public class StacktraceCleaner {
	public static List<StackTraceElement> getCleanStacktrace(Throwable e) {
		StackTraceElement[] stackTraceElements = e.getStackTrace();
		List<StackTraceElement> list = new ArrayList<StackTraceElement>();
		for (StackTraceElement stackTraceElement : stackTraceElements) {
			if (isClean(stackTraceElement)) {
				list.add(stackTraceElement);
			}
		}
		return list;

	}

	public static StackTraceElement[] convert(List<StackTraceElement> list) {
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
	}};
	private static boolean isClean(StackTraceElement stackTraceElement) {
		String s = stackTraceElement.getClassName();
		for (String bannedName : bannedNames) {
			if (s.startsWith(bannedName)) {
				return false;
			}
		}
		return true;
	}

}
