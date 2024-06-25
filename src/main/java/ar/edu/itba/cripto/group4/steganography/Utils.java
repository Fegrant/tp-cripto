package ar.edu.itba.cripto.group4.steganography;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Utils {
    private Utils() {
    }

    public static Byte[] intToBytes(int i) {
        byte[] ba = ByteBuffer.allocate(4).putInt(i).array();
        Byte[] res = new Byte[4];

        Arrays.setAll(res, n -> ba[n]);

        return res;
    }

    public static int intFromBytes(List<Byte> bytes) {
        if (bytes.size() != 4) throw new IllegalArgumentException();

        byte[] ba = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); i++) {
            ba[i] = bytes.get(i);
        }

        return ByteBuffer.wrap(ba).getInt();
    }

    public static Byte[] stringToBytes(String s) {
        byte[] ba = s.getBytes(StandardCharsets.UTF_8);
        Byte[] res = new Byte[ba.length];

        Arrays.setAll(res, n -> ba[n]);

        return res;
    }

    public static String stringFromBytes(List<Byte> bytes) {
        byte[] ba = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); i++) {
            ba[i] = bytes.get(i);
        }

        return new String(ba, StandardCharsets.UTF_8);
    }

    public static Stream<Byte> makeDataStream(InputStream is) {
        final Iterator<Byte> iter = new Iterator<>() {
            int next;

            @Override
            public boolean hasNext() {
                try {
                    next = is.read();

                    if (next != -1) return true;

                    is.close();
                    return false;
                } catch (IOException e) {
                    try {
                        is.close();
                    } catch (IOException ignored) {
                    }

                    throw new IllegalArgumentException(e);
                }
            }

            @Override
            public Byte next() {
                if (next == -1) throw new NoSuchElementException();
                return (byte) next;
            }
        };

        Iterable<Byte> iterable = () -> iter;

        return StreamSupport.stream(iterable.spliterator(), false);
    }
}
