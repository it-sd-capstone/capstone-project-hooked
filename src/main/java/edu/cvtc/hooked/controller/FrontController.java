package edu.cvtc.hooked.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

// Map every route you want to support
@WebServlet(urlPatterns = {
        "/", "/index",
        "/about", "/locations", "/species", "/bait", "/logout"
})
public class FrontController extends HttpServlet {

    private String viewFor(String path) {
        // Normalize “/index/” to “/index”
        if (path == null || path.equals("/")) return "/WEB-INF/views/index.jsp";

        return switch (path) {
            case "/index"     -> "/WEB-INF/views/index.jsp";
            case "/about"     -> "/WEB-INF/views/about.jsp";
            case "/locations" -> "/WEB-INF/views/locations.jsp";
            case "/species"   -> "/WEB-INF/views/species.jsp";
            case "/bait"      -> "/WEB-INF/views/bait.jsp";
            case "/logout"    -> "/WEB-INF/views/logout.jsp"; // or do logout logic first
            default           -> "/WEB-INF/views/error.jsp";  // optional fallback page
        };
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getServletPath();
        req.getRequestDispatcher(viewFor(path)).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // If any form POST should render a JSP, route here too.
        doGet(req, resp);
    }
}
