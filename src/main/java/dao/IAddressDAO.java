package dao;

import dto.AddressDTO;

public interface IAddressDAO {
    AddressDTO getAddressByID(int idUser);
}
