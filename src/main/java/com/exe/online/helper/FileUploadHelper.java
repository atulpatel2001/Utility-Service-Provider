package com.exe.online.helper;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FileUploadHelper {
    public static boolean uploadFile(MultipartFile file, String foldePath) {
        boolean f = false;
        try {
            ClassPathResource resource = new ClassPathResource(foldePath);
            Path savePath = resource.getFile().toPath().resolve(file.getOriginalFilename());
            System.out.println(savePath);
            Files.copy(file.getInputStream(), savePath, StandardCopyOption.REPLACE_EXISTING);
            f = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return f;
    }

    public static boolean deleteFile(String folderPath, String fileName) {
        boolean f = false;
        try {
            ClassPathResource resource = new ClassPathResource(folderPath);
            Path file = resource.getFile().toPath().resolve(fileName);
            if (Files.exists(file)) {
                Files.delete(file);
                f = true;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return f;
    }
}
