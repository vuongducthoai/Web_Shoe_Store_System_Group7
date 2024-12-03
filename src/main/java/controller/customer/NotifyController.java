package controller.customer;

import dao.Impl.NotifyDAO;
import dto.NotifyDTO;
import dto.UserDTO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/Notify")
public class NotifyController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UserDTO userDTO = (UserDTO) session.getAttribute("user");

        // Kiểm tra và chuyển đổi dữ liệu
        if (userDTO != null) {
            int customerID = userDTO.getUserID();
            NotifyDAO notifyDAO = new NotifyDAO();
            List<NotifyDTO> notifications = notifyDAO.LoadNotifies(customerID);

            request.setAttribute("notifications", notifications);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/view/notify.jsp");
            dispatcher.forward(request, response);
        }
        else{
            response.sendRedirect("/view/login.jsp");
        }


    }
}
