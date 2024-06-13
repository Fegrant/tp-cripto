package ar.edu.itba.cripto.group4.esteganography.enums;

public enum StegType {
    LSB1,
    LSB4,
    LSBI;

    public static StegType getStegType(String stegType) {
        if (stegType == null) {
            throw new IllegalArgumentException("Steg type cannot be null.");
        }

        return switch (stegType.toLowerCase()) {
            case "lsb1" -> StegType.LSB1;
            case "lsb4" -> StegType.LSB4;
            case "lsbi" -> StegType.LSBI;
            default -> throw new IllegalArgumentException("Invalid steg type: " + stegType);
        };
    }
}
