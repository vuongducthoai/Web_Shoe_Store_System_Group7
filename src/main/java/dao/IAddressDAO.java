package dao;

import dto.AddressDTO;
import entity.Address;

public interface IAddressDAO {
    AddressDTO getAddressByID(int idUser);
    boolean updateAddress(Address addressEntity);
}
