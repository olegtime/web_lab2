package servlets;

import exceptions.InvalidValueException;
import model.Hit;
import model.Point;
import model.PointData;
import model.PointHandler;


import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AreaCheckServlet extends HttpServlet {
    public static final String HITS_DATA_ATTRIBUTE = "hitsData";
    private final PointHandler pointHandler = new PointHandler();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            var currentHit = pointHandler.getHitInfo(buildPointData(req));
            var session = req.getSession();
            saveHitInSession(session, currentHit);
            fillResponse(resp, session);
        } catch (InvalidValueException e) {
            resp.sendError(403, e.getMessage());
        }
    }

    private PointData buildPointData(HttpServletRequest req) {
        try {
            var xVal = Double.parseDouble(req.getParameter("xVal"));
            var yVal = Double.parseDouble(req.getParameter("yVal"));
            var rVal = Double.parseDouble(req.getParameter("rVal"));
            var timezone = Long.parseLong(req.getParameter("timezone"));

            return new PointData(new Point(xVal, yVal), rVal, timezone);
        } catch (NumberFormatException exception) {
            throw new InvalidValueException("Некорректный формат параметров!");
        }
    }

    private void saveHitInSession(HttpSession session, Hit hit) {
        getHits(session).add(hit);
    }

    private void fillResponse(HttpServletResponse resp, HttpSession session) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");

        PrintWriter writer = resp.getWriter();
        writer.print(getTableHtml(session));
    }

    private String getTableHtml(HttpSession session) {
        var tableBuilder = new StringBuilder()
                .append("<table>")
                .append("<tbody>")
                .append("<tr>")
                .append("<th>X</th>")
                .append("<th>Y</th>")
                .append("<th>R</th>")
                .append("<th>Current <br> time</th>")
                .append("<th>Execution <br> time, ms</th>")
                .append("<th>Hit</th>")
                .append("</tr>");

        getHits(session).forEach((hit) -> {
            tableBuilder
                    .append("<tr>")
                    .append("<td>").append(String.format("%.2f", hit.getXVal())).append("</td>")
                    .append("<td>").append(String.format("%.2f", hit.getYVal())).append("</td>")
                    .append("<td>").append(Math.round(hit.getRVal())).append("</td>")
                    .append("<td>").append(formatLocalDateTime(hit.getCurrentTime())).append("</td>")
                    .append("<td>").append(hit.getExecutionTime()).append("</td>")
                    .append("<td class='calculation_result'>").append(hit.isHit()).append("</td>")
                    .append("</tr>");
        });

        tableBuilder.append("</tbody>");
        tableBuilder.append("</table>");

        return tableBuilder.toString();
    }

    private List<Hit> getHits(HttpSession session) {
        if (session.getAttribute(HITS_DATA_ATTRIBUTE) == null) {
            session.setAttribute(HITS_DATA_ATTRIBUTE, new ArrayList<>());
        }
        return (List<Hit>) session.getAttribute(HITS_DATA_ATTRIBUTE);
    }

    private String formatLocalDateTime(LocalDateTime localDateTime){
        return localDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss", Locale.ROOT));
    }
}
