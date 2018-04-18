package com.cntest.fxpt.anlaysis.uitl;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class NumberFormat {
	private static final String format = "0.00";
	private static final String percentFormat = "0.00%";

	public static String clearZero(String value) {
		return clearZeroForObj(value);
	}

	public static String clearZero(double value) {
		return clearZeroForObj(value);
	}

	private static String clearZeroForObj(Object value) {
		String s = String.valueOf(value);
		int index = s.indexOf(".");
		if (index < 0) {
			return s;
		} else {
			char c = s.charAt(s.length() - 1);
			if (c == '0' || c == '.') {
				s = s.substring(0, s.length() - 1);
				return clearZero(s);
			} else {
				return s;
			}
		}
	}

	public static String format(double d) {
		return format(d, format);
	}

	public static String format(double d, String format) {
		d = Double.isNaN(d) ? 0.0 : d;
		DecimalFormat nf = new DecimalFormat(format);
		nf.setRoundingMode(RoundingMode.HALF_UP);
		String re = nf.format(d);
		return re;
	}
}
