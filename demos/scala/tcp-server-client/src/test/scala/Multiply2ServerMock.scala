import com.github.pheymann.mockit.annotation.MockIt

@MockIt(mockKeys = Array("test"))
class Multiply2ServerMock extends MultiplyServerMock {

   override def multiplyFactor = 2

}
