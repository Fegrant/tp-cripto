package ar.edu.itba.cripto.group4.steganography.enums;

public enum EncryptionMode {
    ECB,
    CBC,
    CFB,
    OFB;

    public static EncryptionMode getEncryptionMode(String encryptionMode) {
        if (encryptionMode == null) {
            throw new IllegalArgumentException("Encryption mode cannot be null.");
        }

        return switch (encryptionMode.toLowerCase()) {
            case "ecb" -> EncryptionMode.ECB;
            case "cbc" -> EncryptionMode.CBC;
            case "cfb" -> EncryptionMode.CFB;
            case "ofb" -> EncryptionMode.OFB;
            default -> throw new IllegalArgumentException("Invalid encryption mode: " + encryptionMode);
        };
    }
}
