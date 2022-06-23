package media.platform.acsrmqsim.common;

public class SleepUtil {

    private SleepUtil() {
        throw new IllegalStateException("Static class");
    }

    public static void trySleep(int msec) {
        try {
            Thread.sleep(msec);
        } catch (InterruptedException e) {
            //Thread Pool에 interrupt 발생을 알림.(문제될 건 없지만, 관용적으로 사용 권장)
            Thread.currentThread().interrupt();
        }
    }
}
