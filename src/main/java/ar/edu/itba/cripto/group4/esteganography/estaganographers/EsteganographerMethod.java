package ar.edu.itba.cripto.group4.esteganography.estaganographers;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.jooq.lambda.Seq.zip;

public enum EsteganographerMethod {
    LSB1{
        public Stream<Byte> hide(Stream<Byte> image, Stream<Byte> data) {
            Stream<Byte> sparse_data = data.flatMap(b -> Stream.of(unnest_bytes(b)));
            return zip(image, sparse_data).map(x ->
                    (byte)(((x.v1() >> 1) << 1) | x.v2())
            ).stream();
        }

        public Stream<Byte> unhide(Stream<Byte> image) {
            Byte[] fileBytesOnBits = (Byte[])(image.map(b -> (byte)(b & 1)).toArray());
            Byte[] fileBytes = new Byte[fileBytesOnBits.length / 8];
            for(int i=0 ; i <= fileBytesOnBits.length ; i++) {
                fileBytes[i / 8] = (byte)(fileBytes[i / 8] + (byte)(fileBytesOnBits[i] << (7 - (i % 8))));
            }
            return Arrays.stream(fileBytes);
        }

        private static Byte[] unnest_bytes(Byte data_byte) {
            Byte[] ret = new Byte[8];
            for(int i = 0; i < 8; i++){
                ret[i] = (byte)((data_byte >> (8 - i - 1)) & 1);
            }
            return ret;
        }
    },
    LSB4{
        public Stream<Byte> hide(Stream<Byte> image, Stream<Byte> data) {
            Stream<Byte> sparse_data = data.flatMap(b -> Stream.of(unnest_bytes(b)));
            return zip(image, sparse_data).map(x ->
                    (byte)(((x.v1() >> 4) << 4) | x.v2())
            ).stream();
        }

        public Stream<Byte> unhide(Stream<Byte> image) {
            Byte[] fileBytesOnBits = (Byte[])(image.map(b -> (byte)(b & 15)).toArray());
            Byte[] fileBytes = new Byte[fileBytesOnBits.length / 2];
            for(int i=0 ; i <= fileBytesOnBits.length ; i++) {
                fileBytes[i / 2] = (byte)(fileBytes[i / 2] + (byte)(fileBytesOnBits[i] << (7 - (4 * (i % 2)))));
            }
            return Arrays.stream(fileBytes);
        }

        private Byte[] unnest_bytes(Byte data_byte) {
            Byte[] ret = new Byte[2];
            ret[0] = (byte)((data_byte & 0b11110000) >> 4);
            ret[1] = (byte)(data_byte & 0b00001111);
            return ret;
        }
    },
    LSBImproved{
        public Stream<Byte> hide(Stream<Byte> image, Stream<Byte> data) {
            return Stream.empty();
        }

        public Stream<Byte> unhide(Stream<Byte> image) {
            return Stream.empty();
        }
    };

    abstract Stream<Byte> hide(Stream<Byte> image, Stream<Byte> data);
    abstract Stream<Byte> unhide(Stream<Byte> image);

}
