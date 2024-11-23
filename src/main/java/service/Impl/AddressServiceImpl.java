package service.Impl;

import dao.IAddressDAO;
import dao.Impl.AddressDaoImpl;
import dto.AddressDTO;
import service.IAddressService;

public class AddressServiceImpl implements IAddressService {
    IAddressDAO iAddressDAO = new AddressDaoImpl();
    @Override
    public AddressDTO getAddressByID(int idUser){

        return iAddressDAO.getAddressByID(idUser);
    }
    @Override
    public boolean updateAddress(AddressDTO addressDTO) {
//        AddressEntity addressEntity = new AddressEntity();
//        addressEntity.setUserID(addressDTO.getUserID());
//        addressEntity.setHouseNumber(addressDTO.getHouseNumber());
//        addressEntity.setStreetName(addressDTO.getStreetName());
//        addressEntity.setDistrict(addressDTO.getDistrict());
//        addressEntity.setCity(addressDTO.getCity());
//        return addressDAO.updateAddress(addressEntity);
        return false;
    }
}
