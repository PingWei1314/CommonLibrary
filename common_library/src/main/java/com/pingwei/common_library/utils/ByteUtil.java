package com.pingwei.common_library.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;

/**
 * 基本数据互转工具
 *
 * @author eguid
 *         eguid的官网：www.eguid.cc
 *         eguid的csdn博客：http://blog.csdn.net/eguid_1,博客园：http://www.cnblogs.com/eguid
 */
public class ByteUtil {
    private static ByteBuffer buffer = ByteBuffer.allocate(8);

    /**
     * int转byte
     *
     * @param x
     * @return
     */
    public static byte intToByte(int x) {
        return (byte) x;
    }

    /**
     * byte转int
     *
     * @param b
     * @return
     */
    public static int byteToInt(byte b) {
        //Java的byte是有符号，通过 &0xFF转为无符号
        return b & 0xFF;
    }

    /**
     * byte[]转int
     *
     * @param b
     * @return
     */
    public static int byteArrayToInt(byte[] b) {
        return b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;
    }

    public static int byteArrayToInt(byte[] b, int index) {
        return b[index + 3] & 0xFF |
                (b[index + 2] & 0xFF) << 8 |
                (b[index + 1] & 0xFF) << 16 |
                (b[index + 0] & 0xFF) << 24;
    }



    /**
     * byte[]转int
     * @param bytes
     * @return
     */
    public static int byteArrayToIntx(byte[] bytes) {
        int value = 0;
        // 由高位到低位
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (bytes[i] & 0x000000FF) << shift;// 往高位游
        }
        return value;
    }

    /**
     * int转byte[]
     *
     * @param a
     * @return
     */
    public static byte[] intToByteArray(int a) {
        return new byte[]{
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }

    /**
     * short转byte[]
     *
     * @param b
     * @param s
     * @param index
     */
    public static void byteArrToShort(byte b[], short s, int index) {
        b[index + 1] = (byte) (s >> 8);
        b[index + 0] = (byte) (s >> 0);
    }

    /**
     * byte[]转short
     *
     * @param b
     * @param index
     * @return
     */
    public static short byteArrToShort(byte[] b, int index) {
        return (short) (((b[index + 0] << 8) | b[index + 1] & 0xff));
    }

    /**
     * 16位short转byte[]
     *
     * @param s short
     * @return byte[]
     */
    public static byte[] shortToByteArr(short s) {
        byte[] targets = new byte[2];
        for (int i = 0; i < 2; i++) {
            int offset = (targets.length - 1 - i) * 8;
            targets[i] = (byte) ((s >>> offset) & 0xff);
        }
        return targets;
    }

    /**
     * byte[]转16位short
     *
     * @param b
     * @return
     */
    public static short byteArrToShort(byte[] b) {
        return byteArrToShort(b, 0);
    }

    /**
     * long转byte[]
     *
     * @param x
     * @return
     */
    public static byte[] longToBytes(long x) {
        buffer.putLong(0, x);
        return buffer.array();
    }

    /**
     * byte[]转Long
     *
     * @param bytes
     * @return
     */
    public static long bytesToLong(byte[] bytes) {
        buffer.put(bytes, 0, bytes.length);
        buffer.flip();//need flip
        return buffer.getLong();
    }

    /**
     * 从byte[]中抽取新的byte[]
     *
     * @param data  - 元数据
     * @param start - 开始位置
     * @param end   - 结束位置
     * @return 新byte[]
     */
    public static byte[] getByteArr(byte[] data, int start, int end) {
        byte[] ret = new byte[end - start];
        for (int i = 0; (start + i) < end; i++) {
            ret[i] = data[start + i];
        }
        return ret;
    }

    /**
     * 流转换为byte[]
     *
     * @param inStream
     * @return
     */
    public static byte[] readInputStream(InputStream inStream) {
        ByteArrayOutputStream outStream = null;
        try {
            outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            byte[] data = null;
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            data = outStream.toByteArray();
            return data;
        } catch (IOException e) {
            return null;
        } finally {
            try {
                if (outStream != null) {
                    outStream.close();
                }
                if (inStream != null) {
                    inStream.close();
                }
            } catch (IOException e) {
                return null;
            }
        }
    }

    /**
     * byte[]转inputstream
     *
     * @param b
     * @return
     */
    public static InputStream readByteArr(byte[] b) {
        return new ByteArrayInputStream(b);
    }

    /**
     * byte数组内数字是否相同
     *
     * @param s1
     * @param s2
     * @return
     */
    public static boolean isEq(byte[] s1, byte[] s2) {
        int slen = s1.length;
        if (slen == s2.length) {
            for (int index = 0; index < slen; index++) {
                if (s1[index] != s2[index]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * byte数组转换为Stirng
     *
     * @param s1     -数组
     * @param encode -字符集
     * @param err    -转换错误时返回该文字
     * @return
     */
    public static String getString(byte[] s1, String encode, String err) {
        try {
            return new String(s1, encode);
        } catch (UnsupportedEncodingException e) {
            return err == null ? null : err;
        }
    }

    /**
     * byte数组转换为Stirng
     *
     * @param s1-数组
     * @param encode-字符集
     * @return
     */
    public static String getString(byte[] s1, String encode) {
        return getString(s1, encode, null);
    }

    //测试
    public static void main(String[] args) {
        System.err.println(isEq(new byte[]{1, 2}, new byte[]{1, 2}));
    }

    /**
     * 字节数组转16进制字符串
     *
     * @param b
     * @return
     */
    public static String byteArrToHexString(byte[] b) {
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

    /**
     * 16进制字符创转int
     *
     * @param hexString
     * @return
     */
    public static int hexStringToInt(String hexString) {
        return Integer.parseInt(hexString, 16);
    }

    /**
     * 十进制转二进制
     *
     * @param i
     * @return
     */
    public static String intToBinary(int i) {
        return Integer.toBinaryString(i);
    }


    /**

     * 16进制直接转换成为字符串(无需Unicode解码)
     * @param hexStr
     * @return
     */

    public static byte[] hexStr2Bytes(String hexStr) {

        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return bytes;
    }

    /**
     * 浮点转换为字节
     *
     * @param f
     * @return
     */
    public static byte[] float2byte(float f) {

        // 把float转换为byte[]
        int fbit = Float.floatToIntBits(f);

        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (fbit >> (24 - i * 8));
        }

        // 翻转数组
        int len = b.length;
        // 建立一个与源数组元素类型相同的数组
        byte[] dest = new byte[len];
        // 为了防止修改源数组，将源数组拷贝一份副本
        System.arraycopy(b, 0, dest, 0, len);
        byte temp;
        // 将顺位第i个与倒数第i个交换
        for (int i = 0; i < len / 2; ++i) {
            temp = dest[i];
            dest[i] = dest[len - i - 1];
            dest[len - i - 1] = temp;
        }

        return dest;

    }

    /**
     * 字节转换为浮点
     *
     * @param b 字节（至少4个字节）
     * @param index 开始位置
     * @return
     */
    public static float byte2float(byte[] b, int index) {
        int l;
        l = b[index + 0];
        l &= 0xff;
        l |= ((long) b[index + 1] << 8);
        l &= 0xffff;
        l |= ((long) b[index + 2] << 16);
        l &= 0xffffff;
        l |= ((long) b[index + 3] << 24);
        return Float.intBitsToFloat(l);
    }

    public static String byteToHex(byte b){
        String hex = Integer.toHexString(b & 0xFF);
        if(hex.length() < 2){
            hex = "0" + hex;
        }
        return hex;
    }

    /**
     * 将String转化为byte[]数组
     *
     * @param arg 需要转换的String对象
     * @return 转换后的byte[]数组
     */
    public static byte[] toByteArray(String arg) {
        if (arg != null) {
            /* 1.先去除String中的' '，然后将String转换为char数组 */
            char[] NewArray = new char[1000];
            char[] array = arg.toCharArray();
            int length = 0;
            for (int i = 0; i < array.length; i++) {
                if (array[i] != ' ') {
                    NewArray[length] = array[i];
                    length++;
                }
            }
            /* 将char数组中的值转成一个实际的十进制数组 */
            int EvenLength = (length % 2 == 0) ? length : length + 1;
            if (EvenLength != 0) {
                int[] data = new int[EvenLength];
                data[EvenLength - 1] = 0;
                for (int i = 0; i < length; i++) {
                    if (NewArray[i] >= '0' && NewArray[i] <= '9') {
                        data[i] = NewArray[i] - '0';
                    } else if (NewArray[i] >= 'a' && NewArray[i] <= 'f') {
                        data[i] = NewArray[i] - 'a' + 10;
                    } else if (NewArray[i] >= 'A' && NewArray[i] <= 'F') {
                        data[i] = NewArray[i] - 'A' + 10;
                    }
                }
                /* 将 每个char的值每两个组成一个16进制数据 */
                byte[] byteArray = new byte[EvenLength / 2];
                for (int i = 0; i < EvenLength / 2; i++) {
                    byteArray[i] = (byte) (data[i * 2] * 16 + data[i * 2 + 1]);
                }
                return byteArray;
            }
        }
        return new byte[]{};
    }

    /**
     * 将byte[]数组转化为String类型
     *
     * @param arg    需要转换的byte[]数组
     * @param length 需要转换的数组长度
     * @return 转换后的String队形
     */
    public static String toHexString(byte[] arg, int length) {
        String result = new String();
        if (arg != null) {
            for (int i = 0; i < length; i++) {
                result = result
                        + (Integer.toHexString(
                        arg[i] < 0 ? arg[i] + 256 : arg[i]).length() == 1 ? "0"
                        + Integer.toHexString(arg[i] < 0 ? arg[i] + 256
                        : arg[i])
                        : Integer.toHexString(arg[i] < 0 ? arg[i] + 256
                        : arg[i])) + " ";
            }
            return result;
        }
        return "";
    }


    public static String bytesToHexString(byte[] src) {
        StringBuilder builder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        String hv;
        for (int i = 0; i < src.length; i++) {
            // 以十六进制（基数 16）无符号整数形式返回一个整数参数的字符串表示形式，并转换为大写
            hv = Integer.toHexString(src[i] & 0xFF).toUpperCase();
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }
        return builder.toString();
    }


    public static byte[] intToByte2(int val){
        byte[] b = new byte[4];
        b[0] = (byte)(val & 0xff);
        b[1] = (byte)((val >> 8) & 0xff);
        b[2] = (byte)((val >> 16) & 0xff);
        b[3] = (byte)((val >> 24) & 0xff);
        return b;
    }


    public static int byteToInt(byte b1,byte b2){
        String se1 = ByteUtil.byteToHex(b1);
        String se2 = ByteUtil.byteToHex(b2);
        BigInteger se = new BigInteger(se2 + se1, 16);
        return se.intValue();
    }
}

