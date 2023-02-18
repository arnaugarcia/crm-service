package com.theagilemonkeys.crmservice.service.customer.request;

import lombok.Getter;

@Getter
public enum PhotoFormat {
    PNG("image/png"),
    JPG("image/jpg"),
    JPEG("image/jpeg"),
    SVG("image/svg");

    private final String format;

    PhotoFormat(String contentType) {
        this.format = contentType;
    }

    public static PhotoFormat from(String format) {
        switch (format) {
            case "image/png":
                return PNG;
            case "image/jpg":
                return JPG;
            case "image/jpeg":
                return JPEG;
            case "image/svg":
                return SVG;
            default:
                throw new IllegalStateException();
        }
    }

    public String getExtension() {
        return "." + format.split("/")[1];
    }
}
