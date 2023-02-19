package com.hike.service.impl;

import com.hike.models.UserEntity;
import com.hike.repository.UserRepository;
import com.hike.service.ImageService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {

    private final UserRepository userRepository;

    public ImageServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public void saveUserPhoto(Long id, MultipartFile file) {
        try {
            Optional<UserEntity> user = userRepository.findById(id);
            byte[] byteObjects = file.getBytes();
            user.get().setPozaProfil(byteObjects);

            userRepository.save(user.get());
        } catch (IOException e) {
            System.out.println("Couldn't set image: " + e.getMessage());
        }
    }
}
