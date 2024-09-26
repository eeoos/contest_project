package core.contest5.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FileService {
    @Value("${file.poster.upload-dir}")
    private String posterUploadDir;

    @Value("${file.member.upload-dir}")
    private String thumbnailUploadDir;

    @Value("${file.attachment.upload-dir}")
    private String attachmentUploadDir;

    @Value("${file.award.upload-dir}")
    private String awardUploadDir;

    public List<String> saveFiles(List<MultipartFile> files, String type) throws IOException {
        List<String> savedFileNames = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (file != null && !file.isEmpty()) {
                    savedFileNames.add(saveFile(file, type));
                }
            }
        }
        return savedFileNames;
    }

    public String saveFile(MultipartFile file, String type) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path uploadPath = getUploadPath(type);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);
        return fileName;
    }

    public void deleteFiles(List<String> fileNames, String type) throws IOException {
        for (String fileName : fileNames) {
            deleteFile(fileName, type);
        }
    }

    public void deleteFile(String fileName, String type) throws IOException {
        if (fileName != null && !fileName.isEmpty()) {
            Path uploadPath = getUploadPath(type);
            Path filePath = uploadPath.resolve(fileName);
            Files.deleteIfExists(filePath);
        }
    }

    private Path getUploadPath(String type) {
        switch (type) {
            case "poster":
                return Paths.get(posterUploadDir);
            case "member":
                return Paths.get(thumbnailUploadDir);
            case "attachment":
                return Paths.get(attachmentUploadDir);
            case "award":
                return Paths.get(awardUploadDir);
            default:
                throw new IllegalArgumentException("Invalid file type: " + type);
        }
    }
}
