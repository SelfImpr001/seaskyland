package com.cntest.web.view.freemarker.tags;

import java.io.IOException;

import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.TemplateException;

import com.cntest.web.view.freemarker.tags.shiro.ShiroTags;

public class ShiroTagFreeMarkerConfiguer extends FreeMarkerConfigurer {
	@Override
	public void afterPropertiesSet() throws IOException,TemplateException {
		super.afterPropertiesSet();
		this.getConfiguration().setSharedVariable("shiro", new ShiroTags()); 
	}
}
