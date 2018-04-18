package com.cntest.fxpt.personalReport;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class FormatUtil {
	public static String format(Object object, String format) {
		double parseDouble = 0;
		if (object == null) {
			parseDouble = 0;
		}
		if (object instanceof Double) {
			parseDouble = (Double) object;
			parseDouble = Double.isNaN(parseDouble) ? 0.0 : parseDouble;
		} else if (object instanceof BigDecimal) {
			parseDouble = ((BigDecimal) object).doubleValue();
		} else if (object instanceof String) {
			try {
				parseDouble = Double.parseDouble((String) object);
			} catch (Exception e) {
				parseDouble = 0;
			}
		}
		return format(parseDouble, format);
	}

	public static String formatWithPositiveAndNagativeSymbol(double d, String format){
		String result = format(d, format);
		return d>0?"+"+result:result;
	}
	
	public static String format(double d, String format) {
		d = Double.isNaN(d) ? 0.0 : d;
		NumberFormat nf = NumberFormat.getInstance();
		nf.setRoundingMode(RoundingMode.HALF_UP);
		nf = new DecimalFormat(format);
		String re = nf.format(d);
		return re;
	}
	public static String format(String d, String format) {
		double tmp = format(d);
		return format(tmp, format);
	}

	public static double format(String value) {
		double parseDouble = 0;
		try {
			parseDouble = Double.parseDouble(value);
		} catch (Exception e) {
			parseDouble = 0;
		}
		return parseDouble;
	}

	public static String backspace0(Object o) {
		String s = Math.round(Double.parseDouble(o.toString()))+"";
//		String s = String.valueOf(o);
//		int index = s.indexOf(".");
//		if (index < 0) {
//			return s;
//		} else {
//			char c = s.charAt(s.length() - 1);
//			if (c == '0' || c == '.') {
//				s = s.substring(0, s.length() - 1);
//				return backspace0(s);
//			} else {
//				return s;
//			}
//		}
		return s;
	}
}
