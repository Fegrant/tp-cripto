package ar.edu.itba.cripto.group4.esteganography.io.bmp;

import ar.edu.itba.cripto.group4.esteganography.io.*;

import java.io.*;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class BmpReaderWriter implements ReaderWriter<BmpReaderWriter> {
    private static final int HEADER_SIZE = 54;

    @Override
    public ReaderOutput<BmpReaderWriter> readFile(Path filepath) {
        BufferedInputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(filepath.toString()));

            byte[] header = is.readNBytes(HEADER_SIZE);

            // Verifico que sea un archivo BMP válido
            if (header.length != HEADER_SIZE || header[0] != 'B' || header[1] != 'M') {
                throw new IllegalArgumentException();
            }

            int fileSize = ((header[5] & 0xff) << 24) | ((header[4] & 0xff) << 16) |
                ((header[3] & 0xff) << 8) | (header[2] & 0xff);

            // Marco la posición inicial
            if (!is.markSupported()) throw new IllegalStateException();
            is.mark(1024);

            // TODO: encontrar una mejor solución?
            final byte[] firstFour = is.readNBytes(4);

            // Reseteo a la posición inicial
            is.reset();

            var data = makeDataStream(is, firstFour);
            var metadata = new BmpMetadata(fileSize, firstFour, header);
            return new BmpReaderOutput(metadata, data);
        } catch (IOException e) {
            throw new ReaderException("Could not read the file.");
        } catch (IllegalArgumentException e) {
            if (is != null) {
                throw new ReaderException("The file is not a valid BMP.");
            }
        }

        throw new ReaderException("Unknown error reading file.");
    }

    @Override
    public void writeFile(Path filepath, Metadata<BmpReaderWriter> metadata, Stream<Byte> data) {
        try(final var outStream = new BufferedOutputStream(new FileOutputStream(filepath.toString()))) {
            outStream.write(metadata.getHeader());
            data.forEach(b -> {
                try {
                    outStream.write((int)b % 0xFF);
                } catch (IOException e) {
                    throw new WriterException("Error writing data to file.");
                }
            });
        } catch (IOException e) {
            throw new WriterException("Error writing file.");
        }
    }

    private Stream<Byte> makeDataStream(InputStream is, byte[] firstFour) {
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
