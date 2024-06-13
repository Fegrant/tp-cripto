package ar.edu.itba.cripto.group4.esteganography.estaganographers;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;
import java.util.stream.Stream;

public class EncrypedEsteg<T extends Esteganographer> implements Esteganographer{

    //TODO le puse el tipo Function por poner algo, hay que mirar como se va a usar y ahi elegir
    Function<Stream<Byte>, Stream<Byte>> enc;
    T esteganographer;

    public EncrypedEsteg(Function<Stream<Byte>, Stream<Byte>> enc, T esteganographer) {
        this.enc = enc;
        this.esteganographer = esteganographer;
    }

    @Override
    public Stream<Byte> hide(Stream<Byte> image, Stream<Byte> data, EsteganographerMethod method){
        return esteganographer.hide(image, encrypt(data), method);
    }

    @Override
    public Stream<Byte> unhide(Stream<Byte> image, EsteganographerMethod method){
        return decrypt(esteganographer.unhide(image, method));
    }

    @Override
    public Boolean analyze(InputStream file) throws IOException {
        return esteganographer.analyze(file);
    }

    private Stream<Byte> encrypt(Stream<Byte> data) {
        return Stream.empty();
    }

    private Stream<Byte> decrypt(Stream<Byte> data) {
        return Stream.empty();
    }
}
