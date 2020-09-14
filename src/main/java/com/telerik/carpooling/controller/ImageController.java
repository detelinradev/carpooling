package com.telerik.carpooling.controller;

import com.telerik.carpooling.exception.MyNotFoundException;
import com.telerik.carpooling.model.Image;
import com.telerik.carpooling.service.service.contract.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@CrossOrigin(maxAge = 3600)
@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("images")
@Validated
public class ImageController {

    private final ImageService imageService;

    @PostMapping()
    public ResponseEntity<Void> uploadUserImage(@RequestParam("upFile") @NotNull final MultipartFile file){

        String loggedUserName = SecurityContextHolder.getContext().getAuthentication().getName();

        imageService.storeUserImage(file, loggedUserName);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/car")
    public ResponseEntity<Void> uploadCarImage(@RequestParam("upFile") @NotNull final MultipartFile file) {

        String loggedUserName = SecurityContextHolder.getContext().getAuthentication().getName();

        imageService.storeCarImage(file, loggedUserName);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}")
    public ResponseEntity<byte[]> downloadUserImage(@PathVariable @NotNull final String username) throws MyNotFoundException {

        return createImageModelInResponseEntity(imageService.getUserImage(username));
    }

    @GetMapping("/car/{username}")
    public ResponseEntity<byte[]> downloadCarImage(@PathVariable @NotNull final String username) throws MyNotFoundException {

        return createImageModelInResponseEntity(imageService.getCarImage(username));
    }

    @DeleteMapping(value = "/{username}")
    public ResponseEntity<Void> deleteUserImage(@PathVariable @NotNull final String username) {

        String loggedUserName = SecurityContextHolder.getContext().getAuthentication().getName();

        imageService.deleteUserImage(username, loggedUserName);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/car/{username}")
    public ResponseEntity<Void> deleteCarImage(@PathVariable @NotNull final String username) {

        String loggedUserName = SecurityContextHolder.getContext().getAuthentication().getName();

        imageService.deleteCarImage(username, loggedUserName);
        return ResponseEntity.ok().build();
    }

    private ResponseEntity<byte[]> createImageModelInResponseEntity(Image dbFile) {

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.valueOf(dbFile.getContentType()));
        header.setContentLength(dbFile.getData().length);
        header.set("Content-Disposition", "form-data; filename=" + dbFile.getFileName());
        return new ResponseEntity<>(dbFile.getData(), header, HttpStatus.OK);
    }
}
