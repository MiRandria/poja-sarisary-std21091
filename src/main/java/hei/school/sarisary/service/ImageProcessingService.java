package hei.school.sarisary.service;

import hei.school.sarisary.file.BucketComponent;
import lombok.AllArgsConstructor;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class ImageProcessingService {
    private final BucketComponent bucketComponent;
    private final Map<String, String> imageMap = new HashMap<>();

    public void processAndUploadImage(byte[] imageData, String id) {
        try {
            FImage image = ImageUtilities.readF(new ByteArrayInputStream(imageData));

            image.processInplace(pixels -> {
                for (int y = 0; y < pixels.height; y++) {
                    for (int x = 0; x < pixels.width; x++) {
                        float pixel = pixels.pixels[y][x];
                        float grayscale = (0.3f * pixel + 0.59f * pixel + 0.11f * pixel) / 255f;
                        pixels.pixels[y][x] = grayscale;
                    }
                }
            });

            File processedFile = File.createTempFile("processed_image", ".jpg");
            ImageUtilities.write(image, processedFile);

            String fileName = id + ".jpg";
            bucketComponent.upload(processedFile, fileName);

            String originalUrl = "https://original.url/" + fileName;
            String transformedUrl = "https://transformed.url/" + fileName;
            imageMap.put(id, originalUrl + "," + transformedUrl);

            processedFile.delete();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getImageUrls(String id) {
        return imageMap.get(id);
    }
}
