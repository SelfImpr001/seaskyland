package com.cntest.fxpt.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cntest.fxpt.domain.ApplySet;
import com.cntest.fxpt.repository.IApplySetDao;
import com.cntest.fxpt.service.IApplySetService;

@Service("IApplySetService")
public class ApplySetServiceImpl implements IApplySetService {
	@Autowired(required = false)
	@Qualifier("IApplySetDao")
	private IApplySetDao applySetDao;

	@Override
	public List<ApplySet> findAllApply() {
		return applySetDao.findAllApply();
	}

	@Override
	public void update(ApplySet applySet) {
		applySetDao.update(applySet);
	}

	@Override
	public void delete(ApplySet applySet) {
		applySetDao.delete(applySet);
	}

	@Override
	public void save(ApplySet applySet) {
		applySetDao.save(applySet);
	}

	@Override
	public ApplySet get(Long id) {
		return applySetDao.get(id);
	}

	@Override
	public List<ApplySet> findApplyByStatus(String status) {
		return applySetDao.findApplyByStatus(status);
	}

	@Override
	public List<ApplySet> findApplyByName(String systemName) {
		return applySetDao.findApplyByName(systemName);
	}

	@Override
	public void justAddImage(List<ApplySet> applyList, String type,ApplySet applySet) {
		ApplySet apply=null;
		//存在更新，不存在新增  type表示是 登录界面图标还是操作页面图标
		if(applyList.size()>0){
			apply=applyList.get(0);
			if("login".equalsIgnoreCase(type)){
				apply.setLoginIcon(applySet.getLoginIcon());
			}else{
				apply.setHandleIcon(applySet.getHandleIcon());
			}
			applySetDao.updateByApply(apply);
		}else{
			applySetDao.addApply(applySet);
		}
	}

	@Override
	public void deleteByStatus(String status) {
		applySetDao.deleteByStatus(status);
	}

}
