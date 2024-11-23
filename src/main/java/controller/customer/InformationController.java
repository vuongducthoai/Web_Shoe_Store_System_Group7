package controller.customer;

import dto.AccountDTO;
import dto.AddressDTO;
import dto.CustomerDTO;
import service.IAccountService;
import service.IAddressService;
import service.ICartService;
import service.ICustomerService;
import service.Impl.AccountServiceImpl;
import service.Impl.AddressServiceImpl;
import service.Impl.CartServiceImpl;
import service.Impl.CustomerServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(urlPatterns = {"/Information"})
public class InformationController extends HttpServlet {

    IAccountService iAccountService = new AccountServiceImpl();
    ICustomerService iCustomerService = new CustomerServiceImpl();
    IAddressService iAddressService = new AddressServiceImpl();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Lấy session hiện tại
//            HttpSession session = req.getSession(false);

            // Kiểm tra session hợp lệ
//            if (session == null || session.getAttribute("userID") == null) {
//                // Chuyển hướng về trang đăng nhập nếu chưa đăng nhập
//                resp.sendRedirect("login.jsp");
//                return;
//            }

            // Lấy thông tin accountID từ session
//            int accountID = (int) session.getAttribute("userID");
            int accountID =22;


                    // Lấy thông tin tài khoản
            AccountDTO accountDTO = iAccountService.getAccountByID(accountID);

            if (accountDTO != null) {
                // Lấy thông tin khách hàng
                CustomerDTO customerDTO = iCustomerService.getCustomerByID(accountDTO.getUser().getUserID());

                if (customerDTO != null) {
                    AddressDTO addressDTO = customerDTO.getAddressDTO();
                    if (addressDTO != null) {
                        req.setAttribute("numberHouse", addressDTO.getHouseNumber());
                        req.setAttribute("streetName", addressDTO.getStreetName());
                        req.setAttribute("district", addressDTO.getDistrict());
                        req.setAttribute("city", addressDTO.getCity());
                    }
                }

                // Đưa các thông tin của khách hàng vào request
                if (customerDTO != null) {
                    req.setAttribute("fullName", customerDTO.getFullName());
                    req.setAttribute("phone", customerDTO.getPhone());
                    req.setAttribute("dateOfBirth", customerDTO.getDateOfBirth());
                    req.setAttribute("loyalty", customerDTO.getLoyalty());

                }


                // Thêm email từ accountDTO
                req.setAttribute("email", accountDTO.getEmail());

                // Chuyển tiếp đến trang hiển thị thông tin cá nhân
                req.getRequestDispatcher("/view/customer/informationCustomer.jsp").forward(req, resp);

            } else {
                // Nếu không tìm thấy thông tin, chuyển hướng về trang lỗi
                resp.sendRedirect("error.jsp");

            }
        } catch (Exception e) {
            // Xử lý lỗi và chuyển hướng đến trang lỗi
            System.out.println("Error in doGet: " + e.getMessage());
//            resp.sendRedirect("error.jsp");

        }

    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fullName= req.getParameter("fullName");
        String phone = req.getParameter("phone");
        String dateOfBirth = req.getParameter("dateOfBirth");

        CustomerDTO customerDTO = new CustomerDTO();

        Integer userID = Integer.parseInt(req.getParameter("userID"));
        Integer houseNumber = Integer.parseInt(req.getParameter("houseNumber"));
        String streetName = req.getParameter("streetName");
        String district = req.getParameter("district");
        String city = req.getParameter("city");

        // Đưa dữ liệu vào DTO

        AddressDTO addressDTO = new AddressDTO();
        //addressDTO.getAddressID(userID);
        addressDTO.setHouseNumber(houseNumber);
        addressDTO.setStreetName(streetName);
        addressDTO.setDistrict(district);
        addressDTO.setCity(city);
        boolean customerUpdated = iCustomerService.updateCustomer(customerDTO);
        boolean addressUpdated = iAddressService.updateAddress(addressDTO);
        // Kiểm tra kết quả và phản hồi người dùng
        if (customerUpdated && addressUpdated) {
            resp.sendRedirect("informationCustomer.jsp?success=true");
        } else {
            resp.sendRedirect("informationCustomer.jsp?error=true");
        }
    }
}
