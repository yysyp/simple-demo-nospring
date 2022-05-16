/**Copyright CopyLeft it's Fri Feb 28 23:46:35 CST 2020 Good luck~~~*/
package ps.demo.util;

import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.*;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * FileUrlConvertUtils fileUrlUtils = new FileUrlConvertUtils();
 *byte[] bytes = fileUrlUtils.loadFileByteFromURL("https://no-chinese/xxx");
* if (bytes == null) {
*     return;
* }
 * //etc.
 *
 */
@Slf4j
public class MyFileUrlConvertUtil {

    /**
     * 从url获取文件内容的字节数组
     *
     * @param fileUrl
     * @return
     */
    public byte[] loadFileByteFromURL(String fileUrl) {

        if (fileUrl.startsWith("http://")) {
            return this.httpConverBytes(fileUrl);
        } else if (fileUrl.startsWith("https://")) {
            return this.httpsConverBytes(fileUrl);
        } else {
            return null;
        }

    }

    /**
     * @param fileUrl
     * @return
     * @MethodName httpConverBytes
     * @Description http路径文件内容获取
     */
    public byte[] httpConverBytes(String fileUrl) {
        BufferedInputStream in = null;
        ByteArrayOutputStream out = null;
        URLConnection conn = null;

        try {
            URL url = new URL(fileUrl);
            conn = url.openConnection();

            in = new BufferedInputStream(conn.getInputStream());

            out = new ByteArrayOutputStream(1024);
            byte[] temp = new byte[1024];
            int size = 0;
            while ((size = in.read(temp)) != -1) {
                out.write(temp, 0, size);
            }
            byte[] content = out.toByteArray();
            return content;
        } catch (Exception e) {
            log.error(e.getMessage(), e);

        } finally {
            try {
                in.close();
            } catch (IOException e) {
                //

            }
            try {
                out.close();
            } catch (IOException e) {
                //
            }
        }
        return null;
    }

    /**
     * @param fileUrl
     * @return
     * @MethodName httpsConverBytes
     * @Description https路径文件内容获取
     */
    private byte[] httpsConverBytes(String fileUrl) {
        BufferedInputStream inStream = null;
        ByteArrayOutputStream outStream = null;

        try {

            TrustManager[] tm = {new TrustAnyTrustManager()};
            SSLContext sc = SSLContext.getInstance("SSL", "SunJSSE");
            sc.init(null, tm, new java.security.SecureRandom());

            HttpsURLConnection conn = (HttpsURLConnection) new URL(fileUrl).openConnection();
            conn.setSSLSocketFactory(sc.getSocketFactory());
            conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.connect();

            inStream = new BufferedInputStream(conn.getInputStream());
            outStream = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }

            byte[] content = outStream.toByteArray();
            return content;

        } catch (Exception e) {
            log.error(e.getMessage(), e);

        } finally {
            if (null != inStream) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    //
                }
            }

            if (null != outStream) {
                try {
                    outStream.close();
                } catch (IOException e) {

                }
            }
        }

        return null;
    }

    /**
     * 信任证书的管理器
     *
     * @author: blabla
     */
    private static class TrustAnyTrustManager implements X509TrustManager {
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    public static String getExtensionName(String filename) {
        if (filename != null && filename.length() > 0) {
            int dot = filename.lastIndexOf('.');
            if (dot > -1 && dot < filename.length() - 1) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

}

