package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ControllerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (isClearRequest(req)) {
            getServletContext().getNamedDispatcher("ClearDataServlet").forward(req, resp);
        } else if (isPointRequest(req)) {
            getServletContext().getNamedDispatcher("AreaCheckServlet").forward(req, resp);
        } else {
            getServletContext().getRequestDispatcher("/form.jsp").forward(req, resp);
        }
    }

    private boolean isClearRequest(HttpServletRequest req) {
        String paramValue = req.getParameter("clear");
        return (paramValue != null && paramValue.equals("true"));
    }

    private boolean isPointRequest(HttpServletRequest req) {
        return req.getParameter("xVal") != null && req.getParameter("yVal") != null
                && req.getParameter("rVal") != null && req.getParameter("timezone") != null;
    }
}
