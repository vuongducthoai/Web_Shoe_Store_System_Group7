package Authentication;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import constant.IConstant;
import dto.AccountDTO;
import dto.UserDTO;
import enums.AuthProvider;
import enums.RoleType;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;

import java.io.IOException;

public class GoogleAuth {

    public static String getToken(String code) throws IOException {

        String response = Request.Post(IConstant.GOOGLE_LINK_GET_TOKEN)
                .bodyForm(
                        Form.form()
                                .add("client_id", IConstant.GOOGLE_CLIENT_ID)
                                .add("client_secret", IConstant.GOOGLE_CLIENT_SECRET)
                                .add("redirect_uri", IConstant.GOOGLE_REDIRECT_URI)
                                .add("code", code)
                                .add("grant_type", IConstant.GOOGLE_GRANT_TYPE)
                                .build()
                )
                .execute().returnContent().asString();

        JsonObject jobj = new Gson().fromJson(response, JsonObject.class);
        String accessToken = jobj.get("access_token").getAsString();

        return accessToken;
    }


    public static AccountDTO getUserInfoFromGoogle(String accessToken) throws IOException {
        // URL để lấy thông tin người dùng từ Google API
        String googleApiUrl = IConstant.GOOGLE_LINK_GET_USER_INFO;

        // Gửi yêu cầu GET để lấy thông tin người dùng từ Google API
        String response = Request.Get(googleApiUrl)
                .addHeader("Authorization", "Bearer " + accessToken)
                .execute()
                .returnContent()
                .asString();

        // In ra response để kiểm tra thông tin trả về từ Google API
        System.out.println("Google API response: " + response);

        // Parse JSON response từ Google API
        JsonObject jsonResponse = new Gson().fromJson(response, JsonObject.class);

        // Lấy các thông tin người dùng từ response (ví dụ: email, tên, ID)
        String email = jsonResponse.get("email").getAsString();
        String fullName = jsonResponse.get("name").getAsString();
        String providerID = jsonResponse.get("sub").getAsString();  // ID duy nhất của người dùng từ Google

        // Tạo AccountDTO từ dữ liệu nhận được
        AccountDTO account = new AccountDTO();
        account.setEmail(email);
        if(account.getUser() == null) {
            account.setUser(new UserDTO());
        }
        account.getUser().setFullName(fullName);
        account.setProviderID(providerID);
        account.setAuthProvider(AuthProvider.GOOGLE); // Gán provider là Google
        account.setRole(RoleType.CUSTOMER); // Vai trò mặc định là Customer
        return account;
    }
}
