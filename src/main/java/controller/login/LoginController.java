package controller.login;

import dto.AccountDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.IAccountService;
import service.Impl.AccountServiceImpl;

import java.io.IOException;

@WebServlet(urlPatterns = { "/login", "/loginFacebook", "/view/loginEmail"})
public class LoginController extends HttpServlet {
    private  IAccountService accountService = new AccountServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        AccountDTO accountDTO = new AccountDTO();
        String path = req.getServletPath();
        boolean flag = false;
        if("/login".equals(path)) {
            String code = req.getParameter("code");
            GoogleAuth gg = new GoogleAuth();
            String accessToken = gg.getToken(code);
            accountDTO = gg.getUserInfoFromGoogle(accessToken);
            accountService.InsertAccount(accountDTO);
        } else if("/loginFacebook".equals(path)) {
            String code = req.getParameter("code");
            FacebookAuth facebookLogin = new FacebookAuth();
            String accessToken = facebookLogin.getToken(code);
            accountDTO = facebookLogin.getUserInforFromFacebook(accessToken);
            accountService.InsertAccount(accountDTO);
        } else if("/view/loginEmail".equals(path)) {
            String email = req.getParameter("email");
            String password = req.getParameter("password");
            accountDTO.setEmail(email);
            accountDTO.setPassword(password);
            if(accountService.findAccountForLogin(accountDTO)){
                flag = true;
            } else {
                flag = false;
            }
        }
        //Tao mot session moi hoac lay session hien co
        HttpSession session = req.getSession();
        //Luu thong tin nguoi dung vao session
        session.setAttribute("user", accountDTO);
        if(flag == true){
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        } else {
            req.setAttribute("errorMessage", "Email hoặc mật khẩu không chính xác");
            req.getRequestDispatcher("/view/login.jsp").forward(req, resp);
        }

    }
}
