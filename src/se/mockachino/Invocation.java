package se.mockachino;

import se.mockachino.util.Formatting;

import java.util.Comparator;

public class Invocation {
    public static final Invocation NULL = new Invocation(null, null, 0, null);

    public static final Comparator<Invocation> COMPARATOR = new Comparator<Invocation>() {
        @Override
        public int compare(Invocation a, Invocation b) {
            return a.callNumber - b.callNumber;
        }
    };

    private final Object obj;
    private final MethodCall methodCall;
    private final int callNumber;
    private final StackTraceElement[] stacktrace;

    public Invocation(Object obj, MethodCall methodCall, int callNumber, StackTraceElement[] stacktrace) {
        this.obj = obj;
        this.methodCall = methodCall;
        this.callNumber = callNumber;
        this.stacktrace = stacktrace;
    }

    public int getCallNumber() {
        return callNumber;
    }

    public String getStackTraceString() {
        return Formatting.toString(stacktrace);
    }

    public String getStackTraceString(int maxLines) {
        return Formatting.toString(stacktrace, maxLines);
    }

    public MethodCall getMethodCall() {
        return methodCall;
    }

    public StackTraceElement[] getStacktrace() {
        return stacktrace;
    }

    public Object getObject() {
        return obj;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Invocation that = (Invocation) o;

        if (callNumber != that.callNumber) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return callNumber;
    }

    public String toString() {
        if (obj == null) {
            return "<no invocation>";
        }
        return obj.toString() + "." + methodCall.toString();  
    }


}
