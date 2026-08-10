package in.tech_camp.furima_a.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StorageService {

    private final Path rootLocation = Paths.get("uploads");

    public String storeFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return "default.png"; 
        }
        String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        if (!Files.exists(rootLocation)) {
            Files.createDirectories(rootLocation);
        }

        Files.copy(file.getInputStream(), this.rootLocation.resolve(filename));

        return filename;
    }
}