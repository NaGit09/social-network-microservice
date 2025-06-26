package org.example.uploadservice.service;

import jakarta.transaction.Transactional;
import org.example.uploadservice.entity.Image;
import org.example.uploadservice.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ImageService {
    @Autowired
    ImageRepository imageRepository;
    public List<Image> list() {
        return imageRepository.findByOrderById();
    }
    public Optional<Image> getOneImage(Integer id) {
        return imageRepository.findById(id);
    }
    public void saveImage(Image image) {
        imageRepository.save(image);
    }
    public void deleteById(Integer id) {
        imageRepository.deleteById(id);
    }
    public boolean existsById(Integer id) {
        return imageRepository.existsById(id);
    }
}
