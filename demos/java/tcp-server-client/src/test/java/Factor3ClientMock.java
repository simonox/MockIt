import org.mockit.annotation.MockIt;

@MockIt(mockKeys = {"test2"})
public class Factor3ClientMock extends FactorClientMock {

    @Override
    public int factor() {
        return 3;
    }

}
