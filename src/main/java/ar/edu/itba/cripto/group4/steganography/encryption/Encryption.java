package ar.edu.itba.cripto.group4.steganography.encryption;

import java.util.List;

public interface Encryption {
    List<Byte> encrypt(List<Byte> data);

    List<Byte> decrypt(List<Byte> data);
}
