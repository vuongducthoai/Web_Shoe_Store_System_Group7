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

@WebServlet(urlPatterns = { "/login", "/loginFacebook", "/loginEmail"})
public class LoginController extends HttpServlet {
    private  IAccountService accountService = new AccountServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        String code = req.getParameter("code");
        AccountDTO accountDTO = getAccountDTO(req, code);
        accountService.InsertAccount(accountDTO);
        //Tao mot session moi hoac lay session hien co
        HttpSession session = req.getSession();
        //Luu thong tin nguoi dung vao session
        session.setAttribute("user", accountDTO);
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }

    private AccountDTO getAccountDTO(HttpServletRequest req, String code) throws IOException {
        String path = req.getServletPath();
        AccountDTO accountDTO = new AccountDTO();
        if("/login".equals(path)) {
            GoogleAuth gg = new GoogleAuth();
            String accessToken = gg.getToken(code);
            accountDTO = gg.getUserInfoFromGoogle(accessToken);
            System.out.println(accountDTO.toString());
        } else if("/loginFacebook".equals(path)) {
            FacebookAuth facebookLogin = new FacebookAuth();
            String accessToken = facebookLogin.getToken(code);
            accountDTO = facebookLogin.getUserInforFromFacebook(accessToken);
        } else {
//            String email = req.getParameter("email");
//            String password = req.getParameter("password");
//            accountDTO.setEmail(email);
//            accountDTO.setPassword(password);
        }
        return accountDTO;
    }
}
