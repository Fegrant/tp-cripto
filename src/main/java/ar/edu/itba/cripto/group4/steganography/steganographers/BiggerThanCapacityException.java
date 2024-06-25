package ar.edu.itba.cripto.group4.steganography.steganographers;

public class BiggerThanCapacityException extends RuntimeException {
    public BiggerThanCapacityException() {
        super("Data is too big to be embedded in the image");
    }
}
