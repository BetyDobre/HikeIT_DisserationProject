package com.hike.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    void saveUserPhoto(Long id, MultipartFile file);
}
