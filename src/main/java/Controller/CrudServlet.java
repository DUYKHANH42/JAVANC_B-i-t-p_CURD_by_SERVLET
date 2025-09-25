/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAO.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.User;

/**
 *
 * @author PC
 */
@WebServlet(name = "CrudServlet", urlPatterns = {"/CrudServlet"})
public class CrudServlet extends HttpServlet {

    private UserDAO userDAO = new UserDAO();

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet CrudServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CrudServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "new":
                    showForm(response, null);
                    break;
                case "insert":
                    insertUser(request, response);
                    break;
                case "edit":
                    editForm(request, response);
                    break;
                case "update":
                    updateUser(request, response);
                    break;
                case "delete":
                    deleteUser(request, response);
                    break;
                default:
                    listUsers(response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void listUsers(HttpServletResponse response) throws IOException {
        List<User> list = userDAO.selectAllUsers();
        PrintWriter out = response.getWriter();
        response.setContentType("text/html;charset=UTF-8");
        out.println("<html><head><title>Danh sách User</title></head><body>");
        out.println("<h2>Danh sách Users</h2>");
        out.println("<a href='CrudServlet?action=new'>➕ Thêm User</a><br><br>");
        out.println("<table border='1' cellpadding='10'>");
        out.println("<tr><th>ID</th><th>Name</th><th>Email</th><th>Country</th><th>Action</th></tr>");
        for (User u : list) {
            out.println("<tr>");
            out.println("<td>" + u.getId() + "</td>");
            out.println("<td>" + u.getName() + "</td>");
            out.println("<td>" + u.getEmail() + "</td>");
            out.println("<td>" + u.getCountry() + "</td>");
            out.println("<td>"
                    + "<a href='CrudServlet?action=edit&id=" + u.getId() + "'>Edit</a> | "
                    + "<a href='CrudServlet?action=delete&id=" + u.getId() + "' "
                    + "onclick=\"return confirm('Xóa user này?');\">Delete</a>"
                    + "</td>");
            out.println("</tr>");
        }
        out.println("</table>");
        out.println("</body></html>");
    }

    private void showForm(HttpServletResponse response, User user) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        boolean isEdit = (user != null);

        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>User Form</title>");
        out.println("<style>");
        out.println("table { border-collapse: collapse; }");
        out.println("td { padding: 8px; }");
        out.println("label { display: inline-block; width: 100px; font-weight: bold; }");
        out.println("input[type=text], input[type=password], input[type=email] { width: 200px; }");
        out.println("</style>");
        out.println("</head><body>");

        out.println("<h2>" + (isEdit ? "Sửa User" : "Thêm User") + "</h2>");
        out.println("<form action='CrudServlet' method='get'>");

        if (isEdit) {
            out.println("<input type='hidden' name='action' value='update'/>");
            out.println("<input type='hidden' name='id' value='" + user.getId() + "'/>");
        } else {
            out.println("<input type='hidden' name='action' value='insert'/>");
        }

        out.println("<table>");
        out.println("<tr><td><label>Tên:</label></td>"
                + "<td><input type='text' name='name' value='" + (isEdit ? user.getName() : "") + "'/></td></tr>");
        out.println("<tr><td><label>Mật khẩu:</label></td>"
                + "<td><input type='password' name='password' value='" + (isEdit ? user.getPassword() : "") + "'/></td></tr>");
        out.println("<tr><td><label>Email:</label></td>"
                + "<td><input type='email' name='email' value='" + (isEdit ? user.getEmail() : "") + "'/></td></tr>");
        out.println("<tr>");
        out.println("<td><label>Quốc gia:</label></td>");
        out.println("<td>");
        out.println("<select name='country'>");
        out.println("<option value=''>-- Chọn quốc gia --</option>");
        out.println("<option value='Vietnam'" + (isEdit && "Vietnam".equals(user.getCountry()) ? " selected" : "") + ">Việt Nam</option>");
        out.println("<option value='USA'" + (isEdit && "USA".equals(user.getCountry()) ? " selected" : "") + ">Mỹ</option>");
        out.println("<option value='Japan'" + (isEdit && "Japan".equals(user.getCountry()) ? " selected" : "") + ">Nhật Bản</option>");
        out.println("<option value='China'" + (isEdit && "China".equals(user.getCountry()) ? " selected" : "") + ">Trung Quốc</option>");
        out.println("</select>");
        out.println("</td>");
        out.println("</tr>");

        out.println("<tr><td></td><td><input type='submit' value='Lưu'/></td></tr>");
        out.println("</table>");

        out.println("</form>");
        out.println("<a href='CrudServlet?action=list'>⬅ Quay lại danh sách</a>");

        out.println("</body></html>");
    }

    private void editForm(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        User u = userDAO.selectUser(id);
        showForm(response, u);
    }

    private void insertUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String country = request.getParameter("country");

        User u = new User(name, password, email, country);
        boolean success = userDAO.insertUser(u);

        PrintWriter out = response.getWriter();
        response.setContentType("text/html;charset=UTF-8");

        if (success) {
            out.println("<script type='text/javascript'>");
            out.println("alert('Thêm user thành công!');");
            out.println("window.location='CrudServlet?action=list';");
            out.println("</script>");
        } else {
            out.println("<script type='text/javascript'>");
            out.println("alert('Thêm user thất bại!');");
            out.println("window.location='CrudServlet?action=new';");
            out.println("</script>");
        }
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String country = request.getParameter("country");

        User u = new User(id, name, password, email, country);
        boolean success = userDAO.updateUser(u);

        PrintWriter out = response.getWriter();
        response.setContentType("text/html;charset=UTF-8");

        if (success) {
            out.println("<script type='text/javascript'>");
            out.println("alert('Update user thành công!');");
            out.println("window.location='CrudServlet?action=list';");
            out.println("</script>");
        }
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        userDAO.deleteUser(id);
        response.sendRedirect("CrudServlet?action=list");
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
