package com.kongqw.serialportlibrary;

import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;

public class Tool {
    //接收what
    public final static int SERIAL_TYPE_WHAT_1 = 1001; //获取序列号
    public final static int SERIAL_TYPE_WHAT_5 = 1005; //电机
    public final static int SERIAL_TYPE_WHAT_4 = 1004;
    public final static int SERIAL_TYPE_WHAT_3 = 1003;

    public final static int ZBJ_A = 1;
    public final static int ZBJ_B = 2;
    public final static int ZBJ_C = 3;
    public final static int ZBJ_D = 4;
    public final static int ZBJ_E = 5;
    public final static int ZBJ_F = 6;
    public final static int ZBJ_G = 7;

    public final static String MOTOR = "MOTOR";
    public final static String MOTOR_NUMBER = "MOTOR_NUMBER";

    public static String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        for (byte b : bArray) {
            String sTemp = Integer.toHexString(b & MotionEventCompat.ACTION_MASK);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }
    public static byte[] decode(String src) {
        int m = 0, n = 0;
        int byteLen = src.length() / 2; // 每两个字符描述一个字节
        byte[] ret = new byte[byteLen];
        for (int i = 0; i < byteLen; i++) {
            m = i * 2 + 1;
            n = m + 1;
            int intVal = Integer.decode("0x" + src.substring(i * 2, m) + src.substring(m, n));
            ret[i] = Byte.valueOf((byte)intVal);
        }
        return ret;
    }
    public static void printHexString(byte[] b) {
        for (byte b2 : b) {
            String hex = Integer.toHexString(b2 & MotionEventCompat.ACTION_MASK);
            if (hex.length() == 1) {
                hex = new StringBuilder(String.valueOf('0')).append(hex).toString();
            }
            System.out.print(hex.toUpperCase());
        }
    }

    public static byte[] charToByte(char c) {
        return new byte[]{(byte) ((MotionEventCompat.ACTION_POINTER_INDEX_MASK & c) >> 8), (byte) (c & MotionEventCompat.ACTION_MASK)};
    }

    public static char byteToChar(byte[] b) {
        return (char) (((b[0] & MotionEventCompat.ACTION_MASK) << 8) | (b[1] & MotionEventCompat.ACTION_MASK));
    }

    public static String toHex(byte b) {
        return "" + "0123456789ABCDEF".charAt((b >> 4) & 15) + "0123456789ABCDEF".charAt(b & 15);
    }

    public static int toInt(String hex) {
        int ss;
        if (hex.charAt(0) - 65 >= 0) {
            ss = 0 + (((hex.charAt(0) - 65) + 10) * 16);
        } else {
            ss = 0 + ((hex.charAt(0) - 48) * 16);
        }
        if (hex.charAt(1) - 65 >= 0) {
            return ss + ((hex.charAt(1) - 65) + 10);
        }
        return ss + (hex.charAt(1) - 48);
    }

    public static int toInt(byte b) {
        return b & MotionEventCompat.ACTION_MASK;
    }

    public static String Make_CRC(byte[] data) {
        int i;
        byte[] buf = new byte[data.length];
        for (i = 0; i < data.length; i++) {
            buf[i] = data[i];
        }
        int len = buf.length;
        int crc = SupportMenu.USER_MASK;
        for (int pos = 0; pos < len; pos++) {
            if (buf[pos] < (byte) 0) {
                crc ^= buf[pos] + 256;
            } else {
                crc ^= buf[pos];
            }
            for (i = 8; i != 0; i--) {
                if ((crc & 1) != 0) {
                    crc = (crc >> 1) ^ 40961;
                } else {
                    crc >>= 1;
                }
            }
        }
        String c = Integer.toHexString(crc);
        if (c.length() == 4) {
            Log.d("原始码4位", c);
            return c.substring(2, 4) + c.substring(0, 2);
        } else if (c.length() == 3) {
            Log.d("原始码3位", c);
            c = "0" + c;
            return c.substring(2, 4) + c.substring(0, 2);
        } else if (c.length() != 2) {
            return c;
        } else {
            Log.d("原始码2位", c);
            c = "00" + c;
            return c.substring(2, 4) + c.substring(0, 2);
        }
    }
}