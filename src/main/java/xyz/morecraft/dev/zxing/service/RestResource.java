package xyz.morecraft.dev.zxing.service;

import com.google.zxing.*;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.pdf417.PDF417Reader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController
@RequestMapping("/decode")
public class RestResource {

    @RequestMapping(
            value = "/pdf417",
            method = {RequestMethod.POST, RequestMethod.PUT},
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> scan(@RequestParam("file") final MultipartFile file) throws FormatException, ChecksumException, NotFoundException, IOException {
        final BufferedImage image = ImageIO.read(file.getInputStream());
        final int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
        final BinaryBitmap bitmap = new BinaryBitmap(
                new HybridBinarizer(
                        new RGBLuminanceSource(
                                image.getWidth(),
                                image.getHeight(),
                                pixels
                        )
                )
        );

        final PDF417Reader reader = new PDF417Reader();

        Result result = reader.decode(bitmap);

        return new ResponseEntity<Object>(
                result,
                HttpStatus.OK
        );
    }

}
