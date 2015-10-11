import org.mockit.annotation.MockIt

@MockIt(mockKeys = Array("test2"))
class Factor3ClientMock extends FactorClientMock {

    override def factor: Int = 3

}
