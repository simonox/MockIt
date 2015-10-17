import com.github.pheymann.mockit.annotation.MockIt

@MockIt(mockKeys = Array("test"))
class Multiply3ServerMock extends MultiplyServerMock {

    override val multiplyFactor = 3

}
