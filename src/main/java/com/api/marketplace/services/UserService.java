package com.api.marketplace.services;

import com.api.marketplace.daos.User;
import com.api.marketplace.dtos.UserDTO;
import com.api.marketplace.dtos.UserUpdateRequestDTO;

public interface UserService {

    /**
     * Metodo que modifica la información de un usuario
     * 
     * @param dto  UserUpdateRequestDTO objeto que contiene la nueva informacion
     * @param user User objeto que contiene la informacion del usuario a modificar
     * @return UserDTO objeto que contiene la nueva informacion
     */
    UserDTO updateUser(UserUpdateRequestDTO dto, User user);

    /**
     * Metodo que elimina un usuario
     * 
     * @param userId id del usuario a eliminar
     * @return boolean true si se ha eliminado correctamente, false en caso
     *         contrario
     */
    boolean deleteUser(int userId);
}
