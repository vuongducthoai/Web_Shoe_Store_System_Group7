package service.Impl;

import dao.Impl.AccountDaoImpl;
import dto.AccountDTO;
import dto.TokenForgetPasswordDTO;
import entity.Account;
import entity.TokenForgetPassword;
import service.ITokenForgetPw;
import util.PasswordHashingSHA;

import java.security.NoSuchAlgorithmException;

public class TokenForgetPw implements ITokenForgetPw {
    dao.ITokenForgetPw iTokenForgetPw = new dao.Impl.TokenForgetPw();
    dao.IAccountDAO iAccountDAO = new AccountDaoImpl();

    @Override
    public boolean insertToken(TokenForgetPasswordDTO token) {
        TokenForgetPassword tokenForgetPassword = new TokenForgetPassword();
        tokenForgetPassword.setToken(token.getToken());
        tokenForgetPassword.setUsed(token.isUsed());
        tokenForgetPassword.setExpireTime(token.getExpireTime());

        Account accout = new Account();
        accout.setAccountID(token.getAccountDTO().getAccountID());
        tokenForgetPassword.setAccount(accout);
        return iTokenForgetPw.insertToken(tokenForgetPassword);
    }

    @Override
    public TokenForgetPasswordDTO getToken(String token) {
        TokenForgetPassword tokenForgetPassword = iTokenForgetPw.getTokenPassword(token);
        if (tokenForgetPassword != null) {
            TokenForgetPasswordDTO tokenForgetPasswordDTO = new TokenForgetPasswordDTO();
            tokenForgetPasswordDTO.setToken(tokenForgetPassword.getToken());
            tokenForgetPasswordDTO.setExpireTime(tokenForgetPassword.getExpireTime());
            tokenForgetPasswordDTO.setUsed(tokenForgetPassword.isUsed());
            Account account = tokenForgetPassword.getAccount();

            if (account != null) {
                AccountDTO accountDTO = new AccountDTO();
                accountDTO.setAccountID(account.getAccountID());
                accountDTO.setEmail(account.getEmail());
                accountDTO.setPassword(account.getPassword());
                tokenForgetPasswordDTO.setAccountDTO(accountDTO);
            } else {
                tokenForgetPasswordDTO.setAccountDTO(null);
            }
            return tokenForgetPasswordDTO;
        }

        return null;
    }

    @Override
    public boolean updateTokenPassword(TokenForgetPasswordDTO token) {
        TokenForgetPassword tokenForgetPassword = iTokenForgetPw.getTokenPassword(token.getToken());
        tokenForgetPassword.setToken(token.getToken());
        tokenForgetPassword.setUsed(token.isUsed());
        tokenForgetPassword.setExpireTime(token.getExpireTime());

        Account account = new Account();
        String password = token.getAccountDTO().getPassword();
        PasswordHashingSHA passwordHashingSHA = new PasswordHashingSHA();
        String passwordHash = null;
        try {
            passwordHash = passwordHashingSHA.toHexString(passwordHashingSHA.getSHA(password));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        account.setAccountID(token.getAccountDTO().getAccountID());
        account.setPassword(passwordHash);

        Account existingAccount = iAccountDAO.getAccountByID(account.getAccountID());
        if (existingAccount != null) {
            existingAccount.setPassword(account.getPassword());
            tokenForgetPassword.setAccount(existingAccount);
        }
        return iTokenForgetPw.updateTokenPassword(tokenForgetPassword);
    }
}


