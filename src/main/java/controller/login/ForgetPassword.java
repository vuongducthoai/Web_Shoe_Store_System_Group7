package controller.login;

import Authentication.ResetPassword;
import dto.AccountDTO;
import dto.TokenForgetPasswordDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
    ResetPassword resetPassword = new ResetPassword();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        if(path.equals("/view/requestPassword")) {
            req.getRequestDispatcher("/view/requestPassword.jsp").forward(req, resp);
        } else if(path.equals("/view/resetPassword")) {
            String token = req.getParameter("token");
            HttpSession session = req.getSession();
            if(token != null){

                TokenForgetPasswordDTO tokenForgetPasswordDTO = tokenForgetPw.getToken(token);
                System.out.println(tokenForgetPasswordDTO.getExpireTime());
                if(tokenForgetPasswordDTO == null) {  // Token khong hop le
                    req.setAttribute("mess", "Token invalid");
                    req.getRequestDispatcher("/view/requestPassword.jsp").forward(req, resp);
                    return;
                }

                 if (tokenForgetPasswordDTO.getExpireTime() == null ||
                        resetPassword.isExpireTime(tokenForgetPasswordDTO.getExpireTime())) {
                    req.setAttribute("mess", "Token is invalid or expired");
                    req.getRequestDispatcher("/view/requestPassword.jsp").forward(req, resp);
                    return;
                }

                 if(resetPassword.isExpireTime(tokenForgetPasswordDTO.getExpireTime())){  // Token da vuot qua thoi gian
                    req.setAttribute("mess", "Token is expiry time");
                    req.getRequestDispatcher("/view/requestPassword.jsp").forward(req, resp);
                    return;
                }
                 // Truong hop thanh cong
                    AccountDTO accoutDTO = tokenForgetPasswordDTO.getAccountDTO();
                    req.setAttribute("email", accoutDTO.getEmail());
                    session.setAttribute("token", tokenForgetPasswordDTO.getToken());
                    req.getRequestDispatcher("/view/resetPassword.jsp").forward(req, resp);
            } else {
                req.getRequestDispatcher("/view/requestPassword.jsp").forward(req, resp);
            }
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
                   req.setAttribute("mess", "Have error in server.");
                   req.getRequestDispatcher("/view/requestPassword.jsp").forward(req, resp);
                   return;
               }
               boolean isSend = resetPassword.sendEmail(email, linkReset);
               if(!isSend){
                   req.setAttribute("mess", "Can not send request");
                   req.getRequestDispatcher("/view/requestPassword.jsp").forward(req, resp);
                   return;
               }
               req.setAttribute("mess", "send request successfully");
               req.getRequestDispatcher("/view/requestPassword.jsp").forward(req, resp);


             } else {
                req.setAttribute("mess", "Email không tồn tai.");
                req.getRequestDispatcher("/view/requestPassword.jsp").forward(req, resp);
            }
        } else if(path.equals("/view/resetPassword")) {
            String email = req.getParameter("email");
            String password = req.getParameter("password");
            String confirmPassword = req.getParameter("confirm_password");

            //ValidatePassword
            if(!password.equals(confirmPassword)) {
                req.setAttribute("mess", "Confirm password must same password");
                req.setAttribute("email", email);
                req.getRequestDispatcher("/view/resetPassword.jsp").forward(req, resp);
                return;
            }
            HttpSession session = req.getSession();
            String tokenStr = (String) session.getAttribute("token");
            TokenForgetPasswordDTO tokenForgetPasswordDTO = tokenForgetPw.getToken(tokenStr);
            if(tokenForgetPasswordDTO == null) {  // Token khong hop le
                req.setAttribute("mess", "Token invalid");
                req.getRequestDispatcher("/view/requestPassword.jsp").forward(req, resp);
                return;
            }
            if(tokenForgetPasswordDTO.isUsed()){ // Token da duoc su dung
                req.setAttribute("mess", "Token used");
                req.getRequestDispatcher("/view/requestPassword.jsp").forward(req, resp);
                return;
            }
            if(tokenForgetPasswordDTO.getExpireTime() == null ||resetPassword.isExpireTime(tokenForgetPasswordDTO.getExpireTime())){  // Token da vuot qua thoi gian
                req.setAttribute("mess", "Token is expiry time");
                req.getRequestDispatcher("/view/requestPassword.jsp").forward(req, resp);
                return;
            }
            //update is used of token
            tokenForgetPasswordDTO.setToken(tokenStr);
            tokenForgetPasswordDTO.setUsed(true);

            AccountDTO accoutDTO = tokenForgetPasswordDTO.getAccountDTO();
            accoutDTO.setPassword(password); // Đặt mật khẩu mới
            tokenForgetPasswordDTO.setAccountDTO(accoutDTO);
            tokenForgetPw.updateTokenPassword(tokenForgetPasswordDTO);
            req.getRequestDispatcher("/view/login.jsp").forward(req, resp);
        }
    }
}
