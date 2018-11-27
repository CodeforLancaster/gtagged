package org.codeforlancaster.gtagged;

/**
 * @author Will Faithfull
 */
public class Hex {

    final private static char[] hexAlphabet = "0123456789ABCDEF".toCharArray();

    public static String toHexString(byte[] bytes) {
        char[] stringBase = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            stringBase[i * 2] = hexAlphabet[v >>> 4];
            stringBase[i * 2 + 1] = hexAlphabet[v & 0x0F];
        }

        return new String(stringBase);
    }

}
