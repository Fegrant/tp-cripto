package ar.edu.itba.cripto.group4.steganography.enums;

public enum EncryptionType {
    AES128("AES", 128),
    AES192("AES", 192),
    AES256("AES", 256),
    DES("DESede", 168);

    private final String algorithm;
    private final int keySize;

    EncryptionType(String algorithm, int keySize) {
        this.algorithm = algorithm;
        this.keySize = keySize;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public int getKeySize() {
        return keySize;
    }

    public static EncryptionType fromString(String text) {
        for (EncryptionType type : EncryptionType.values()) {
            if (type.name().equalsIgnoreCase(text)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No enum constant for text: " + text);
    }
}