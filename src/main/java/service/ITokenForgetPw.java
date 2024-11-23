package service;

import dto.TokenForgetPasswordDTO;

public interface ITokenForgetPw {
    boolean insertToken(TokenForgetPasswordDTO token);
    TokenForgetPasswordDTO getToken(String token);
    boolean updateTokenPassword(TokenForgetPasswordDTO token);

}
