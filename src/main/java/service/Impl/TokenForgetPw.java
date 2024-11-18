package service.Impl;

import dto.TokenForgetPasswordDTO;
import entity.Account;
import entity.TokenForgetPassword;
import service.ITokenForgetPw;

public class TokenForgetPw implements ITokenForgetPw {
     dao.ITokenForgetPw iTokenForgetPw = new dao.Impl.TokenForgetPw();
    @Override
    public boolean insertToken(TokenForgetPasswordDTO token) {
        TokenForgetPassword tokenForgetPassword = new TokenForgetPassword();
        tokenForgetPassword.setToken(token.getToken());
        tokenForgetPassword.setUsed(token.isUsed());
        tokenForgetPassword.setExpireTime(token.getExpireTime());

        Account accout = new Account();
        accout.setAccountID(token.getAccountDTO().getAccountID());
        tokenForgetPassword.setAccount(accout );
        return iTokenForgetPw.insertToken(tokenForgetPassword);
    }

    @Override
    public TokenForgetPasswordDTO getToken(String token) {
        TokenForgetPassword tokenForgetPassword = iTokenForgetPw.getTokenPassword(token);
        TokenForgetPasswordDTO tokenForgetPasswordDTO = new TokenForgetPasswordDTO();
        tokenForgetPasswordDTO.setToken(tokenForgetPassword.getToken());
        tokenForgetPassword.setExpireTime(tokenForgetPassword.getExpireTime());
        tokenForgetPasswordDTO.setUsed(tokenForgetPassword.isUsed());
        tokenForgetPassword.setAccount(tokenForgetPassword.getAccount());
        return tokenForgetPasswordDTO;
    }
}
