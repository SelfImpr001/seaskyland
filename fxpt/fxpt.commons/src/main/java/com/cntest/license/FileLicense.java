/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.license;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.util.ExceptionHelper;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆2014年10月9日
 * @version 1.0
 **/
public final class FileLicense implements License {
	private static Logger logger = LoggerFactory.getLogger(FileLicense.class);

	private File licenseFile;

	private String licenseContent;
	
	private LicenseCredentialsMatcher matcher;

	public FileLicense(LicenseCredentialsMatcher matcher,String path) {
		this.matcher = matcher;
		File licenseFile = new File(path);
		if (!licenseFile.exists())
			throw new LicenseException("License file not exists!");
		readLicenseContent(licenseFile);
	}

	@Override
	public void onAccess(LicenseInfo info, LicenseToken token) throws LicenseException {
		if(matcher == null)
			throw new LicenseException("License matcher not exists!");
		
		boolean b = matcher.doCredentialsMatch(this, token);
		if (b) {
			info.setAccessabled(true);
			info.setMessage("License Matched!");
		} else {
			info.setAccessabled(false);
			info.setMessage(token.getAccessDeniedMessage());
		}
	}

	private void readLicenseContent(File licenseFile) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(licenseFile)));
			String line = null;
			StringBuilder sb = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			if (sb.length() > 0)
				licenseContent = sb.toString();
			logger.info("This system's license is\n<===========\n{}\n===========>", licenseContent);
		} catch (Exception e) {

		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				logger.error(ExceptionHelper.trace2String(e));
			}
		}
	}
	
	@Override
	public Object getLicense() {
		return licenseContent;
	}
}
