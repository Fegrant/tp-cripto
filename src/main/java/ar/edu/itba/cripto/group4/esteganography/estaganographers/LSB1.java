package ar.edu.itba.cripto.group4.esteganography.estaganographers;

import java.util.stream.Stream;
import org.jooq.lambda.Seq;

import static org.jooq.lambda.Seq.zip;

public class LSB1 implements Esteganographer{

    @Override
    public Stream<Byte> hide(Stream<Byte> image, Stream<Byte> data) {
        Stream<Byte> sparse_data = data.flatMap(b -> Stream.of(unnest_bytes(b)));
        return zip(image, sparse_data).map(x ->
                (byte)(((x.v1() << 1) >> 1) | x.v2())
        ).stream();
    }

    @Override
    public Stream<Byte> unhide(Stream<Byte> image) {
        return Stream.empty();
    }

    private Byte[] unnest_bytes(Byte data_byte) {
        Byte[] ret = new Byte[8];
        for(int i = 0; i < 8; i++){
            ret[i] = (byte)((data_byte << i) & 0b10000000);
        }
        return ret;
    }
}
