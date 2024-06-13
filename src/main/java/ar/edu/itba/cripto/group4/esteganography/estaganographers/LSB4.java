package ar.edu.itba.cripto.group4.esteganography.estaganographers;

import java.util.stream.Stream;

import static org.jooq.lambda.Seq.zip;

public class LSB4 implements Esteganographer{

    @Override
    public Stream<Byte> hide(Stream<Byte> image, Stream<Byte> data) {
        Stream<Byte> sparse_data = data.flatMap(b -> Stream.of(unnest_bytes(b)));
        return zip(image, sparse_data).map(x ->
                (byte)(((x.v1() >> 4) << 4) | x.v2())
        ).stream();
    }

    @Override
    public Stream<Byte> unhide(Stream<Byte> image) {
        return Stream.empty();
    }

    private Byte[] unnest_bytes(Byte data_byte) {
        Byte[] ret = new Byte[2];
        ret[0] = (byte)((data_byte & 0b11110000) >> 4);
        ret[1] = (byte)(data_byte & 0b00001111);
        return ret;
    }
}
