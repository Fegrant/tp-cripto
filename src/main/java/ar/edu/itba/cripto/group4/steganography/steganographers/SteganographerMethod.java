package ar.edu.itba.cripto.group4.steganography.steganographers;

import ar.edu.itba.cripto.group4.steganography.io.Metadata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.jooq.lambda.Seq.zip;

public enum SteganographerMethod {
    LSB1{
        public Stream<Byte> hide(Stream<Byte> image, Stream<Byte> data) {
            List<Byte> sparse_data = data.flatMap(b -> Stream.of(unnest_bytes(b))).toList();
            List<Byte> imageList = image.toList();
            
            List<Byte> res = new ArrayList<>();
            
            for(int i = 0; i < imageList.size(); i++) {
                if(i == sparse_data.size()) {
                    res.addAll(imageList.subList(i, imageList.size()));
                    break;
                }
                
                final byte a = imageList.get(i);
                final byte b = sparse_data.get(i);
                
                res.add((byte)(((a >> 1) << 1) | b));
            }
            
            return res.stream();
        }

        public Stream<Byte> unhide(Stream<Byte> image, Metadata meta) {
            List<Byte> fileBytesOnBits = image.map(b -> (Byte)(byte)(b & 1)).toList();
            Byte[] fileBytes = new Byte[fileBytesOnBits.size() / 8 + 1];
            for(int i=0 ; i < fileBytesOnBits.size() ; i++) {
                final int idx = i / 8;
                if(fileBytes[idx] == null) fileBytes[idx] = 0;
                fileBytes[idx] = (byte)(fileBytes[idx] + (byte)(fileBytesOnBits.get(i) << (7 - (i % 8))));
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

        public Stream<Byte> unhide(Stream<Byte> image, Metadata meta) {
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
        private static final byte[] patterns = new byte[]{0b00000000, 0b00000010, 0b00000100, 0b00000110};
        private static final byte pattern_mask = 0b00000110;
        private static final byte unhide_mask = 0b00000001;

        public Stream<Byte> hide(Stream<Byte> image, Stream<Byte> data) {
            Stream<Byte> sparse_data = data.flatMap(b -> Stream.of(unnest_bytes(b)));
            Stream<int[]> image_patterns = zip(image, sparse_data).map(x ->
                    signed_get_pattern(x.v1(), (x.v1() << 7 == x.v2() << 7))
            ).stream();
            int[] summed_patterns = image_patterns.reduce(
                    new int[]{0, 0, 0, 0},
                    (a, b) -> new int[]{a[0] + b[0], a[1] + b[1], a[2] + b[2], a[3] + b[3]}
            );
            Byte[] head = new Byte[4];
            for (int i = 0; i < summed_patterns.length; i++) {
                if (summed_patterns[i] >= 0)
                    head[i] = 1;
            }
            sparse_data = Stream.concat(Arrays.stream(head), sparse_data);
            return zip(image, sparse_data).map(x ->
                    (byte)(((x.v1() >> 1) << 1) | x.v2())
            ).stream().map(x -> {
                    byte pattern = (byte)(x & pattern_mask);
                    if (summed_patterns[pattern_index(pattern)] < 0)
                        return x;
                    return (byte)(x ^ 0b00000001);
                }
            );
        }

        public Stream<Byte> unhide(Stream<Byte> image, Metadata meta) {
            Integer[] used_patterns = (Integer[]) Arrays.stream(meta.getFirstFour()).map(b -> b & unhide_mask).toArray();
            Byte[] fileBytesOnBits = (Byte[])image.map(b -> {
                int flip = used_patterns[pattern_index((byte)(b & pattern_mask))];
                return (byte)(flip ^ (b & unhide_mask));
            }).toArray();
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

        private static int[] signed_get_pattern(Byte img_byte, boolean sign) {
            byte image_pattern = (byte)(img_byte & pattern_mask);
            int[] ret = new int[]{0, 0, 0, 0};
            int index = pattern_index(image_pattern);
            ret[index] = sign ? 1 : -1;
            return ret;
        }

        private static int pattern_index(byte pattern) {
            int index = -1;
            for (int i = 0; i < patterns.length; i++) {
                if (patterns[i] == pattern) {
                    index = i;
                    break;
                }
            }
            return index;
        }
    };

    abstract Stream<Byte> hide(Stream<Byte> image, Stream<Byte> data);
    abstract Stream<Byte> unhide(Stream<Byte> image, Metadata meta);

}
