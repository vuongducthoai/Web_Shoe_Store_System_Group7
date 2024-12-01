package constant;

public class IConstant {
    /*
     GOOGLE
    */
    public static final String GOOGLE_CLIENT_ID = "347868096033-jmjks5edljqsd2aetc3bnmdpdl7fvrak.apps.googleusercontent.com";
    public static final String GOOGLE_CLIENT_SECRET = "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";
    public static final String GOOGLE_REDIRECT_URI = "http://localhost:8080/login";
    public static final String GOOGLE_GRANT_TYPE = "authorization_code";
    public static final String GOOGLE_LINK_GET_TOKEN  = "https://accounts.google.com/o/oauth2/token";
    public static final String GOOGLE_LINK_GET_USER_INFO ="https://www.googleapis.com/oauth2/v3/userinfo";


    /*
        FACEBOOK
     */
    public static final String FACEBOOK_CLIENT_ID = "958382746118607";
    public static final String FACEBOOK_CLIENT_SECRET = "bb552c8aa43eabf0f911c7a1c2702792";
    public static final String FACEBOOK_REDIRECT_URI = "http://localhost:8080/loginFacebook";
    public static final String FACEBOOK_LINK_GET_TOKEN = "https://graph.facebook.com/v21.0/oauth/access_token";
    public static final String FACEBOOK_LINK_GET_USER_INFO = "https://graph.facebook.com/me?fields=id,name,email,picture";

}
