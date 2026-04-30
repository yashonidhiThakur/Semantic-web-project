package org.example.partner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;

@Service
public class PartnerStorageService {

    private final Path uploadDir;

    public PartnerStorageService(@Value("${xcommerce.partner.upload-dir:./uploads/partners}") String uploadDir) {
        this.uploadDir = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    public Path ensureUploadDir() throws IOException {
        Files.createDirectories(uploadDir);
        return uploadDir;
    }

    public Path savePartnerFile(String partnerName, String type, MultipartFile file) throws IOException {
        ensureUploadDir();
        String original = file.getOriginalFilename();
        String ext = original != null && original.contains(".") ? original.substring(original.lastIndexOf('.')) : ".rdf";
        String safePartner = partnerName.replaceAll("[^a-zA-Z0-9_-]", "_");
        String safeType = type.replaceAll("[^a-zA-Z0-9_-]", "_");
        String filename = safePartner + "_" + safeType + "_" + Instant.now().toEpochMilli() + ext;
        Path target = uploadDir.resolve(filename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        return target;
    }
}
