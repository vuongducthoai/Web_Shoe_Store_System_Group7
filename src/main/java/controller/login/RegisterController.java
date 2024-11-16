package controller.login;

import dto.AccountDTO;
import dto.AddressDTO;
import dto.CustomerDTO;
import enums.AuthProvider;
import enums.RoleType;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ICustomerService;
import service.Impl.CustomerServiceImpl;

import java.io.IOException;
import java.util.Date;


@WebServlet("/view/register")
public class RegisterController extends HttpServlet {
    private ICustomerService customerService = new CustomerServiceImpl();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fullName = req.getParameter("fullName");
        String phone = req.getParameter("phone");
        int day = Integer.parseInt(req.getParameter("day"));
        int month = Integer.parseInt(req.getParameter("month"));
        int year = Integer.parseInt(req.getParameter("year"));
        Date dateOfBirth = new Date(day, month - 1, year);
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
        customerDTO.getAccount().setPassword(password);
        customerDTO.getAccount().setAuthProvider(AuthProvider.LOCAL);
        customerDTO.getAccount().setRole(RoleType.CUSTOMER);
        customerDTO.setFullName(fullName);
        customerDTO.setPhone(phone);

        customerService.insertCustomer(customerDTO);
        resp.sendRedirect("/index.jsp");
    }
}
