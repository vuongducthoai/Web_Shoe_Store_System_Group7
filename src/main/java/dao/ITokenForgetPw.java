package dao;

import entity.TokenForgetPassword;

public interface ITokenForgetPw {
     boolean insertToken(TokenForgetPassword token);
     TokenForgetPassword getTokenPassword(String token);
     boolean updateTokenPassword(TokenForgetPassword token);
}
