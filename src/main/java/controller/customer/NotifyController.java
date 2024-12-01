package controller.customer;

import dao.Impl.NotifyDAO;
import dto.NotifyDTO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/Notify")
public class NotifyController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int customerID = Integer.parseInt(request.getParameter("customerID"));

        NotifyDAO notifyDAO = new NotifyDAO();
        List<NotifyDTO> notifications = notifyDAO.LoadNotifies(customerID);

        request.setAttribute("notifications", notifications);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/view/notify.jsp");
        dispatcher.forward(request, response);
    }
}
