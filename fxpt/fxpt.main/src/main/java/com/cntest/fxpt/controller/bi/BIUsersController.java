package com.cntest.fxpt.controller.bi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cntest.foura.domain.Role;
import com.cntest.foura.service.UserService;
import com.cntest.fxpt.bi.domain.BiInfo;
import com.cntest.fxpt.bi.domain.BiUser;
import com.cntest.fxpt.domain.SynUsers;
import com.cntest.fxpt.service.bi.BiService;
import com.cntest.fxpt.util.LogUtil;
import com.cntest.fxpt.util.TitleUtil;
import com.cntest.web.view.ModelAndViewBuilder;
import com.cntest.web.view.ResponseStatus;
import com.cntest.common.page.Page;

@Controller
@RequestMapping("/biUser")
public class BIUsersController {
	@Autowired(required = false)
	@Qualifier("BiService")
	private BiService biService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/biUsersList")
	public ModelAndView biUserList(String pagerMethod,HttpServletRequest request){
		List<BiUser> listBiUser = new ArrayList<BiUser>();
		listBiUser = biService.getAllBiUsers();
//		// 查询总共有多少记录
//		Integer rowCount= listBiUser.size();
//		page = pagerService.getPage(page,pagerMethod, rowCount);
//		// 分页查询记录
//		listBiUser = biService.findPageByBiUser(page);
//		page.setList(listBiUser);
//		modelAndView.addObject("page",page);
		String title = TitleUtil.getTitle(request.getServletPath());
		return ModelAndViewBuilder.newInstanceFor("/bi/biUser/biUsersList")
				.append("title",title)
				.append("listBiUser", listBiUser).append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("list")
						.msg("list").build()).build();
	}
	
	@RequestMapping("/addBiUser")
	public ModelAndView goAddBiUser(){
		List<BiInfo> listBiInfo = biService.getBiInfoList();
		return ModelAndViewBuilder.newInstanceFor("/bi/biUser/addBiUser").append("listBiInfo", listBiInfo).append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("")
				.msg("").build()).build();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView doAdd(@RequestBody BiUser biUser)throws Exception {
		String status = "失败",info = " ",erre="";
		try {
			BiInfo biInfo = new BiInfo();
			biInfo.setId(biUser.getBiInfo().getId());
			biUser.setBiInfo(biInfo);
			biService.addBiUser(biUser);
			status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			info="SmartBI用户：<b style='color:red;'>"+biUser.getUserName()+"</b>添加"+status+"</br>";
			LogUtil.log("系统设置>BI配置>BI用户设置", "添加",biUser.getUserName(),status,info,erre);
		}
		return ModelAndViewBuilder.newInstanceFor("").append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("")
				.msg("").build()).build();
	}
	@RequestMapping(method = RequestMethod.DELETE)
	public ModelAndView doDelete(@RequestBody BiUser biUser)throws Exception{
		biUser = biService.getBiUser(biUser.getId());
		String status = "失败",info = " ",erre="",name = biUser.getUserName();
		try {
			biService.deleteBiUser(biUser);
			status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			info="SmartBI用户：<b style='color:red;'>"+name+"</b>删除"+status+"</br>";
			LogUtil.log("系统设置>BI配置>BI用户设置", "删除",name,status,info,erre);
		}
		return ModelAndViewBuilder.newInstanceFor("").append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("")
				.msg("").build()).build();
	}
	
	@RequestMapping("/updateBiUser/{biUserid}")
	public ModelAndView goUpdate(@PathVariable int biUserid)throws Exception{
		BiUser biUser = new BiUser();
		biUser = biService.getBiUser(biUserid);
		List<BiInfo> listBiInfo = biService.getBiInfoList();
		return ModelAndViewBuilder.newInstanceFor("/bi/biUser/updateBiUser").append("listBiInfo", listBiInfo).append("biUser", biUser).append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("")
				.msg("").build()).build();
	}
	@RequestMapping(method = RequestMethod.PUT)
	public ModelAndView doUpdate(@RequestBody BiUser biUser)
			throws Exception {
		String status = "失败",info = " ",erre="";
		BiUser bi = biService.getBiUser(biUser.getId());
		try {
			info+= "</br>修改项</br>";
			info+= updatainfo(biUser,bi);
			userService.evictSession(bi);
			biService.updateBiUser(biUser);
			status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			LogUtil.log("系统设置>BI配置>BI用户设置", "修改",biUser.getUserName(),status, "同步用户 角色<b style='color:red;'>"+biUser.getUserName()+"</b>修改"+status+"</br>"+info,erre);
		}
		return ModelAndViewBuilder.newInstanceFor("").append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("")
				.msg("").build()).build();
	}
	
	public String updatainfo(BiUser resource,BiUser temp) {
		//resource:新来的数据，temp :之前的数据
		String info="";
		if(resource.getBiInfo().getId()!=null &&(!resource.getBiInfo().getId().equals(temp.getBiInfo().getId()))) {
			BiInfo biInfo = biService.getBiInfo(resource.getBiInfo().getId());
			info+="SmartBI用户：<b style='color:red;'>"+temp.getBiInfo().getName()+"</b> 改  <b style='color:red;'>"+biInfo.getName()+"</b><br/>";
		}
		if(resource.getUserName()!=null && (!resource.getUserName().equals(temp.getUserName()))) {
			info+="用户名：<b style='color:red;'>"+temp.getUserName()+"</b> 改  <b style='color:red;'>"+resource.getUserName()+"</b><br/>";
		}
		if(resource.getUserPassword()!=null && (!resource.getUserPassword().equals(temp.getUserPassword()))) {
			info+="密码：<b style='color:red;'>"+temp.getUserPassword()+"</b> 改  <b style='color:red;'>"+resource.getUserPassword()+"</b><br/>";
		}
		if(info.length()==0) {
			info="没有修改项";
		}
		return info;
	}
	
}
