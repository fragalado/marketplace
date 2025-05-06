package com.api.marketplace.services;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.api.marketplace.daos.User;
import com.api.marketplace.dtos.UserDTO;
import com.api.marketplace.dtos.UserUpdateRequestDTO;
import com.api.marketplace.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDTO updateUser(UserUpdateRequestDTO dto, User user) {
        // Modificamos los datos del usuario con los nuevos datos
        if (dto.getUsername() != null)
            user.setUsername(dto.getUsername());
        if (dto.getPassword() != null)
            user.setHashedPassword(passwordEncoder.encode(dto.getPassword()));

        // Guardamos el usuario actualizado
        User updatedUser = userRepository.save(user);

        // Pasamos el usuario a DTO y devolvemos
        return modelMapper.map(updatedUser, UserDTO.class);
    }

    @Override
    public boolean deleteUser(int userId) {
        // Eliminamos el usuario de la base de datos por el id
        userRepository.deleteById(userId);

        // Comprobamos si el usuario ha sido eliminado
        if (userRepository.existsById(userId)) {
            return false;
        } else {
            return true;
        }
    }

}
