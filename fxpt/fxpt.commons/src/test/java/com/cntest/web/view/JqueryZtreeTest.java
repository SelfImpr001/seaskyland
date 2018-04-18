/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.web.view;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.web.view.JqueryZtree.ZtreeField;
import com.google.gson.Gson;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2015年8月24日
 * @version 1.0
 **/
public class JqueryZtreeTest {
	private Logger logger = LoggerFactory.getLogger(JqueryZtreeTest.class);
	@Test
	public void test()throws Exception{
		JqueryZtree root  = new JqueryZtree().isParent(true).name("root");
		ArrayList<JqueryZtree> children1 = genChildren(root, 10);
		assertNowId(children1.get(9),root);
		assertNowId(children1.get(0),root);
		assertNowId(children1.get(5),root);
		assertNowId(children1.get(9),children1.get(0));
		assertNowId(children1.get(9),children1.get(5));
		assertPid(children1.get(9),children1.get(0));
		assertPid(children1.get(5),children1.get(0));
		assertPid(children1.get(9),children1.get(5));
		assertPidId(children1.get(9),root);
		assertPidId(children1.get(0),root);
		assertPidId(children1.get(5),root);
		
		ArrayList<JqueryZtree> children2 = genChildren(children1.get(0), 10);
		assertNowId(children2.get(9),root);
		assertNowId(children2.get(0),root);
		assertNowId(children2.get(5),root);
		assertNowId(children2.get(9),children1.get(0));
		assertNowId(children2.get(9),children1.get(5));
		assertPid(children2.get(9),children2.get(0));
		assertPid(children2.get(5),children2.get(0));
		assertPid(children2.get(9),children2.get(5));
		assertPidId(children2.get(9),children1.get(0));
		assertPidId(children2.get(0),children1.get(0));
		assertPidId(children2.get(5),children1.get(0));
		
		ArrayList<JqueryZtree> children3 = genChildren(children1.get(9), 10);
		assertNowId(children3.get(9),root);
		assertNowId(children3.get(0),root);
		assertNowId(children3.get(5),root);
		assertNowId(children3.get(9),children1.get(0));
		assertNowId(children3.get(9),children1.get(5));
		assertPid(children3.get(9),children3.get(0));
		assertPid(children3.get(5),children3.get(0));
		assertPid(children3.get(9),children3.get(5));
		assertPidId(children3.get(9),children1.get(9));
		assertPidId(children3.get(0),children1.get(9));
		assertPidId(children3.get(5),children1.get(9));
		
		Gson gson = new Gson();
		logger.debug(gson.toJson(children3.get(5).getZtreeNode()));
		logger.debug(gson.toJson(children3.get(4).getZtreeNode()));
	}
	
	private void assertNowId(JqueryZtree node1,JqueryZtree node2) {
		assertEquals(node1.nowId(),node2.nowId());
	}
	
	private void assertPid(JqueryZtree node1,JqueryZtree node2) {
		assertEquals(node1.get(ZtreeField.pId),node2.get(ZtreeField.pId));
	}
	
	private void assertPidId(JqueryZtree node,JqueryZtree parent) {
		assertEquals(node.get(ZtreeField.pId),parent.get(ZtreeField.id));
	}
	
	private ArrayList<JqueryZtree> genChildren(JqueryZtree root,int count) {
		ArrayList<JqueryZtree> children = new ArrayList<JqueryZtree>(count);
		for(int i=0;i<count;i++) {
			children.add(root.genChild().name(root.get(ZtreeField.name)+"-"+i));
		}
		
		return children;
	}
}

