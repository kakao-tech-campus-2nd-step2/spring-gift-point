package gift.utils;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ScriptUtils {

    public static void init(HttpServletResponse response) {
        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("euc-kr");
    }

    public static void alert(HttpServletResponse response, String message) throws IOException {
        init(response);
        PrintWriter out = response.getWriter();
        out.println("<script>alert('" + message + "');</script> ");
        out.flush();
    }

    public static void alertAndMovePage(HttpServletResponse response,
        String message, String nextPage) throws IOException {
        init(response);
        PrintWriter out = response.getWriter();
        out.println(
            "<script>alert('" + message + "'); location.href='" + nextPage + "';</script> ");
    }

    public static void alertAndBackPage(HttpServletResponse response,
        String message) throws IOException {
        init(response);
        PrintWriter out = response.getWriter();
        out.println("<script>alert('" + message + "'); history.go(-1);</script> ");
    }

}
