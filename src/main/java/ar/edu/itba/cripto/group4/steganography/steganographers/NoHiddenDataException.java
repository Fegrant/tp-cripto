package ar.edu.itba.cripto.group4.steganography.steganographers;

public class NoHiddenDataException extends RuntimeException {
    public NoHiddenDataException() {
        super("The file does not contain valid hidden data.");
    }
}
