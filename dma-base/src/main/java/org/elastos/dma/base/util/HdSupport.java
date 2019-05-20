package org.elastos.dma.base.util;

import io.github.novacrypto.bip32.ExtendedPrivateKey;
import io.github.novacrypto.bip32.ExtendedPrivateKeyR1;
import io.github.novacrypto.bip32.networks.Bitcoin;
import io.github.novacrypto.bip44.AddressIndex;
import io.github.novacrypto.bip44.Change;

import io.github.novacrypto.bip44.CoinType;
import org.elastos.dma.base.conf.ChangeConstant;
import org.spongycastle.crypto.digests.SHA512Digest;
import org.spongycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.spongycastle.crypto.params.KeyParameter;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: yangchuantong
 * @Description:
 * @Date:Created in  2019/4/4 16:42
 */
public class HdSupport {

    private static final int SEED_ITERATIONS = 2048;
    private static final int SEED_KEY_SIZE = 512;

    public static SecureRandom secureRandom = new SecureRandom();

    public enum MnemonicType {
        CHINESE,
        ENGLISH
    }

    /**
     * 获取助记词
     *
     * @param mnemonicType 助记词类型
     */
    public static String generateMnemonic(MnemonicType mnemonicType) {

        mnemonicType = mnemonicType == null ?MnemonicType.ENGLISH:mnemonicType;
        byte[] initialEntropy = new byte[16];
        secureRandom.nextBytes(initialEntropy);
        validateInitialEntropy(initialEntropy);
        int ent = initialEntropy.length * 8;
        int checksumLength = ent / 32;
        byte checksum = calculateChecksum(initialEntropy);
        boolean[] bits = convertToBits(initialEntropy, checksum);
        int iterations = (ent + checksumLength) / 11;
        StringBuilder mnemonicBuilder = new StringBuilder();

        List<String> strings = populateWordList(mnemonicType);

        for (int i = 0; i < iterations; ++i) {
            int index = toInt(nextElevenBits(bits, i));
            mnemonicBuilder.append(strings.get(index));
            boolean notLastIteration = i < iterations - 1;
            if (notLastIteration) {
                mnemonicBuilder.append(" ");
            }
        }
        return mnemonicBuilder.toString();
    }


    public static byte[] generateSeed(String mnemonic, String passphrase) {
        validateMnemonic(mnemonic);
        passphrase = passphrase == null ? "" : passphrase;
        String salt = String.format("mnemonic%s", passphrase);
        PKCS5S2ParametersGenerator gen = new PKCS5S2ParametersGenerator(new SHA512Digest());
        gen.init(mnemonic.getBytes(StandardCharsets.UTF_8), salt.getBytes(StandardCharsets.UTF_8), SEED_ITERATIONS);
        return ((KeyParameter) gen.generateDerivedParameters(SEED_KEY_SIZE)).getKey();
    }

    public static byte[] generateSeed(String mnemonic) {
        return generateSeed(mnemonic, null);
    }


    public static String generatePrivateKey(String mnemonic, String passphrase, Change acctType, int index) throws InvalidKeySpecException, NoSuchAlgorithmException {
        byte[] seedByte = generateSeed(mnemonic, passphrase);
        AddressIndex addressIndex = acctType.address(index);
        String privateKey = "";
        if (acctType == ChangeConstant.ELA_CHANGE) {
            ExtendedPrivateKeyR1 rootKey = ExtendedPrivateKeyR1.fromSeed(seedByte, null);
            ExtendedPrivateKeyR1 childPrivateKey = rootKey.derive(addressIndex, AddressIndex.DERIVATION);
            privateKey = childPrivateKey.getPrivKey();
        } else {
            ExtendedPrivateKey rootKey = ExtendedPrivateKey.fromSeed(seedByte, null);
            ExtendedPrivateKey childPrivateKey = rootKey.derive(addressIndex, AddressIndex.DERIVATION);
            privateKey = childPrivateKey.getPrivKey();
        }
        return privateKey;
    }

    public static String generatePrivateKey(String mnemonic, Change acctType, int index) throws InvalidKeySpecException, NoSuchAlgorithmException {
        return generatePrivateKey(mnemonic, null, acctType, index);
    }


    private static void validateMnemonic(String mnemonic) {
        if (mnemonic == null || mnemonic.trim().isEmpty()) {
            throw new IllegalArgumentException("Mnemonic is required to generate a seed");
        }
    }


    private static boolean[] nextElevenBits(boolean[] bits, int i) {
        int from = i * 11;
        int to = from + 11;
        return Arrays.copyOfRange(bits, from, to);
    }

    private static void validateInitialEntropy(byte[] initialEntropy) {
        if (initialEntropy == null) {
            throw new IllegalArgumentException("Initial entropy is required");
        } else {
            int ent = initialEntropy.length * 8;
            if (ent < 128 || ent > 256 || ent % 32 != 0) {
                throw new IllegalArgumentException("The allowed size of ENT is 128-256 bits of multiples of 32");
            }
        }
    }

    private static boolean[] convertToBits(byte[] initialEntropy, byte checksum) {
        int ent = initialEntropy.length * 8;
        int checksumLength = ent / 32;
        int totalLength = ent + checksumLength;
        boolean[] bits = new boolean[totalLength];

        int i;
        for (i = 0; i < initialEntropy.length; ++i) {
            for (int j = 0; j < 8; ++j) {
                byte b = initialEntropy[i];
                bits[8 * i + j] = toBit(b, j);
            }
        }

        for (i = 0; i < checksumLength; ++i) {
            bits[ent + i] = toBit(checksum, i);
        }

        return bits;
    }

    private static boolean toBit(byte value, int index) {
        return (value >>> 7 - index & 1) > 0;
    }

    private static int toInt(boolean[] bits) {
        int value = 0;

        for (int i = 0; i < bits.length; ++i) {
            boolean isSet = bits[i];
            if (isSet) {
                value += 1 << bits.length - i - 1;
            }
        }

        return value;
    }

    private static byte calculateChecksum(byte[] initialEntropy) {
        int ent = initialEntropy.length * 8;
        byte mask = (byte) (255 << 8 - ent / 32);
        byte[] bytes = sha256(initialEntropy);
        return (byte) (bytes[0] & mask);
    }

    private static List<String> populateWordList(MnemonicType mnemonicType) {
        List<String> list = new ArrayList<>();
        try {
            //使用java
            String fileName = "mnemonic_" + mnemonicType.name().toLowerCase() + ".txt";
            InputStream resourceAsStream = HdSupport.class.getClassLoader().getResourceAsStream(fileName);
            if (resourceAsStream == null) {
                resourceAsStream = HdSupport.class.getResourceAsStream("/data/words/" + fileName);
            }
            if (resourceAsStream == null) {
                resourceAsStream = HdSupport.class.getResourceAsStream("/assets/" + fileName);
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                list.add(line);
            }
            bufferedReader.close();
            resourceAsStream.close();
            return list;
        } catch (Exception var2) {
            var2.printStackTrace();
        }

        return list;
    }

    public static byte[] sha256(byte[] input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(input);
        } catch (NoSuchAlgorithmException var2) {
            throw new RuntimeException("Couldn't find a SHA-256 provider", var2);
        }
    }
}
