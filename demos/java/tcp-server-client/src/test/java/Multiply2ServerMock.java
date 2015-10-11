import org.mockit.annotation.MockIt;

@MockIt(mockKeys = {"test"})
public class Multiply2ServerMock extends MultiplyServerMock {

    @Override
    public int multiplyFactor() {
        return 2;
    }

}
