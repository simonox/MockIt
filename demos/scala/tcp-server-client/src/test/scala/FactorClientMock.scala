import java.io.{ObjectOutputStream, ByteArrayOutputStream, ObjectInputStream, InputStream}

import com.github.pheymann.mockit.core.{NoFault, FaultLevel}
import com.github.pheymann.mockit.mock.tcp.TCPMockUnit

abstract class FactorClientMock extends TCPMockUnit {

    val maxCalculations     = 2

        var isResponse          = false
    var calculationCount    = 0
    var receivedData        = 0

    def factor: Int

    override def init: Unit = {}

    override def isNext: Boolean = {
        calculationCount < maxCalculations
    }

    override def waitOnData: Boolean = isResponse

    override def receiveData(inStream: InputStream): Unit = {
        val objIn = new ObjectInputStream(inStream)

        receivedData = objIn.readInt
    }

    override def mock: (Array[Byte], FaultLevel) = {
        val bOut = new ByteArrayOutputStream
        val objOut = new ObjectOutputStream(bOut)

        objOut.writeInt(factor)
        objOut.flush

        calculationCount += 1
        isResponse = true

        (bOut.toByteArray, NoFault())
    }

}