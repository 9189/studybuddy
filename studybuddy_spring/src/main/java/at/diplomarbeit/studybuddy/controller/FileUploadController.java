package at.diplomarbeit.studybuddy.controller;

import at.diplomarbeit.studybuddy.service.FileStorageService;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path="/api/images")
public class FileUploadController {
    private final FileStorageService fileStorageService;

    @Autowired
    public FileUploadController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping
    public void handleFileUpload(@RequestPart("file") @Valid @NotNull @NotBlank MultipartFile file, @RequestPart("email") String email) {
        fileStorageService.saveFile(file, email);
    }

    @DeleteMapping
    public void deleteFile(Long id) {
        fileStorageService.deleteFile(id);
    }
}
