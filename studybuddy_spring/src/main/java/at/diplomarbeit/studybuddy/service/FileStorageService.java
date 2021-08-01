package at.diplomarbeit.studybuddy.service;

import at.diplomarbeit.studybuddy.data.entity.Bild;
import at.diplomarbeit.studybuddy.data.entity.User;
import at.diplomarbeit.studybuddy.data.repository.BildRepository;
import at.diplomarbeit.studybuddy.data.repository.UserRepository;
import at.diplomarbeit.studybuddy.exception.FileStorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.time.LocalDate;

@Service
public class FileStorageService {
    private final Path fileStorageLocation;
    private final BildRepository bildRepository;
    private final UserRepository userRepository;

    @Autowired
    public FileStorageService(@Value("${spring.servlet.multipart.location}") String pathName, BildRepository bildRepository, UserRepository userRepository) {
        this.fileStorageLocation = Path.of(pathName);
        this.bildRepository = bildRepository;
        this.userRepository = userRepository;
    }

    public String saveFile(MultipartFile file, String email) {
        User user = userRepository.findUserByEmail(email).orElseThrow();

        try {
            if(file.isEmpty()) {
                throw new FileStorageException("Failed to store empty file.");
            }

            Path destinationFile = this.fileStorageLocation.resolve(
                    Paths.get(user.getUserId() + "_" + file.getOriginalFilename()))
                    .normalize()
                    .toAbsolutePath();

            if(!destinationFile.getParent().equals(this.fileStorageLocation.toAbsolutePath())) {
                throw new FileStorageException("Cannot store file outside current directory");
            }

            try(InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
                String filename = destinationFile.getFileName().toString();

                if(bildRepository.existsByUrl(filename)) {
                    throw new FileStorageException("File exists in DB already");
                } else {
                    bildRepository.save(new Bild(Date.valueOf(
                            LocalDate.now()),
                            filename,
                            user));
                }
            }

            if(!(user.getProfilbild() == null)) {
                deleteFile(user.getProfilbild());
            }

            user.setProfilbild(bildRepository.findFirstByUser(user).orElseThrow());
            userRepository.save(user);

            return destinationFile.getFileName().toString();

        } catch (IOException e) {
            throw new FileStorageException("Failed to store file", e);
        }
    }

    public void deleteFile(Bild bild) {
        File file = new File(fileStorageLocation + "\\" + bild.getUrl());

        if(file.exists()) {
            if(!file.delete()) {
                throw new FileStorageException("Old File could not be deleted.");
            }
        }

        bildRepository.delete(bild);
    }

    public void deleteFile(Long id) {
        Bild bild = bildRepository.findById(id).orElseThrow();
        File file = new File(fileStorageLocation + "\\" + bild.getUrl());

        if(file.exists()) {
            if(!file.delete()) {
                throw new FileStorageException("Old File could not be deleted.");
            }
        }

        bildRepository.delete(bild);
    }
}
