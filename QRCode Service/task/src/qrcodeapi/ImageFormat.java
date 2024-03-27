package qrcodeapi;

import org.springframework.http.MediaType;

public class ImageFormat {

    private final String formatName;

    ImageFormat(String formatName) {
        this.formatName = formatName.toLowerCase();
    }

    public MediaType getMediaType() {
        return switch (formatName) {
            case "jpeg" -> MediaType.IMAGE_JPEG;
            case "gif" -> MediaType.IMAGE_GIF;
            case "png" -> MediaType.IMAGE_PNG;
            default -> MediaType.APPLICATION_JSON;
        };
    }

    public String getFormatName() {
        return formatName;
    }
}