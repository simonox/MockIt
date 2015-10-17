import java.io.{ByteArrayOutputStream, InputStream, ObjectInputStream, ObjectOutputStream}

import com.github.pheymann.mockit.core.{FaultLevel, NoFault}
import com.github.pheymann.mockit.mock.tcp.TCPMockUnit

abstract class MultiplyServerMock extends TCPMockUnit {

    val maxCalculations     = 2

    var calculationCount    = 0
    var receivedData        = 0

    def multiplyFactor: Int

    override def init: Unit = {}

    override def isNext: Boolean = {
        calculationCount < maxCalculations
    }

    override def waitOnData: Boolean = true

    override def receiveData(inStream: InputStream): Unit = {
        val objIn = new ObjectInputStream(inStream)

        receivedData = objIn.readInt
    }

    override def mock: (Array[Byte], FaultLevel) = {
        val bOut = new ByteArrayOutputStream
        val objOut = new ObjectOutputStream(bOut)

        objOut.writeInt(receivedData * multiplyFactor)
        objOut.flush

        calculationCount += 1
        (bOut.toByteArray, NoFault())
    }

}