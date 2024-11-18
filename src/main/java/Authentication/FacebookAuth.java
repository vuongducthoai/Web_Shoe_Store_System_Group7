package Authentication;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import constant.IConstant;
import dto.AccountDTO;
import dto.UserDTO;
import enums.AuthProvider;
import enums.RoleType;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.HttpResponseException;

import java.io.IOException;

public class FacebookAuth {
    public static String getToken(String code) throws IOException {

        try {
            String response = Request.Post(IConstant.FACEBOOK_LINK_GET_TOKEN)
                    .bodyForm(
                            Form.form()
                                    .add("client_id", IConstant.FACEBOOK_CLIENT_ID)
                                    .add("client_secret", IConstant.FACEBOOK_CLIENT_SECRET)
                                    .add("redirect_uri", IConstant.FACEBOOK_REDIRECT_URI)
                                    .add("code", code)
                                    .build()
                    )
                    .execute()
                    .returnContent()
                    .asString();
            System.out.println("Request URL: " + IConstant.FACEBOOK_LINK_GET_TOKEN);


            JsonObject jobj = new Gson().fromJson(response, JsonObject.class);

            if (jobj.has("access_token")) {
                return jobj.get("access_token").getAsString();
            } else {
                throw new IOException("Access token not found in the response.");
            }

        } catch (HttpResponseException e) {
            throw new IOException("Failed to retrieve token, response code: " + e.getStatusCode(), e);
        }
    }

    public static AccountDTO getUserInforFromFacebook(String accessToken) throws IOException {
        //URL lấy thông tin người dùng từ FaceBook API
        String facebookApiUrl = IConstant.FACEBOOK_LINK_GET_USER_INFO;

        //Gui yeu cau Get de lay thong tin nguoi dung tu Facebook API
        String response = Request.Get(facebookApiUrl)
                .addHeader("Authorization", "Bearer " + accessToken)
                .execute()
                .returnContent()
                .asString();


        //Parse JSON response từ Facebook API
        JsonObject jobj = new Gson().fromJson(response, JsonObject.class);
        JsonElement emailElement = jobj.get("email");
        JsonElement nameElement = jobj.get("name");
        JsonElement idElement = jobj.get("id");
        AccountDTO accountDTO = new AccountDTO();

        if (emailElement != null && !emailElement.isJsonNull()) {
            accountDTO.setEmail(emailElement.getAsString());
        } else {
            System.out.println("Email not found in the Facebook response.");
        }

        if (nameElement != null && !nameElement.isJsonNull()) {
            String fullName = nameElement.getAsString();
            if (accountDTO.getUser() == null) {
                accountDTO.setUser(new UserDTO());
            }
            accountDTO.getUser().setFullName(fullName);
        } else {
            System.out.println("Full name not found in the Facebook response.");
        }

        if (idElement != null && !idElement.isJsonNull()) {
            accountDTO.setProviderID(idElement.getAsString());
        } else {
            System.out.println("Provider ID not found in the Facebook response.");
        }
        accountDTO.setAuthProvider(AuthProvider.FACEBOOK);
        accountDTO.setRole(RoleType.CUSTOMER);
        return accountDTO;
    }
}
