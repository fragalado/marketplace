package com.api.marketplace.services;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.api.marketplace.daos.User;
import com.api.marketplace.dtos.CourseResponseDTO;
import com.api.marketplace.dtos.UserDTO;
import com.api.marketplace.dtos.UserUpdateRequestDTO;
import com.api.marketplace.repositories.CourseRepository;
import com.api.marketplace.repositories.PurchaseRepository;
import com.api.marketplace.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PurchaseRepository courseRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, PurchaseRepository courseRepository,
            PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDTO updateUser(UserUpdateRequestDTO dto, User user) {
        // Modificamos los datos del usuario con los nuevos datos
        user.setUsername(dto.getUsername());
        user.setHashedPassword(passwordEncoder.encode(dto.getPassword()));

        // Guardamos el usuario actualizado y pasamos el usuario a DTO y devolvemos
        return modelMapper.map(userRepository.save(user), UserDTO.class);
    }

    @Override
    public boolean deleteUser(int userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }

        userRepository.deleteById(userId);

        return !userRepository.existsById(userId);
    }

    @Override
    public Page<CourseResponseDTO> getPurchasedCourses(int page, int size, User userAuthenticated) {
        Pageable pageable = PageRequest.of(page, size);
        return courseRepository.findByUser(userAuthenticated, pageable)
                .map(purchase -> modelMapper.map(purchase.getCourse(), CourseResponseDTO.class));
    }

}
