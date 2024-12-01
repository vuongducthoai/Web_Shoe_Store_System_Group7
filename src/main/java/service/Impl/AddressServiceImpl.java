package service.Impl;

import dao.IAddressDAO;
import dao.Impl.AddressDaoImpl;
import dto.AddressDTO;
import entity.Address;
import service.IAddressService;

public class AddressServiceImpl implements IAddressService {
    IAddressDAO iAddressDAO = new AddressDaoImpl();
    @Override
    public AddressDTO getAddressByID(int idUser){

        return iAddressDAO.getAddressByID(idUser);
    }
    @Override
    public boolean updateAddress(AddressDTO addressDTO) {
        try {
            // Chuyển AddressDTO sang Address entity
            Address address = new Address();
            address.setAddressID(addressDTO.getAddressID());
            address.setHouseNumber(addressDTO.getHouseNumber());
            address.setStreetName(addressDTO.getStreetName());
            address.setDistrict(addressDTO.getDistrict());
            address.setCity(addressDTO.getCity());
            address.setProvince(addressDTO.getProvince());

            // Gọi DAO để cập nhật Address
            return iAddressDAO.updateAddress(address);
        } catch (Exception e) {
            System.out.println("Error in service updateAddress: " + e.getMessage());
            return false;
        }
    }
}
