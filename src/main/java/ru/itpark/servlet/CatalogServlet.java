package ru.itpark.servlet;

import ru.itpark.service.AutoService;
import ru.itpark.service.FileService;
import ru.itpark.util.ResourcesPaths;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class CatalogServlet extends HttpServlet {
    private AutoService autoService;
    private FileService fileService;

    @Override
    public void init() throws ServletException {
        InitialContext context = null;
        try {
            context = new InitialContext();
            autoService = (AutoService) context.lookup(ResourcesPaths.autoServicePath);
            fileService = (FileService) context.lookup(ResourcesPaths.fileServicePath);
        } catch (NamingException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("items", autoService.getAll());
            req.getRequestDispatcher(ResourcesPaths.catalogJspPath).forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            var name = req.getParameter("name");
            var description = req.getParameter("description");
            var part = req.getPart("image");

            var image = fileService.writeFile(part);

            autoService.create(name, description, image);
            resp.sendRedirect(String.join("/", req.getContextPath(), req.getServletPath()));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }
}
