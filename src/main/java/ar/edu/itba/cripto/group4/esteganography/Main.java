package ar.edu.itba.cripto.group4.esteganography;

import ar.edu.itba.cripto.group4.esteganography.estaganographers.Esteganographer;
import ar.edu.itba.cripto.group4.esteganography.estaganographers.EsteganographerImpl;
import ar.edu.itba.cripto.group4.esteganography.estaganographers.EsteganographerMethod;
import ar.edu.itba.cripto.group4.esteganography.io.ReaderOutput;
import ar.edu.itba.cripto.group4.esteganography.io.ReaderWriter;
import ar.edu.itba.cripto.group4.esteganography.io.bmp.BmpReaderWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
//        ArgumentParser argumentParser = new ArgumentParser(args);
        Esteganographer esteganographer = new EsteganographerImpl();
        final ReaderWriter rw = new BmpReaderWriter();
        
        String path = "test_files/ladoLSB1.bmp";
        final ReaderOutput ro = rw.readFile(Path.of(path));
        final var outList = esteganographer.unhide(ro.getData(), ro.getMetadata(), EsteganographerMethod.LSB1).toList();
        
        rw.writeFile(Path.of("imagen.png"), outList.stream().limit(outList.size() - 4));
        
        
//        File bmpFile = new File(path);
//        boolean isBmpFile = esteganographer.analyze(new FileInputStream(bmpFile));
//
//        if (isBmpFile) {
//            System.out.println("Es probable que el archivo BMP contenga datos ocultos");
//        } else {
//            System.out.println("Es poco probable que el archivo BMP contenga datos ocultos");
//        }
    }
}
