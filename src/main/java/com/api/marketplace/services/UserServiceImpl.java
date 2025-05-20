package com.api.marketplace.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.api.marketplace.daos.User;
import com.api.marketplace.dtos.CourseResponseLiteDTO;
import com.api.marketplace.dtos.UserDTO;
import com.api.marketplace.dtos.UserUpdateRequestDTO;
import com.api.marketplace.repositories.PurchaseRepository;
import com.api.marketplace.repositories.UserRepository;

/**
 * Implementación del servicio de usuarios.
 * Contiene la lógica para actualizar, eliminar y obtener información
 * relacionada con los usuarios y sus compras.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PurchaseRepository purchaseRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, PurchaseRepository purchaseRepository,
            PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.purchaseRepository = purchaseRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDTO updateUser(UserUpdateRequestDTO dto, User user) {
        // Si se intenta cambiar el nombre de usuario, se valida su disponibilidad
        if (dto.getUsername() != null && !dto.getUsername().equals(user.getUsernameReal())) {
            System.out.println("Username actual: " + user.getUsernameReal());
            System.out.println("Username nuevo: " + dto.getUsername());
            if (userRepository.existsByUsername(dto.getUsername())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nombre de usuario ya en uso");
            }
            user.setUsername(dto.getUsername());
        }

        // Se actualizan otros campos si están presentes en el DTO
        if (dto.getFirstName() != null)
            user.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null)
            user.setLastName(dto.getLastName());
        if (dto.getBio() != null)
            user.setBio(dto.getBio());
        if (dto.getProfilePicture() != null)
            user.setProfilePicture(dto.getProfilePicture());

        // Si se proporciona una nueva contraseña, se encripta y se actualiza
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            System.out.println("Se ha cambiado la password");
            user.setHashedPassword(passwordEncoder.encode(dto.getPassword()));
        }

        user.setUpdated_at(LocalDateTime.now());

        return modelMapper.map(userRepository.save(user), UserDTO.class);
    }

    @Override
    public boolean deleteUser(int userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }

        userRepository.deleteById(userId);

        // Verificamos si el usuario se eliminó correctamente
        return !userRepository.existsById(userId);
    }

    // TODO: Si hay campos no usados crear una clase CoursePurchaseDTO
    @Override
    public Page<CourseResponseLiteDTO> getPurchasedCourses(int page, int size, User userAuthenticated) {
        Pageable pageable = PageRequest.of(page, size);
        return purchaseRepository.findByUser(userAuthenticated, pageable)
                .map(purchase -> modelMapper.map(purchase.getCourse(), CourseResponseLiteDTO.class));
    }

    @Override
    public List<UUID> getUuidPurchasedCourses(User userAuthenticated) {
        return purchaseRepository.findByUser(userAuthenticated, null)
                .map(purchase -> purchase.getCourse().getUuid())
                .toList();
    }
}
