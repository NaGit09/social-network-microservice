package org.example.uploadservice.controller;

import org.example.uploadservice.entity.Image;
import org.example.uploadservice.service.CloudinaryService;
import org.example.uploadservice.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/upload")

public class CloudinaryController {
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private ImageService imageService;

    @GetMapping("/All")
    public ResponseEntity<?> getAll() {
        List<Image> list = imageService.list();
        return ResponseEntity.ok(list);
    }

    @PostMapping("/uploadFile")
    @ResponseBody
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        // Check if the file is empty
        if (multipartFile.isEmpty()) {
            return ResponseEntity.badRequest().body("No file uploaded");
        }

        // Attempt to read the image
        BufferedImage bi = ImageIO.read(multipartFile.getInputStream());
        if (bi == null) {
            return ResponseEntity.badRequest().body("Invalid image file");
        }

        // Upload to Cloudinary
        Map result = cloudinaryService.upload(multipartFile);

        // Create the image object
        Image image = new Image();
        image.setName(result.get("original_filename").toString());
        image.setUrl(result.get("url").toString());
        image.setImageId(result.get("public_id").toString());

        // Save the image information in the database
        imageService.saveImage(image);

        // Return the saved image details
        return ResponseEntity.ok(image);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        Optional<Image> imageOptional = imageService.getOneImage(id);
        if (imageOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Image image = imageOptional.get();
        String cloudinaryImageId = image.getImageId();
        try {
            cloudinaryService.delete(cloudinaryImageId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        imageService.deleteById(id);
        return ResponseEntity.ok(image);
    }

}
