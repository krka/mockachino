package se.mockachino.order;

import se.mockachino.MockData;
import se.mockachino.Mockachino;
import se.mockachino.proxy.ProxyUtil;

public class ExactOrderVerify {

    private final ExactOrderingContext orderingContext;
    private final int invocationIndex;
    private final int nrOfInvocations;

    public ExactOrderVerify(ExactOrderingContext orderingContext, int invocationIndex, int nrOfInvocations) {
        this.orderingContext = orderingContext;
        this.invocationIndex = invocationIndex;
        this.nrOfInvocations = nrOfInvocations;
    }

    public <T> T on(T mock) {
		MockData<T> data = Mockachino.getData(mock);
        if (!orderingContext.doesMockExistInContext(mock)) {
            throw new IllegalArgumentException(String.format("The mock %s is not part the ExactOrderingContext. Please add it to the constructor.", 
                    data.getInterface().getName()));
        }
		Class<T> clazz = data.getInterface();
		return (T) ProxyUtil.newProxy(clazz, new ExactOrderVerifyHandler(orderingContext, mock, invocationIndex, nrOfInvocations));
	}
}
