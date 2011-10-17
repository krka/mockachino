package se.mockachino.stub.returnvalue;

import se.mockachino.MethodCall;
import se.mockachino.VerifyableCallHandler;
import se.mockachino.util.MockachinoMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SequentialAnswers implements VerifyableCallHandler {
	private final List<VerifyableCallHandler> answers = new ArrayList<VerifyableCallHandler>();
	private final AtomicInteger position = new AtomicInteger();

    public SequentialAnswers(VerifyableCallHandler first, VerifyableCallHandler... additional) {
        this.answers.add(first);
        if (additional != null) {
            for (VerifyableCallHandler handler : additional) {
                this.answers.add(handler);
            }
        }
    }

	@Override
	public Object invoke(Object obj, MethodCall call) throws Throwable {
		int pos = Math.min(position.getAndIncrement(), answers.size() - 1);
		return answers.get(pos).invoke(obj, call);
	}

    @Override
    public void verify(MockachinoMethod method) {
        for (VerifyableCallHandler answer : answers) {
            answer.verify(method);
        }
    }

    public void add(VerifyableCallHandler answer) {
        this.answers.add(answer);
    }
}
