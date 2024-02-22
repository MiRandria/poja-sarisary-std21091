package hei.school.sarisary.controller;

import hei.school.sarisary.service.ImageProcessingService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class ImageProcessingController {
    private final ImageProcessingService imageProcessingService;

    @PutMapping("/black-and-white/{id}")
    public ResponseEntity<Void> processAndUploadImage(@RequestBody byte[] imageData, @PathVariable String id) {
        imageProcessingService.processAndUploadImage(imageData, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/black-and-white/{id}")
    public ResponseEntity<Object> getImageUrls(@PathVariable String id) {
        String urls = imageProcessingService.getImageUrls(id);
        if (urls != null) {
            String[] urlArray = urls.split(",");
            String originalUrl = urlArray[0];
            String transformedUrl = urlArray[1];
            return ResponseEntity.ok().body(
                    new Object() {
                        public String original_url = originalUrl;
                        public String transformed_url = transformedUrl;
                    }
            );
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
