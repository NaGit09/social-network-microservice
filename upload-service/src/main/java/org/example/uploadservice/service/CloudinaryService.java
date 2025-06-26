package org.example.uploadservice.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;


@Service
@Data
@AllArgsConstructor
public class CloudinaryService {
@Autowired
    Cloudinary cloudinary;

    public Map upload(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        File file = convert(multipartFile);

        try {
            Map result = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());

            if (result == null || result.isEmpty()) {
                throw new IOException("Cloudinary upload failed");
            }

            return result;
        } catch (Exception e) {
            throw new IOException("Error during file upload: " + e.getMessage(), e);
        } finally {
            // Attempt to delete the file after the upload process, even in case of failure.
            if (!Files.deleteIfExists(file.toPath())) {
                throw new IOException("Failed to delete temporary file: " + file.getName());
            }
        }
    }

    public Map delete(String id) throws IOException {
        try {
            return cloudinary.uploader().destroy(id, ObjectUtils.emptyMap());
        } catch (Exception e) {
            throw new IOException("Error during file deletion from Cloudinary: " + e.getMessage(), e);
        }
    }

    private File convert(MultipartFile multipartFile) throws IOException {
        File file = File.createTempFile("tempfile", ".tmp");

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        }

        return file;
    }

}
