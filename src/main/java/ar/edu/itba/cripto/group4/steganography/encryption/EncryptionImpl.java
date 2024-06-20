package ar.edu.itba.cripto.group4.steganography.encryption;

import ar.edu.itba.cripto.group4.steganography.ArgumentParser;
import ar.edu.itba.cripto.group4.steganography.enums.EncryptionMode;
import ar.edu.itba.cripto.group4.steganography.enums.EncryptionType;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EncryptionImpl implements Encryption {

    private EncryptionType encType;
    private EncryptionMode encMode;
    private SecretKey key;
    private IvParameterSpec iv;

    static Integer byteSize = 8;

    public EncryptionImpl(ArgumentParser argumentParser) {
        this.encType = argumentParser.getEncryptionType();
        this.encMode = argumentParser.getEncryptionMode();
        byte[][] keyAndIv = generateKeyAndIv(argumentParser.getPassword(), encType, encMode);
        this.key = new SecretKeySpec(keyAndIv[0], encType.getAlgorithm());
        this.iv = encMode == EncryptionMode.ECB ? null : new IvParameterSpec(keyAndIv[1]);
    }

    @Override
    public List<Byte> encrypt(List<Byte> data) {
        try {
            Cipher cipher = Cipher.getInstance(encType.getAlgorithm() + "/" + encMode.name() + "/PKCS5Padding");

            if (encMode == EncryptionMode.ECB) {
                cipher.init(Cipher.ENCRYPT_MODE, key);
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            }

            byte[] dataBytes = listToArray(data);
            byte[] encryptedData = cipher.doFinal(dataBytes);

            System.out.println("Encrypted data size: " + encryptedData.length);
            if (encMode != EncryptionMode.ECB) {
                System.out.println("IV used for encryption (hex): " + bytesToHex(iv.getIV()));
            }

            return arrayToList(encryptedData);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Byte> decrypt(List<Byte> data) {
        try {

            byte[] dataArray = listToArray(data);
            System.out.println("Data array size: " + dataArray.length);
            Cipher cipher = Cipher.getInstance(encType.getAlgorithm() + "/" + encMode.name() + "/NoPadding");

            if (encMode == EncryptionMode.ECB) {
                cipher.init(Cipher.DECRYPT_MODE, key);
            } else {
                cipher.init(Cipher.DECRYPT_MODE, key, iv);
            }

            byte[] decryptedData = cipher.doFinal(dataArray);

            System.out.println("Decrypted data size: " + decryptedData.length);
            System.out.println("Derived key for decryption (hex): " + bytesToHex(key.getEncoded()));
            if (encMode != EncryptionMode.ECB) {
                System.out.println("IV used for decryption (hex): " + bytesToHex(iv.getIV()));
            }

            return arrayToList(decryptedData);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private byte[][] generateKeyAndIv(String password, EncryptionType encType, EncryptionMode encMode) {
        try {
            int keySize = encType.getKeySize();
            int ivSize = encType.getAlgorithm().equals("DESede") ? 8 : 16; // 8 bytes for 3DES, 16 bytes for AES
            byte[] salt = new byte[8]; // 8 bytes of zeros for the salt
            int iterationCount = 10000; // OpenSSL default iteration count

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterationCount, (keySize + ivSize) * 8); // Multiply by 8 to convert bytes to bits
            byte[] keyAndIv = factory.generateSecret(spec).getEncoded();

            byte[] keyBytes = Arrays.copyOfRange(keyAndIv, 0, keySize / byteSize);
            byte[] ivBytes = Arrays.copyOfRange(keyAndIv, keySize / byteSize, keySize / byteSize + ivSize);

            System.out.println("Generated key (hex): " + bytesToHex(keyBytes));
            if (encMode != EncryptionMode.ECB) {
                System.out.println("Generated IV (hex): " + bytesToHex(ivBytes));
            }

            return new byte[][] { keyBytes, ivBytes };
        } catch (Exception e) {
            throw new RuntimeException("Error generating key and IV", e);
        }
    }

    private byte[] listToArray(List<Byte> data) {
        byte[] byteArray = new byte[data.size()];
        for (int i = 0; i < data.size(); i++) {
            byteArray[i] = data.get(i);
        }
        return byteArray;
    }

    private List<Byte> arrayToList(byte[] array) {
        List<Byte> list = new ArrayList<>();
        for (byte b : array) {
            list.add(b);
        }
        return list;
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
