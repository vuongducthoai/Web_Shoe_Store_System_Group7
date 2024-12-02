package controller.login;

import dto.AccountDTO;
import dto.AddressDTO;
import dto.CustomerDTO;
import dto.UserDTO;
import enums.AuthProvider;
import enums.RoleType;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.ICustomerService;
import service.IUserService;
import service.Impl.CustomerServiceImpl;
import service.Impl.UserServiceImpl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@WebServlet("/InformationUser")
public class ProfileController extends HttpServlet {
    IUserService userService = new UserServiceImpl();
    ICustomerService customerService = new CustomerServiceImpl();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");
        if(user != null) {
            int userId = user.getUserID();
            CustomerDTO customerDTO = userService.getInformationUser(userId);
            req.setAttribute("customerDTO", customerDTO);
            System.out.println("User ID: " + userId);
            req.getRequestDispatcher("/view/profile.jsp").forward(req, resp);
        } else {
            System.out.println("No user session found");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");
        if(user != null) {
            int userId = user.getUserID();
            String fullName = req.getParameter("fullName");
            String phone = req.getParameter("phone");
            int day = Integer.parseInt(req.getParameter("day"));
            int month = Integer.parseInt(req.getParameter("month"));
            int year = Integer.parseInt(req.getParameter("year"));
            LocalDate localDate = LocalDate.of(year, month, day);
            int houseNumber =Integer.parseInt(req.getParameter("houseNumber")) ;
            String streetName = req.getParameter("streetName");
            String email = req.getParameter("email");
            String province = req.getParameter("province");
            String district = req.getParameter("district");
            String ward = req.getParameter("ward");
            String password = req.getParameter("password");

            CustomerDTO customerDTO = new CustomerDTO();
            if (customerDTO.getAddressDTO() == null) {
                customerDTO.setAddressDTO(new AddressDTO());
            }

            Date dateOfBirth = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            customerDTO.setDateOfBirth(dateOfBirth);
            customerDTO.getAddressDTO().setHouseNumber(houseNumber);
            customerDTO.getAddressDTO().setStreetName(streetName);
            customerDTO.getAddressDTO().setProvince(province);
            customerDTO.getAddressDTO().setDistrict(district);
            customerDTO.getAddressDTO().setCity(ward);
            if (customerDTO.getAccount() == null) {
                customerDTO.setAccount(new AccountDTO());
            }

            customerDTO.getAccount().setEmail(email);
            System.out.println("email " + customerDTO.getAccount().getEmail());
            customerDTO.getAccount().setPassword(password);
            customerDTO.getAccount().setAuthProvider(AuthProvider.LOCAL);
            customerDTO.getAccount().setRole(RoleType.CUSTOMER);
            customerDTO.setFullName(fullName);
            customerDTO.setUserID(userId);
            customerDTO.setPhone(phone);
            try {
                if (!customerService.updateCustomer((customerDTO))) {

                    req.setAttribute("errorEmail", "Email đã tồn tại!");
                    System.out.println(req.getAttribute("errorEmail"));
                    req.setAttribute("customerDTO", customerDTO);
                    req.getRequestDispatcher("/view/login.jsp").forward(req, resp);
                    return;
                }
                req.getRequestDispatcher("/view/profile.jsp").forward(req, resp);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                req.setAttribute("errorMessage", "Đã xảy ra lỗi. Vui lòng thử lại!");
                req.setAttribute("customerDTO", customerDTO);
                req.getRequestDispatcher("/view/login.jsp").forward(req, resp);
            }
        }
    }
}
