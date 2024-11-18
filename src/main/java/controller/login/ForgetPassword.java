package controller.login;

import Authentication.ResetPassword;
import dto.AccountDTO;
import dto.TokenForgetPasswordDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.IAccountService;
import service.ITokenForgetPw;
import service.Impl.AccountServiceImpl;
import service.Impl.TokenForgetPw;

import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet(urlPatterns = {"/view/requestPassword", "/view/resetPassword"})
public class ForgetPassword extends HttpServlet {
    IAccountService accountService = new AccountServiceImpl();
    ITokenForgetPw tokenForgetPw = new TokenForgetPw();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        if(path.equals("/view/requestPassword")) {
            req.getRequestDispatcher("/view/requestPassword.jsp").forward(req, resp);
        } else {
            req.getRequestDispatcher("/view/resetPassword.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        if(path.equals("/view/requestPassword")) {
            String email = req.getParameter("email");
            AccountDTO accountDTO = accountService.findAccountByEmail(email);
            if(accountDTO != null) {
                ResetPassword resetPassword = new ResetPassword();
                String token = resetPassword.generateToken();
                String linkReset = "http://localhost:8080/view/resetPassword?token=" + token + "&email=" + email;
                LocalDateTime timeNow = resetPassword.expiryTime();
                TokenForgetPasswordDTO tokenDTO = new TokenForgetPasswordDTO(token, timeNow, false, accountDTO);
               boolean isInsert = tokenForgetPw.insertToken(tokenDTO);
               if(!isInsert){
                   req.setAttribute("errorEmail", "Have error in server.");
                   req.getRequestDispatcher("/view/requestPassword.jsp").forward(req, resp);
                   return;
               }
               boolean isSend = resetPassword.sendEmail(email, linkReset);
               if(!isSend){
                   req.setAttribute("errorEmail", "Can not send request");
                   req.getRequestDispatcher("/view/requestPassword.jsp").forward(req, resp);
                   return;
               }
               req.setAttribute("errorEmail", "send request successfully");
               req.getRequestDispatcher("/view/requestPassword.jsp").forward(req, resp);


             } else {
                req.setAttribute("errorEmail", "Email không tồn tai.");
                req.getRequestDispatcher("/view/requestPassword.jsp").forward(req, resp);
            }
        } else if(path.equals("/view/resetPassword")) {
            String token = req.getParameter("token");
            if(token != null){
                ResetPassword resetPassword = new ResetPassword();

                TokenForgetPasswordDTO tokenForgetPasswordDTO = tokenForgetPw.getToken(token);
                if(tokenForgetPasswordDTO == null) {
                    req.setAttribute("errorEmail", "token invalid");
                    req.getRequestDispatcher("/view/requestPassword.jsp").forward(req, resp);
                }
               else if(tokenForgetPasswordDTO.isUsed()){
                    req.setAttribute("errorEmail", "token used");
                    req.getRequestDispatcher("/view/requestPassword.jsp").forward(req, resp);
                }
                else if(resetPassword.isExpireTime(tokenForgetPasswordDTO.getExpireTime())){
                    req.setAttribute("errorEmail", "token expired");
                    req.getRequestDispatcher("/view/requestPassword.jsp").forward(req, resp);
                } else {

                }
            }
        }
    }
}
