package service.Impl;

import dao.IAddressDAO;
import dao.Impl.AddressDaoImpl;
import dto.AddressDTO;
import service.IAddressService;

public class AddressServiceImpl implements IAddressService {
    IAddressDAO iAddressDAO = new AddressDaoImpl();
    public AddressDTO getAddressByID(int idUser){
        return iAddressDAO.getAddressByID(idUser);
    }
}
