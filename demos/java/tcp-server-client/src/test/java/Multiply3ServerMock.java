import com.github.pheymann.mockit.annotation.MockIt;

@MockIt(mockKeys = {"test"})
public class Multiply3ServerMock extends MultiplyServerMock {

    @Override
    public int multiplyFactor() {
        return 3;
    }

}
