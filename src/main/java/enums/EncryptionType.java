package enums;

public enum EncryptionType {
    AES128,
    AES192,
    AES256,
    DES;

    public static EncryptionType getEncryptionType(String encryptionType) {
        if (encryptionType == null) {
            throw new IllegalArgumentException("Encryption type cannot be null.");
        }

        return switch (encryptionType.toLowerCase()) {
            case "aes128" -> EncryptionType.AES128;
            case "aes192" -> EncryptionType.AES192;
            case "aes256" -> EncryptionType.AES256;
            case "des" -> EncryptionType.DES;
            default -> throw new IllegalArgumentException("Invalid encryption type: " + encryptionType);
        };
    }
}
