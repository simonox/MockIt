import com.github.pheymann.mockit.annotation.MockIt

@MockIt(mockKeys = Array("test2"))
class Factor2ClientMock extends FactorClientMock {

    override def factor: Int = 2

}
