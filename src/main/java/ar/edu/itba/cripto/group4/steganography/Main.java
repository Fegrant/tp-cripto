package ar.edu.itba.cripto.group4.steganography;

import ar.edu.itba.cripto.group4.steganography.steganographers.Steganographer;
import ar.edu.itba.cripto.group4.steganography.steganographers.SteganographerImpl;
import ar.edu.itba.cripto.group4.steganography.steganographers.SteganographerMethod;
import ar.edu.itba.cripto.group4.steganography.io.ReaderOutput;
import ar.edu.itba.cripto.group4.steganography.io.ReaderWriter;
import ar.edu.itba.cripto.group4.steganography.io.bmp.BmpReaderWriter;

import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {
//        ArgumentParser argumentParser = new ArgumentParser(args);
        Steganographer steganographer = new SteganographerImpl();
        final ReaderWriter rw = new BmpReaderWriter();
        
        String path = "test_files/ladoLSB1.bmp";
        final ReaderOutput ro = rw.readFile(Path.of(path));
        final var outList = steganographer.unhide(ro.getData(), ro.getMetadata(), SteganographerMethod.LSB1).toList();
        
        rw.writeFile(Path.of("imagen.png"), outList.stream().limit(outList.size() - 4));
        
        
//        File bmpFile = new File(path);
//        boolean isBmpFile = steganographer.analyze(new FileInputStream(bmpFile));
//
//        if (isBmpFile) {
//            System.out.println("Es probable que el archivo BMP contenga datos ocultos");
//        } else {
//            System.out.println("Es poco probable que el archivo BMP contenga datos ocultos");
//        }
    }
}
