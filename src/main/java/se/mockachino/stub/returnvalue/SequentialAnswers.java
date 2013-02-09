package se.mockachino.stub.returnvalue;

import se.mockachino.MethodCall;
import se.mockachino.VerifyableCallHandler;
import se.mockachino.util.MockachinoMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SequentialAnswers<T> implements VerifyableCallHandler<T> {
	private final List<VerifyableCallHandler<T>> answers = new ArrayList<VerifyableCallHandler<T>>();
	private final AtomicInteger position = new AtomicInteger();

    public SequentialAnswers(VerifyableCallHandler<T> first) {
        this.answers.add(first);
    }

    public SequentialAnswers(VerifyableCallHandler<T> first, VerifyableCallHandler<T>... additional) {
        this.answers.add(first);
        if (additional != null) {
            for (VerifyableCallHandler<T> handler : additional) {
                this.answers.add(handler);
            }
        }
    }

	@Override
	public T invoke(Object obj, MethodCall call) throws Throwable {
		int pos = Math.min(position.getAndIncrement(), answers.size() - 1);
		return answers.get(pos).invoke(obj, call);
	}

    @Override
    public void verify(MockachinoMethod method) {
        for (VerifyableCallHandler<T> answer : answers) {
            answer.verify(method);
        }
    }

    public void add(VerifyableCallHandler<T> answer) {
        this.answers.add(answer);
    }
}
