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
                    req.setAttribute("userID", customerDTO.getUserID());
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
        try {
            // Lấy dữ liệu từ form
            String fullName = req.getParameter("fullName");
            String phone = req.getParameter("phone");
            String dateOfBirthStr = req.getParameter("dateOfBirth");

            int userID = Integer.parseInt(req.getParameter("userID"));
            int houseNumber = Integer.parseInt(req.getParameter("houseNumber"));
            String streetName = req.getParameter("streetName");
            String district = req.getParameter("district");
            String city = req.getParameter("city");

            // Chuyển đổi dateOfBirth thành java.sql.Date
            java.sql.Date dateOfBirth = java.sql.Date.valueOf(dateOfBirthStr);

            // Tạo các DTO
            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setUserID(userID);
            customerDTO.setFullName(fullName);
            customerDTO.setPhone(phone);
            customerDTO.setDateOfBirth(dateOfBirth);

            AddressDTO addressDTO = new AddressDTO();
            addressDTO.setHouseNumber(houseNumber);
            addressDTO.setStreetName(streetName);
            addressDTO.setDistrict(district);
            addressDTO.setCity(city);


            // Cập nhật dữ liệu
            boolean customerUpdated = iCustomerService.updateCustomer(customerDTO);
            boolean addressUpdated = iAddressService.updateAddress(addressDTO);

            // Kiểm tra kết quả cập nhật và thực hiện chuyển hướng
            if (customerUpdated || addressUpdated) {
                // Gọi lại doGet để lấy thông tin mới
                resp.sendRedirect("Information"); // Điều này sẽ gọi lại doGet
                System.out.println("Cập nhật thành công");
            } else {
                // Nếu cập nhật thất bại, chuyển hướng về cùng trang
                System.out.println("Cập nhật thất bại");
            }
        } catch (Exception e) {
            e.printStackTrace();  // In chi tiết lỗi ra console
            System.out.println("Error in doPost: " + e.getMessage());
            resp.sendRedirect("/view/customer/informationCustomer.jsp");  // Chuyển hướng khi có lỗi
        }
    }

}
