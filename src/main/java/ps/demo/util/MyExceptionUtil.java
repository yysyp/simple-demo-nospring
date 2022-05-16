package ps.demo.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class MyExceptionUtil {

    public static String stackTraceToString(Throwable e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw, true));
        return sw.toString();
    }

}
