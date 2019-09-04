package com.telerik.carpooling.controllers;

import com.telerik.carpooling.models.Image;
import com.telerik.carpooling.services.services.contracts.ImageService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(maxAge = 3600)
@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("images")
public class ImageController {

    private final ImageService imageService;

    @PostMapping()
    public ResponseEntity<Void> uploadUserImage(@RequestParam("upfile") final MultipartFile file,
                                                final Authentication authentication) {

        imageService.storeUserImage(file, authentication.getName());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/car")
    public ResponseEntity<Void> uploadCarImage(@RequestParam("upfile") final MultipartFile file,
                                               final Authentication authentication) {

        imageService.storeCarImage(file, authentication.getName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}")
    public ResponseEntity downloadUserImage(@PathVariable final String username) {

        return ResponseEntity.ok().body(createImageModelInResponseEntity(imageService.getUserImage(username)));
    }

    @GetMapping("/car/{username}")
    public ResponseEntity downloadCarImage(@PathVariable final String username) throws NotFoundException {

        return ResponseEntity.ok().body(createImageModelInResponseEntity(imageService.getCarImage(username)));
    }

    @DeleteMapping(value = "/{username}")
    public ResponseEntity<Void> deleteUserImage(@PathVariable final String username, final Authentication authentication) {

        imageService.deleteUserImage(username,authentication);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/car/{username}")
    public ResponseEntity<Void> deleteCarImage(@PathVariable final String username, final Authentication authentication) throws NotFoundException {

        imageService.deleteCarImage(username,authentication);
        return ResponseEntity.ok().build();
    }

    private ResponseEntity<byte[]> createImageModelInResponseEntity(Image dbFile) {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.valueOf(dbFile.getContentType()));
        header.setContentLength(dbFile.getData().length);
        header.set("Content-Disposition", "attachment; filename=" + dbFile.getFileName());
        return new ResponseEntity<>(dbFile.getData(), header, HttpStatus.OK);
    }
}
