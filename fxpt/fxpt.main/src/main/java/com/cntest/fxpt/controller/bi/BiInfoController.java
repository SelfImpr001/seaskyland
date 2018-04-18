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

import com.cntest.exception.BusinessException;
import com.cntest.foura.domain.Role;
import com.cntest.foura.service.UserService;
import com.cntest.fxpt.bi.BIConnectorPool;
import com.cntest.fxpt.bi.BIConnectorPoolMgr;
import com.cntest.fxpt.bi.domain.BiInfo;
import com.cntest.fxpt.bi.domain.BiUser;
import com.cntest.fxpt.domain.SynUsers;
import com.cntest.fxpt.service.bi.BiService;
import com.cntest.fxpt.util.LogUtil;
import com.cntest.fxpt.util.TitleUtil;
import com.cntest.common.page.Page;
import com.cntest.web.view.ModelAndViewBuilder;
import com.cntest.web.view.ResponseStatus;

@Controller
@RequestMapping("/biInfo")
public class BiInfoController {

	@Autowired(required = false)
	@Qualifier("BiService")
	private BiService biService;

	@Autowired(required = false)
	@Qualifier("bi.BIConnectorPoolMgr")
	private BIConnectorPoolMgr biConnectorPoolMgr;
	
	@Autowired
	private UserService userService;

	@RequestMapping("/biInfoList")
	public ModelAndView biInfoList(String pagerMethod,HttpServletRequest request) {
		List<BiInfo> listBiInfo = new ArrayList<BiInfo>();
		listBiInfo = biService.getBiInfoList();
		// // 查询总共有多少记录
		// Integer rowCount = listBiInfo.size();
		// page = pagerService.getPage(page, pagerMethod, rowCount);
		// // 分页查询记录
		// listBiInfo = biService.findPageByBiInfo(page);
		// page.setList(listBiInfo);
		// modelAndView.addObject("page", page);
		String title = TitleUtil.getTitle(request.getServletPath());
		return ModelAndViewBuilder.newInstanceFor("/bi/biInfo/biInfoList")
				.append("title",title)
				.append("listBiInfo", listBiInfo).append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("list")
						.msg("list").build()).build();
	}

	@RequestMapping("/addBiInfo")
	public ModelAndView goAddBiInfo() {
		return ModelAndViewBuilder.newInstanceFor("/bi/biInfo/addBiInfo").append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("")
				.msg("").build()).build();
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView doAdd(@RequestBody BiInfo biInfo) throws Exception {
		String status = "失败",info = " ",erre="";
		try {
			biService.addBiInfo(biInfo);
			status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			info="SmartBI用户：<b style='color:red;'>"+biInfo.getName()+"</b>添加"+status+"</br>";
			LogUtil.log("系统设置>BI配置>BI系统设置", "添加",biInfo.getName(),status,info,erre);
		}
		return ModelAndViewBuilder.newInstanceFor("").append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("")
				.msg("").build()).build();
	}

//	@RequestMapping(method = RequestMethod.DELETE)
//	public ModelAndView doDelete(@RequestBody BiInfo biInfo) throws Exception {
//		List<BiUser> listBiUser = biService.getBiUsersByBiInfoId(biInfo.getId());
//		if (listBiUser != null && listBiUser.size() > 0) {
//			throw new BusinessException("","已有绑定信息，无法删除");
//		} else {
//			biService.deleteBiInfo(biInfo);
//		}
//		return ModelAndViewBuilder.newInstanceFor("").append(ResponseStatus.NAME,
//				new ResponseStatus.Builder(Boolean.TRUE).code("")
//				.msg("").build()).build();
//	}

	@RequestMapping("/updateBiInfo/{biInfoid}")
	public ModelAndView goUpdate(@PathVariable int biInfoid) throws Exception {
		BiInfo biInfo = new BiInfo();
		biInfo = biService.getBiInfo(biInfoid);
		return ModelAndViewBuilder.newInstanceFor("/bi/biInfo/updateBiInfo").append("biInfo", biInfo).append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("")
				.msg("").build()).build();
	}

	@RequestMapping(method = RequestMethod.PUT)
	public ModelAndView doUpdate(@RequestBody BiInfo biInfo) throws Exception {
		String status = "失败",info = " ",erre="";
		BiInfo bi = biService.getBiInfo(biInfo.getId());
		try {
			info+= "</br>修改项</br>";
			info+= updatainfo(biInfo,bi);
			userService.evictSession(bi);
			biService.updateBiInfo(biInfo);
			status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			LogUtil.log("系统设置>BI配置>BI系统设置", "修改",biInfo.getName() ,status, "SmartBI用户<b style='color:red;'>"+biInfo.getName()+"</b>修改"+status+"</br>"+info,erre);
		}
		return ModelAndViewBuilder.newInstanceFor("").append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("")
				.msg("").build()).build();
	}
	public String updatainfo(BiInfo resource,BiInfo temp) {
		//resource:新来的数据，temp :之前的数据
		String info="";
		if(resource.getName()!=temp.getName()) {
			info+="SmartBI用户：<b style='color:red;'>"+temp.getName()+"</b> 改  <b style='color:red;'>"+resource.getName()+"</b><br/>";
		}
		if(resource.getUrl()!=temp.getUrl()) {
			info+="地址：<b style='color:red;'>"+temp.getUrl()+"</b> 改  <b style='color:red;'>"+resource.getUrl()+"</b><br/>";
		}
		if(resource.getRemark()!=temp.getRemark()) {
			info+="描述：<b style='color:red;'>"+temp.getRemark()+"</b> 改  <b style='color:red;'>"+resource.getRemark()+"</b><br/>";
		}
		if(info.length()==0) {
			info="没有修改项";
		}
		return info;
	}
	@RequestMapping("/cleanPool")
	public ModelAndView cleanPool() throws Exception {
		String status = "失败",info = " ",erre="";
		try {
			biConnectorPoolMgr.getBIConnectorPool().clean();
			status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			info="操作"+status+"</br>";
			LogUtil.log("系统设置>BI配置>BI系统设置", "清空连接池","",status,info,erre);
		}
		return ModelAndViewBuilder.newInstanceFor("").append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("")
				.msg("").build()).build();
	}

}
