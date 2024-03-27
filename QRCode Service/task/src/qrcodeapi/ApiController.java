package qrcodeapi;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;

@RestController
@RequestMapping("api")
public class ApiController {

    @GetMapping("/health")
    public ResponseEntity<?> getHealth() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/qrcode")
    public ResponseEntity<?> getQrcode(@RequestParam String contents,
                                       @RequestParam(defaultValue = "L", required = false) String correction,
                                       @RequestParam(defaultValue = "250", required = false) int size,
                                       @RequestParam(defaultValue = "png", required = false) String type) {

        if (contents.isEmpty() || contents.isBlank()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Contents cannot be null or blank");
            return ResponseEntity.badRequest().body(error);
        }

        if (size < 150 || size > 350) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Image size must be between 150 and 350 pixels");
            return ResponseEntity.badRequest().body(error);
        }

        if (!correction.equalsIgnoreCase("L")
                && !correction.equalsIgnoreCase("M")
                && !correction.equalsIgnoreCase("Q")
                && !correction.equalsIgnoreCase("H")) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Permitted error correction levels are L, M, Q, H");
            return ResponseEntity.badRequest().body(error);
        }

        ImageFormat imageFormat;

        switch(type.toLowerCase()) {
            case "jpeg":
                imageFormat = new ImageFormat("jpeg");
                break;
            case "png":
                imageFormat = new ImageFormat("png");
                break;
            case "gif":
                imageFormat = new ImageFormat("gif");
                break;
            default:
                HashMap<String, String> error = new HashMap<>();
                error.put("error", "Only png, jpeg and gif image types are supported");
                return ResponseEntity.badRequest().body(error);
        }

        ErrorCorrectionLevel errorCorrectionLevel = switch (correction.toUpperCase()) {
            case "M" -> ErrorCorrectionLevel.M;
            case "Q" -> ErrorCorrectionLevel.Q;
            case "H" -> ErrorCorrectionLevel.H;
            default -> ErrorCorrectionLevel.L;
        };

        QRGenerator qrGenerator = new QRGenerator(contents, size, imageFormat, errorCorrectionLevel);

        return ResponseEntity
                .ok()
                .contentType(imageFormat.getMediaType())
                .body(qrGenerator.getQRCodeInBytes());
    }
}
