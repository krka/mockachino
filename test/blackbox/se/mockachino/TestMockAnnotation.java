package se.mockachino;

import org.junit.Before;
import org.junit.Test;
import se.mockachino.annotations.Mock;

import java.util.List;

import static org.junit.Assert.assertNotNull;

public class TestMockAnnotation {

    @Mock
    public List mock1;

    @Mock
    protected List mock2;

    @Mock
    List mock3;

    @Mock
    private List mock4;

    @Before
    public void before() {
        Mockachino.setupMocks(this);
    }

    @Test
    public void simpleTest1() {
        verify(mock1);
        verify(mock2);
        verify(mock3);
        verify(mock4);

        mock1.size();
        Mockachino.verifyExactly(1).on(mock1).size();
    }

    @Test
    public void simpleTest2() {
        // Verify that mocks are reset between tests
        mock1.size();
        Mockachino.verifyExactly(1).on(mock1).size();
    }



    private void verify(List mock) {
        assertNotNull(mock);
        assertNotNull(Mockachino.getData(mock));
    }
}
