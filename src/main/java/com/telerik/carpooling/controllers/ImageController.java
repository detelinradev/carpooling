package com.telerik.carpooling.controllers;

import com.telerik.carpooling.models.Image;
import com.telerik.carpooling.repositories.UserRepository;
import com.telerik.carpooling.services.services.contracts.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
@CrossOrigin(value = {"*"}, exposedHeaders = {"Content-Disposition"})
public class ImageController {


    private final ImageService imageService;
    private final UserRepository userRepository;

    @PostMapping("/uploadUserImage")
    public ResponseEntity<Void> uploadUserImage(@RequestParam("file") final MultipartFile file,
                                                final Authentication authentication) {
        imageService.storeUserImage(file, userRepository.findFirstByUsername(authentication.getName()));
        URI fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUri();

        return ResponseEntity.created(fileDownloadUri).build();
    }

    @PutMapping("/uploadCarImage")
    public ResponseEntity<Void> uploadCarImage(@RequestParam("file") final MultipartFile file,
                                           final Authentication authentication) {
        imageService.storeCarImage(file, userRepository.findFirstByUsername(authentication.getName()));
        URI fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUri();

        return ResponseEntity.created(fileDownloadUri).build();
    }


    @GetMapping("/downloadUserImage")
    public ResponseEntity<byte[]> downloadUserImage(final Authentication authentication) {
        Image dbFile = imageService.getImage(userRepository.findFirstByUsername(
                authentication.getName()).getUserImage().getId());
        return createImageModelInResponseEntity(dbFile);
    }

    @GetMapping("/downloadCarImage")
    public ResponseEntity<byte[]> downloadCarImage(Authentication authentication) {
        Image dbFile = imageService.getImage(userRepository.findFirstByUsername(
                authentication.getName()).getCar().getCarImage().getId());
        return createImageModelInResponseEntity(dbFile);
    }


    private ResponseEntity<byte[]> createImageModelInResponseEntity(Image dbFile) {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.valueOf(dbFile.getContentType()));
        header.setContentLength(dbFile.getData().length);
        header.set("Content-Disposition", "attachment; filename=" + dbFile.getFileName());
        return new ResponseEntity<>(dbFile.getData(), header, HttpStatus.OK);
    }
}
