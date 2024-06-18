package ar.edu.itba.cripto.group4.steganography;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class Utils {
    private Utils() {}
    
    public static Byte[] intToBytes(int i) {
        byte[] ba = ByteBuffer.allocate(4).putInt(i).array();
        Byte[] res = new Byte[4];

        Arrays.setAll(res, n -> ba[n]);
        
        return res;
    }
    
    public static int intFromBytes(List<Byte> bytes) {
        if(bytes.size() != 4) throw new IllegalArgumentException();
        
        byte[] ba = new byte[bytes.size()];
        for(int i = 0; i < bytes.size(); i++) {
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
        for(int i = 0; i < bytes.size(); i++) {
            ba[i] = bytes.get(i);
        }
        
        return new String(ba, StandardCharsets.UTF_8);
    }
}
