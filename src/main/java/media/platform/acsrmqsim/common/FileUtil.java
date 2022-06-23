package media.platform.acsrmqsim.common;

import java.io.*;

/** 파일 -> String 변환 util */
public class FileUtil {

    private FileUtil(){}

    /**
     * @param path : filePath
     * @return String
     * */
    public static String filepathToString(String path) throws IOException {

        InputStream is = new FileInputStream(new File(path));
        StringBuilder sb = new StringBuilder();
        String line;
        try(BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }

    /**
     * @param file : String 변환하려는 파일
     * @return String
     * */
    public static String fileToString(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        StringBuilder sb = new StringBuilder();
        String line;
        try(BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }

}
