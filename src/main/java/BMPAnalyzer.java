import java.io.File;
import java.io.FileInputStream;

public class BMPAnalyzer {

    public static void main(String[] args){
        String path = "test_files/lado.bmp";
        File bmpFile = new File(path);
        analyzeBMPFile(bmpFile);
    }

    private static void analyzeBMPFile(File bmpFile){
        int HEADER_SIZE = 54;
        try (FileInputStream fis = new FileInputStream(bmpFile)) {
            byte[] header = new byte[HEADER_SIZE];
            if (fis.read(header) != HEADER_SIZE) {
                System.out.println("No se pudo leer la cabecera del archivo BMP.");
                return;
            }

            // Verifico si es un archivo BMP
            if (header[0] != 'B' || header[1] != 'M') {
                System.out.println("El archivo no es un BMP válido.");
                return;
            }

            int fileSize = ((header[5] & 0xff) << 24) | ((header[4] & 0xff) << 16) |
                    ((header[3] & 0xff) << 8) | (header[2] & 0xff);
            System.out.println("Tamaño del archivo BMP: " + fileSize + " bytes");

            // Offset del bitmap de la imagen
            int dataOffset = ((header[13] & 0xff) << 24) | ((header[12] & 0xff) << 16) |
                    ((header[11] & 0xff) << 8) | (header[10] & 0xff);
            System.out.println("Offset de los datos de la imagen: " + dataOffset + " bytes");

            byte[] imageData = new byte[fileSize - dataOffset];
            fis.skip(dataOffset - HEADER_SIZE);
            if (fis.read(imageData) != imageData.length) {
                System.out.println("No se pudieron leer los datos de la imagen.");
                return;
            }

            // Analizar los bits menos significativos
            int[] bitCounts = new int[2];
            for (byte imageDatum : imageData) {
                bitCounts[imageDatum & 1]++;
            }

            // Prueba de Chi-Cuadrado
            double chiSquare = calculateChiSquare(bitCounts, imageData.length / 2.0);
            System.out.println("Resultado de la prueba de Chi-Cuadrado: " + chiSquare);

            // Determinar la presencia de datos ocultos
            if (chiSquare > 3.841) { // valor crítico para p=0.05 y 1 grado de libertad
                System.out.println("Es probable que el archivo BMP contenga datos ocultos.");
            } else {
                System.out.println("No se encontraron datos ocultos en el archivo BMP.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static double calculateChiSquare(int[] observed, double expected) {
        double chiSquare = 0.0;
        for (int count : observed) {
            chiSquare += Math.pow(count - expected, 2) / expected;
        }
        return chiSquare;
    }

}
