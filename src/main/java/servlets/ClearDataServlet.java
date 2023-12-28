package servlets;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static servlets.AreaCheckServlet.HITS_DATA_ATTRIBUTE;

public class ClearDataServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        req.getSession().removeAttribute(HITS_DATA_ATTRIBUTE);
    }
}
