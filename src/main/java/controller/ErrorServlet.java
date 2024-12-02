package controller;

import java.io.*;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "ErrorServletServlet", value = "/ErrorServlet-servlet")
public class ErrorServlet extends HttpServlet {


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        try{
            String errorMessage = request.getParameter("errorMessage");
            // Hello
            if (errorMessage == null) {
                errorMessage = "Có lỗi xảy ra, vui lòng thử lại!";
            }
            request.setAttribute("message", errorMessage);

            // Forward the request to error.jsp
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public void destroy() {
    }
}