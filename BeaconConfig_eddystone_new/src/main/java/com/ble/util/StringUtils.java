package com.ble.util;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;

/**
 * 十六进制和字节数组或者int之间转换。
 *
 * @author geshenjibi
 *
 */
public class StringUtils {

	/**
	 * 将单个字节转为十六进制。
	 *
	 * @param paramByte
	 * @return
	 */
	public static String Byte2Hex(Byte paramByte) {
		Object[] arrayOfObject = new Object[1];
		arrayOfObject[0] = paramByte;
		return String.format("%02x", arrayOfObject).toUpperCase();
	}

	/**
	 * 将Byte[]字节数组转为十六进制。
	 *
	 * @param paramArrayOfByte
	 * @return
	 */
	public static String ByteArrToHex(byte[] paramArrayOfByte) {
		StringBuilder localStringBuilder = new StringBuilder();
		int i = paramArrayOfByte.length;
		for (int j = 0; j < i; j++) {
			localStringBuilder.append(Byte2Hex(Byte.valueOf(paramArrayOfByte[j])));
			localStringBuilder.append(" ");
		}
		return localStringBuilder.toString();
	}

	/**
	 * 将Byte[]字节数组转为十六进制。
	 *
	 * @param paramArrayOfByte
	 * @param start
	 *            0开始
	 * @param length
	 *            长度
	 * @return
	 */
	public static String ByteArrToHex(byte[] paramArrayOfByte, int start, int length) {
		StringBuilder localStringBuilder = new StringBuilder();
		for (int i = start; i < length; i++)
			localStringBuilder.append(Byte2Hex(Byte.valueOf(paramArrayOfByte[i])));
		return localStringBuilder.toString();
	}

	/**
	 * 十六进制转为字节
	 *
	 * @param paramString
	 * @return
	 */
	public static byte HexToByte(String paramString) {
		paramString.replace(" ", "");
		return (byte) Integer.parseInt(paramString, 16);
	}

	/**
	 * 十六进制转为字节数组
	 *
	 * @param paramString
	 * @return
	 */
	public static byte[] HexToByteArr(String paramString) {
		int j = paramString.length();
		byte[] arrayOfByte;
		if (isOdd(j) != 1) {
			arrayOfByte = new byte[j / 2];
		} else {
			j++;
			arrayOfByte = new byte[j / 2];
			paramString = "0" + paramString;
		}
		int k = 0;
		for (int i = 0; i < j; i += 2) {
			arrayOfByte[k] = HexToByte(paramString.substring(i, i + 2));
			k++;
		}
		return arrayOfByte;
	}

	/**
	 * 十六进制转为int
	 *
	 * @param paramString
	 * @return
	 */
	public static int HexToInt(String paramString) {

		return Integer.parseInt(paramString, 16);
	}

	public static int isOdd(int paramInt) {
		return paramInt & 0x1;
	}

	/**
	 * 将源int数据转为byte[]
	 *
	 * @param iSource
	 * @param length
	 * @return
	 */
	public static byte[] toByteArray(int iSource, int length) {
		byte[] bLocalArr = new byte[length];
		for (int i = 0; (i < 4) && (i < length); i++) {
			bLocalArr[i] = (byte) (iSource >> 8 * i & 0xFF);
		}
		return bLocalArr;
	}

	/***
	 * 两个字节的字节数转化为int类型
	 *
	 * @param b
	 * @param hasFuHao
	 * @return
	 */
	public static int TwoByte2Int(byte b, boolean hasFuHao) {
		if (hasFuHao) {
			int c = b;
			return c;
		} else {
			int n = b & 0xFF;
			return n;
		}
	}

	/***
	 * 将byte[]16位的，就是每两个字节为一个int数，
	 *
	 * @param datas
	 * @return
	 */
	public static int[] ByteArray2IntArray(byte[] datas) {
		int[] result = new int[datas.length / 2];
		int resultIndex = 0;
		for (int i = 0; i < datas.length; i += 2) {
			if ((i + 1 < datas.length)) {
				result[resultIndex] = byte2int(datas[i], datas[i + 1]);
				resultIndex++;
			}
		}
		return result;

	}

	/**
	 * 将高位、低位两个字节转化为有符号的数据。 有符号的。
	 *
	 * @param high
	 * @param low
	 *            低位
	 * @return
	 */
	public static int byte2int(byte high, byte low) {
		short z = (short) (((high & 0x00FF) << 8) | (0x00FF & low));
		return z;
	}

	/**
	 * 转为无符号的int数
	 */
	private static int toUnsigned(short s) {
		return s & 0x0FFFF;
	}

	/***
	 * 两个高低字节生成无符号的数据。
	 *
	 * @param high
	 * @param low
	 * @return
	 */
	public static int byte2intUnsigned(byte high, byte low) {
		short z = (short) (((high & 0x00FF) << 8) | (0x00FF & low));
		return toUnsigned(z);
	}

	private static String hexString = "0123456789ABCDEF";

	//
	// /*
	// * 将字符串编码成16进制数字,适用于所有字符（包括中文）
	// */
	// public static String encode(String str) {
	// // 根据默认编码获取字节数组
	// byte[] bytes = str.getBytes();
	// StringBuilder sb = new StringBuilder(bytes.length * 2);
	// // 将字节数组中每个字节拆解成2位16进制整数
	// for (int i = 0; i < bytes.length; i++) {
	// sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));
	// sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
	// }
	// return sb.toString();
	// }

	/*
	 * 将16进制数字解码成字符串,适用于所有字符（包括中文）
	 */
	public static String decode(String bytes) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length() / 2);
		// 将每2位16进制整数组装成一个字节
		for (int i = 0; i < bytes.length(); i += 2)
			baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString.indexOf(bytes.charAt(i + 1))));
		return new String(baos.toByteArray());
	}

	/**
	 * 将byte[]转为各种进制的字符串
	 *
	 * @param bytes
	 *            byte[]
	 * @param radix
	 *            基数可以转换进制的范围，从Character.MIN_RADIX到Character.MAX_RADIX，
	 *            超出范围后变为10进制
	 * @return 转换后的字符串
	 */
	public static String binary(byte[] bytes, int radix) {
		// System.out.println("可以转换的进制范围：" + Character.MIN_RADIX + "-" +
		// Character.MAX_RADIX);
		// System.out.println("2进制：" + binary(bytes, 2));
		// System.out.println("5进制：" + binary(bytes, 5));
		// System.out.println("8进制：" + binary(bytes, 8));
		// System.out.println("16进制：" + binary(bytes, 16));
		// System.out.println("32进制：" + binary(bytes, 32));
		// System.out.println("64进制：" + binary(bytes, 64));//
		// 这个已经超出范围，超出范围后变为10进制显示
		// 这个2是指二进制。如果是16就是16进制
		return new BigInteger(1, bytes).toString(2);// 这里的1代表正数
	}

	/**
	 * 将bytes[]数组转化为二进制的字符串。
	 *
	 * @param bytes
	 * @return
	 */
	public static String binary2(byte[] bytes) {
		final String ZERO = "00000000";
		String result = "";
		for (int i = 0; i < bytes.length; i++) {
			String s = Integer.toBinaryString(bytes[i]);
			if (s.length() > 8) {
				s = s.substring(s.length() - 8);
			} else if (s.length() < 8) {
				s = ZERO.substring(s.length()) + s;
			}
			result += s;
		}
		return result;
	}

	/**
	 * 将单个bytes转化为二进制的字符串。
	 *
	 * @param b
	 * @return
	 */
	public static String binary2(byte b) {
		String ZERO = "00000000";
		String result = "";
		String s = Integer.toBinaryString(b);
		if (s.length() > 8) {
			s = s.substring(s.length() - 8);
		} else if (s.length() < 8) {
			s = ZERO.substring(s.length()) + s;
		}
		result += s;
		return result;
	}

	/**
	 * 将二进制转为十六进制字符串
	 *
	 * @param s
	 * @return
	 */
	public static String binaryToHex(String s) {
		String result = Long.toHexString(Long.parseLong(s, 2));
		if (result.length() % 2 == 1) {
			result = "0" + result;
		}
		return result;
	}

	/** bit二进制数据转为byte */
	public static byte bit2byte(String bString) {
		byte result = 0;
		for (int i = bString.length() - 1, j = 0; i >= 0; i--, j++) {
			result += (Byte.parseByte(bString.charAt(i) + "") * Math.pow(2, j));
		}
		return result;
	}

	public static String replaceAll(String raw,String p1,String p2){
		return raw.replaceAll(p1,p2);
	}
}
