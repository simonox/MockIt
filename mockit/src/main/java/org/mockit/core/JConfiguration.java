package org.mockit.core;

import org.mockit.General;

/**
 * Java wrapper for {@link org.mockit.core.Configuration}. For a detailed
 * description of the parameter use the documentation of the scala implementation.
 *
 * @author  pheymann
 * @version 0.1.0
 */
public class JConfiguration {

    private int             threadNumber    = General.DEF_THREAD_NUM;
    private int             repetitions     = General.DEF_REPETITIONS;

    private int             serverPort      = General.DEF_PORT;

    private int             targetPort      = General.DEF_PORT;
    private String          targetIp        = General.DEF_IP;

    private int             mockNumber      = 1;
    private General.JBasicMockType mockType        = General.JBasicMockType.none;
    private General.JConnectionType mockConnection  = General.JConnectionType.none;

    public int getThreadNumber() {
        return threadNumber;
    }

    public void setThreadNumber(int threadNumber) {
        this.threadNumber = threadNumber;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public int getTargetPort() {
        return targetPort;
    }

    public void setTargetPort(int targetPort) {
        this.targetPort = targetPort;
    }

    public String getTargetIp() {
        return targetIp;
    }

    public void setTargetIp(String targetIp) {
        this.targetIp = targetIp;
    }

    public int getMockNumber() {
        return mockNumber;
    }

    public void setMockNumber(int mockNumber) {
        this.mockNumber = mockNumber;
    }

    public General.JBasicMockType getMockType() {
        return mockType;
    }

    public void setMockType(General.JBasicMockType mockType) {
        this.mockType = mockType;
    }

    public General.JConnectionType getMockConnection() {
        return mockConnection;
    }

    public void setMockConnection(General.JConnectionType mockConnection) {
        this.mockConnection = mockConnection;
    }

}
