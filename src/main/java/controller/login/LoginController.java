package controller.login;

import Authentication.FacebookAuth;
import Authentication.GoogleAuth;
import dto.AccountDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;
import service.IAccountService;
import service.Impl.AccountServiceImpl;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;

@WebServlet(urlPatterns = { "/login", "/loginFacebook", "/view/loginEmail"})
public class LoginController extends HttpServlet {
    private  IAccountService accountService = new AccountServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        AccountDTO accountDTO = new AccountDTO();
        String path = req.getServletPath();
        if("/login".equals(path)) { // Login Google
            String code = req.getParameter("code");
            GoogleAuth gg = new GoogleAuth();
            String accessToken = gg.getToken(code);
            accountDTO = gg.getUserInfoFromGoogle(accessToken);
            if(accountService.findAccountByEmail(accountDTO.getEmail()) == null) { // Neu tai khoản chua ton tai
                accountService.InsertAccount(accountDTO);
            }
        } else if("/loginFacebook".equals(path)) {  //Login Facebook
            String code = req.getParameter("code");
            FacebookAuth facebookLogin = new FacebookAuth();
            String accessToken = facebookLogin.getToken(code);
            accountDTO = facebookLogin.getUserInforFromFacebook(accessToken);
            if(accountService.findAccountByEmail(accountDTO.getEmail()) == null) { // Neu tai khoản chua ton tai
                accountService.InsertAccount(accountDTO);
            }
        }
        //Tao mot session moi hoac lay session hien co
        accountDTO = accountService.findAccoutByProvide(accountDTO.getProviderID(),accountDTO.getAuthProvider());
        HttpSession session = req.getSession();
        //Luu thong tin nguoi dung vao session
        req.setAttribute("loginSuccess" , true);
        session.setAttribute("user", accountDTO);
        resp.sendRedirect("/home");
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AccountDTO account = new AccountDTO();
        String path = req.getServletPath();
        if("/view/loginEmail".equals(path)) { //Login Email/Password
            String email = req.getParameter("email");
            String password = req.getParameter("password");
            String captchaResponse = req.getParameter("g-recaptcha-response");

            account.setEmail(email);
            account.setPassword(password);

            boolean isCaptchaValid = validateCaptcha(captchaResponse);
            if(!isCaptchaValid) {
                req.setAttribute("errorMessage", "Vui lòng xác thực CAPTCHA");
                req.getRequestDispatcher("/view/login.jsp").forward(req, resp);
                return;
            }

            if(accountService.findAccountForLogin(account)){
                AccountDTO accountDTO = accountService.findAccountByEmail(email);
                //Tao mot session moi hoac lay session hien co
                HttpSession session = req.getSession();
                //Luu thong tin nguoi dung vao session
                session.setAttribute("user", accountDTO);
                resp.sendRedirect("/home");
            } else {
                req.setAttribute("loginSuccess" , true);
                req.setAttribute("errorMessage", "Email hoặc Password không chính xác");
                req.getRequestDispatcher("/view/login.jsp").forward(req, resp);
            }
       }
    }

    private boolean validateCaptcha(String captchaResponse) {
        String secretKey = "6LdFY4EqAAAAAABgwo4hSIWvi2j_XLdcD_mGSivA";
        String url = "https://www.google.com/recaptcha/api/siteverify";

        try {
            // Gửi yêu cầu tới Google API
            URL obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setDoOutput(true);

            String postParams = "secret=" + secretKey + "&response=" + captchaResponse;

            OutputStream os = con.getOutputStream();
            os.write(postParams.getBytes());
            os.flush();
            os.close();

            // Nhận phản hồi từ Google API

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Phân tích phản hồi JSON
            JSONObject jsonObject = new JSONObject(response.toString());
            return jsonObject.getBoolean("success");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}