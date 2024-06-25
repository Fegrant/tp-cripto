package ar.edu.itba.cripto.group4.steganography.steganographers;

import ar.edu.itba.cripto.group4.steganography.io.Metadata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public enum SteganographerMethod {
    LSB1 {
        public Stream<Byte> embed(Stream<Byte> image, Metadata meta, Stream<Byte> data, long dataSize) {
            if (dataSize > meta.getDataSize() / 8)
                throw new BiggerThanCapacityException();

            List<Byte> sparse_data = data.flatMap(b -> Stream.of(unnest_bytes(b))).toList();
            List<Byte> imageList = image.toList();

            List<Byte> res = new ArrayList<>();

            for (int i = 0; i < imageList.size(); i++) {
                if (i == sparse_data.size()) {
                    res.addAll(imageList.subList(i, imageList.size()));
                    break;
                }

                final byte a = imageList.get(i);
                final byte b = sparse_data.get(i);

                res.add((byte) (((a >> 1) << 1) | b));
            }

            return res.stream();
        }

        public Stream<Byte> extract(Stream<Byte> image, Metadata meta) {
            List<Byte> fileBytesOnBits = image.map(b -> (Byte) (byte) (b & 0x01)).toList();
            Byte[] fileBytes = new Byte[fileBytesOnBits.size() / 8 + 1];
            for (int i = 0; i < fileBytesOnBits.size(); i++) {
                final int idx = i / 8;
                if (fileBytes[idx] == null) fileBytes[idx] = 0;
                fileBytes[idx] = (byte) (fileBytes[idx] + (byte) (fileBytesOnBits.get(i) << (7 - (i % 8))));
            }
            return Arrays.stream(fileBytes);
        }

        private static Byte[] unnest_bytes(Byte data_byte) {
            Byte[] ret = new Byte[8];
            for (int i = 0; i < 8; i++) {
                ret[i] = (byte) ((data_byte >> (8 - i - 1)) & 1);
            }
            return ret;
        }
    },
    LSB4 {
        public Stream<Byte> embed(Stream<Byte> image, Metadata meta, Stream<Byte> data, long dataSize) {
            if (dataSize > meta.getDataSize() / 2)
                throw new BiggerThanCapacityException();

            List<Byte> sparse_data = data.flatMap(b -> Stream.of(unnest_bytes(b))).toList();
            List<Byte> imageList = image.toList();

            List<Byte> res = new ArrayList<>();

            for (int i = 0; i < imageList.size(); i++) {
                if (i == sparse_data.size()) {
                    res.addAll(imageList.subList(i, imageList.size()));
                    break;
                }

                final byte a = imageList.get(i);
                final byte b = sparse_data.get(i);

                res.add((byte) (((a >> 4) << 4) | b));
            }

            return res.stream();
        }

        public Stream<Byte> extract(Stream<Byte> image, Metadata meta) {
            List<Byte> fileBytesOnBits = image.map(b -> (byte) (b & 0x0F)).toList();
            Byte[] fileBytes = new Byte[fileBytesOnBits.size() / 2 + 1];
            for (int i = 0; i < fileBytesOnBits.size(); i++) {
                final int idx = i / 2;
                if (fileBytes[idx] == null) fileBytes[idx] = 0;
                fileBytes[idx] = (byte) (fileBytes[idx] + (byte) (fileBytesOnBits.get(i) << (4 - (4 * (i % 2)))));
            }
            return Arrays.stream(fileBytes);
        }

        private Byte[] unnest_bytes(Byte data_byte) {
            Byte[] ret = new Byte[2];
            ret[0] = (byte) ((data_byte & 0xF0) >> 4);
            ret[1] = (byte) (data_byte & 0x0F);
            return ret;
        }
    },
    LSBI {
        private static final byte pattern_mask = 0b00000110;
        private static final byte unhide_mask = 0b00000001;

        public Stream<Byte> embed(Stream<Byte> image, Metadata meta, Stream<Byte> data, long dataSize) {
            if (dataSize > (meta.getDataSize() / 3 * 2) / 8 - 4)
                throw new BiggerThanCapacityException();

            List<Byte> sparse_data = data.flatMap(b -> Stream.of(unnest_bytes(b))).toList();
            List<Byte> imageList = image.toList();

            List<int[]> imagePatternList = new ArrayList<>();

            for (int i = 0; i < imageList.size(); i++) {
                Byte v1 = imageList.get(i);

                if (i >= sparse_data.size()) {
                    imagePatternList.add(signed_get_pattern(v1, false));
                    continue;
                }

                Byte v2 = sparse_data.get(i);
                imagePatternList.add(signed_get_pattern(v1, (v1 << 7 == v2 << 7)));
            }

            int[] summed_patterns = imagePatternList.stream().reduce(
                new int[]{0, 0, 0, 0},
                (a, b) -> new int[]{a[0] + b[0], a[1] + b[1], a[2] + b[2], a[3] + b[3]}
            );
            List<Byte> head = new ArrayList<>();
            for (int i = 0; i < summed_patterns.length; i++) {
                if (summed_patterns[i] >= 0)
                    head.add(i, (byte) 0b00000001);
                else
                    head.add(i, (byte) 0b00000000);
            }

            // Primeros 4 bytes normal
            List<Byte> res = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                final byte v1 = imageList.get(i);
                final byte v2 = head.get(i);

                res.add((byte) (((v1 >> 1) << 1) | v2));
            }

            int skipped = 0;
            for (int i = 0; i < imageList.size(); i++) {
                if ((i + 4) % 3 == 2) {   // Canal R -> Pongo los datos de la imagen original y sigo
                    res.add(imageList.get(i + 4 - skipped));
                    skipped++;
                    continue;
                }
                if (i - skipped == sparse_data.size()) {
                    res.addAll(imageList.subList(i + 4, imageList.size()));
                    break;
                }

                final byte v1 = imageList.get(i + 4);
                final byte v2 = sparse_data.get(i - skipped);

                res.add((byte) (((v1 >> 1) << 1) | v2));
            }

            List<Byte> headLSB1 = res.subList(0, 4);

            headLSB1.addAll(res.subList(4, res.size()).stream().map(x -> {
                byte pattern = (byte) (x & pattern_mask);
                if (summed_patterns[pattern >> 1] <= 0)
                    return x;
                return (byte) (x ^ 0b00000001);
            }).toList());

            return headLSB1.stream();
        }

        public Stream<Byte> extract(Stream<Byte> image, Metadata meta) {
            List<Boolean> used_patterns = Arrays.stream(meta.getFirstFour()).map(b -> (byte) (b & unhide_mask) == 1).toList();
            image = image.skip(4);
            List<Byte> fileBytesOnBits = new ArrayList<>(image.map(b -> {
                // boolean flip = used_patterns.get((b & pattern_mask) >> 1) == 1;
                boolean flip = used_patterns.get((b & pattern_mask) >> 1);
                byte maskedB = (byte) (b & unhide_mask);
                if (flip) {
                    maskedB ^= 0b00000001;
                }
                return maskedB;
            }).toList());

            // Ya leí los 4 bytes de la imagen BGRB
            // Después queda en orden GRBGRBGRB..., entonces cono el canal R no tienen info, lo skipeo
            List<Byte> fileBytesOnBitsBG = new ArrayList<>();
            for (int j = 0; j < fileBytesOnBits.size(); j++) {
                if (j % 3 != 1) fileBytesOnBitsBG.add(fileBytesOnBits.get(j));
            }
            fileBytesOnBits = fileBytesOnBitsBG;

            Byte[] fileBytes = new Byte[fileBytesOnBits.size() / 8 + 1];
            for (int i = 0; i < fileBytesOnBits.size(); i++) {
                final int idx = i / 8;
                if (fileBytes[idx] == null) fileBytes[idx] = 0;
                fileBytes[idx] = (byte) (fileBytes[idx] + (byte) (fileBytesOnBits.get(i) << (7 - (i % 8))));
            }
            return Arrays.stream(fileBytes);
        }

        private static Byte[] unnest_bytes(Byte data_byte) {
            Byte[] ret = new Byte[8];
            for (int i = 0; i < 8; i++) {
                ret[i] = (byte) ((data_byte >> (7 - i)) & 1);
            }
            return ret;
        }

        private static int[] signed_get_pattern(Byte img_byte, boolean sign) {
            byte image_pattern = (byte) (img_byte & pattern_mask);
            int[] ret = new int[]{0, 0, 0, 0};
            ret[image_pattern >> 1] = sign ? 1 : -1;
            return ret;
        }
    };

    abstract Stream<Byte> embed(Stream<Byte> image, Metadata meta, Stream<Byte> data, long dataSize);

    abstract Stream<Byte> extract(Stream<Byte> image, Metadata meta);

}
