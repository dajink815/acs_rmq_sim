package media.platform.acsrmqsim.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/** 명령어 UDP 방식으로 전달받기 위한 서버 */
public class UdpServer {
    static final Logger log = LoggerFactory.getLogger(UdpServer.class);
    private DatagramSocket ds;
    private DatagramPacket dp;

    //메시지 수신 위한 환경 세팅
    public UdpServer(int port) {
        try {
            ds = new DatagramSocket(port);
        } catch (SocketException e) {
            log.error("UdpServer Error Occurs", e);
        }
    }

    //메시지 수신 받고 String 변환 후 리턴
    public String recv() {

        String msg = "";
        try {
            byte[] buffer = new byte[256];
            dp = new DatagramPacket(buffer, buffer.length);
            ds.receive(dp);
            msg = new String(dp.getData()).trim();

        } catch (IOException e) {
            log.error("UdpServer.recv Error", e);
        }

        return msg;
    }

}


