/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.fxpt.domain.spec.examinee;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.common.specification.AndSpecificationBuilder;
import com.cntest.common.specification.Specification;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年11月4日
 * @version 1.0
 **/
public class StudentSpecificationBuilder {
	private static final Logger loggr = LoggerFactory.getLogger(StudentSpecificationBuilder.class);

	public static Specification build(String...specs) {
		String[] fullSpecs = new String[specs.length];
		StudentEqualsSpecification spec = new StudentEqualsSpecification();
		int i = 0;
		for(String specname:specs) {
			spec.addSpecification(specname);
		}
		return spec;
//		for(String specname:specs) {
//			fullSpecs[i++] = "com.cntest.fxpt.domain.spec.examinee."+specname;
//		}			
//		return spec;
		//return AndSpecificationBuilder.build(fullSpecs);
	}
	
	public static Specification build(List<String> specs) {
		String[] fullSpecs = new String[specs.size()];
		int i = 0;
		for(String specname:specs) {
			fullSpecs[i++] = "com.cntest.fxpt.domain.spec.examinee."+specname;
		}			
		
		return AndSpecificationBuilder.build(fullSpecs);
	}
}

