/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.license;

import java.io.File;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆2015年1月21日
 * @version 1.0
 **/
public class TimeLimiteEndecrypt {

	private static int l = 2;

	private static final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

	private static final SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");

	public static String gen(int leftDay) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, leftDay);

		String s = format.format(c.getTime());// DateUtils.format(c.getTime(),
												// "yyyyMMdd");
		return convertMD5(mixin(s));
	}

	public static String gen(Date expired) {
		Calendar c = Calendar.getInstance();
		c.setTime(expired);
		String s = format.format(c.getTime());// DateUtils.format(c.getTime(),
												// "yyyyMMdd");
		return convertMD5(mixin(s));
	}

	private static String mixin(String target) {
		String md5 = toMD5(target);

		int i = 0;
		char[] ch1 = md5.toCharArray();
		char[] ch2 = target.toCharArray();
		int ll = ch2.length;
		int lch = 0;
		StringBuilder sb = new StringBuilder();
		while (i < ch1.length) {
			if (i > l && i % (l) == 0 && lch < ll) {
				sb.append(ch2[lch++]);
			}
			sb.append(ch1[i++]);
		}
		return sb.toString();
	}

	private static String retrive(String s) {
		String md5 = convertMD5(s);
		char[] ch1 = md5.toCharArray();
		StringBuilder sb = new StringBuilder();
		int i = ch1.length;
		for (int j = l; j < i; j++) {
			if (sb.length() == 0 && j > l && j % l == 0)
				sb.append(ch1[j]);
			else if (j % (l + 1) == 0) {
				sb.append(ch1[j + 1]);
			}
			if (sb.length() >= 8)
				break;

		}

		return sb.toString();
	}

	public static boolean lt(Calendar date, String target) {
		try {
			String s = format.format(date.getTime());// DateUtils.format(date.getTime(),
														// "yyyyMMdd");
			String s1 = retrive(target);

			return Integer.valueOf(s).compareTo(Integer.valueOf(s1)) > 0 ? false : true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private static String toMD5(String s) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			return "";
		}
		char[] charArray = s.toCharArray();
		byte[] byteArray = new byte[charArray.length];
		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}

	private static String convertMD5(String inStr) {
		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ 't');
		}
		String s = new String(a);
		return s;
	}

	public static void main(String[] args) throws Exception {
		System.out.println("请输入License的有效天数（正整数）或者到期日期（yyyy-mm-dd）：");
		Scanner sc = new Scanner(System.in);

		while (true) {
			if (sc.hasNext()) {
				try {
					String is = sc.nextLine();
					if (is == null || is.length() == 0)
						continue;

					File licenseFile = new File("D1:/Cntest.license");
					licenseFile.createNewFile();
					String s = getLimite(is);
					RandomAccessFile raf = new RandomAccessFile(licenseFile, "rw");

					raf.write(s.getBytes());

					raf.close();

					RandomAccessFile raf2 = new RandomAccessFile("D:/Cntest.license", "rw");
					s = raf2.readLine();
					System.out.println("License文件：" + licenseFile.getAbsolutePath());
					System.out.println(s);
					raf2.close();
					sc.close();
					System.exit(0);
				} catch (Exception e) {
					System.out.println(e.getMessage());
					sc.reset();
				}
			}
		}
	}

	private static String getLimite(String in) throws Exception {
		try {
			int limite = Integer.valueOf(in);
			if (limite < 1)
				throw new IllegalArgumentException("请输入正整数");
			return TimeLimiteEndecrypt.gen(limite);
		} catch (Exception e) {
			return getLimiteOfDateString(in);
		}
	}

	private static String getLimiteOfDateString(String in) throws Exception {

		Date expired = null;
		try {
			expired = format2.parse(in);
		} catch (Exception e) {
			throw new IllegalArgumentException("请输入正整数或者日期");
		}
		Date now = Calendar.getInstance().getTime();
		if (expired.compareTo(now) < 0) {
			throw new IllegalArgumentException("输入的日期必须大于当前日期");
		}
		return TimeLimiteEndecrypt.gen(expired);

	}
}
