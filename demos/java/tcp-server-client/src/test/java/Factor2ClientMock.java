import com.github.pheymann.mockit.annotation.MockIt;

@MockIt(mockKeys = {"test2"})
public class Factor2ClientMock extends FactorClientMock {

    @Override
    public int factor() {
        return 2;
    }

}
