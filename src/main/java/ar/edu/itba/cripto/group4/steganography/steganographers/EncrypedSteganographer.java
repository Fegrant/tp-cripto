package ar.edu.itba.cripto.group4.steganography.steganographers;

import ar.edu.itba.cripto.group4.steganography.io.Metadata;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;
import java.util.stream.Stream;

public class EncrypedSteganographer<T extends Steganographer> implements Steganographer {

    //TODO le puse el tipo Function por poner algo, hay que mirar como se va a usar y ahi elegir
    Function<Stream<Byte>, Stream<Byte>> enc;
    T steganographer;

    public EncrypedSteganographer(Function<Stream<Byte>, Stream<Byte>> enc, T steganographer) {
        this.enc = enc;
        this.steganographer = steganographer;
    }

    @Override
    public Stream<Byte> hide(Stream<Byte> image, Stream<Byte> data, SteganographerMethod method){
        return steganographer.hide(image, encrypt(data), method);
    }

    @Override
    public Stream<Byte> unhide(Stream<Byte> image, Metadata meta, SteganographerMethod method){
        return decrypt(steganographer.unhide(image, meta, method));
    }

    @Override
    public boolean analyze(InputStream file) throws IOException {
        return steganographer.analyze(file);
    }

    private Stream<Byte> encrypt(Stream<Byte> data) {
        return Stream.empty();
    }

    private Stream<Byte> decrypt(Stream<Byte> data) {
        return Stream.empty();
    }
}
