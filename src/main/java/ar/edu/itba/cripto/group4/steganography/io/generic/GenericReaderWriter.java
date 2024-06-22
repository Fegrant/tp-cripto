package ar.edu.itba.cripto.group4.steganography.io.generic;

import ar.edu.itba.cripto.group4.steganography.Utils;
import ar.edu.itba.cripto.group4.steganography.io.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class GenericReaderWriter implements ReaderWriter {
    @Override
    public ReaderOutput readFile(Path filepath) throws ReaderException {
        BufferedInputStream is;
        try {
            final var size = Files.size(filepath);

            is = new BufferedInputStream(new FileInputStream(filepath.toString()));
            
           var data = Utils.makeDataStream(is);
           var metadata = new GenericMetadata((int)size, filepath.getFileName().toString());
           
           return new GenericReaderOutput(data, metadata);
        } catch (IOException e) {
            throw new ReaderException("Could not read the file.");
        }
    }

    @Override
    public void writeFile(Path filepath, Stream<Byte> data, Metadata meta) {
        try(final var os = new BufferedOutputStream(new FileOutputStream(filepath.toString()))) {
            data.forEach(b -> {
                try {
                    os.write((int)b % 0xFF);
                } catch (IOException e) {
                    throw new WriterException("Error writing data to file");
                }
            });
        } catch(IOException e) {
            throw new WriterException("Error writing file");
        }
    }
}
