package service;

import dto.AddressDTO;

public interface IAddressService {
    AddressDTO getAddressByID(int idUser);
    boolean updateAddress(AddressDTO addressDTO);
}
