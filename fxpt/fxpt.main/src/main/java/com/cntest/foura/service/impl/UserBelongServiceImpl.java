/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.service.impl;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cntest.common.service.AbstractEntityService;
import com.cntest.exception.BusinessException;
import com.cntest.foura.domain.Organization;
import com.cntest.foura.domain.User;
import com.cntest.foura.domain.UserBelong;
import com.cntest.foura.repository.OrganizationRepository;
import com.cntest.foura.repository.UserBelongRepository;
import com.cntest.foura.repository.UserRepository;
import com.cntest.foura.service.UserBelongService;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 肖 肖 2014年6月18日
 * @version 1.0
 **/
@Transactional
@Service
public class UserBelongServiceImpl extends AbstractEntityService<UserBelong,Long> implements UserBelongService {
	private static Logger logger = LoggerFactory.getLogger(UserBelongServiceImpl.class);
	
	@Autowired
	private UserBelongRepository userBelongRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrganizationRepository orgRepository;
	
	public UserBelongServiceImpl() {
	}
	@Autowired
	public UserBelongServiceImpl(UserBelongRepository userBelongRepository) {
		this.userBelongRepository = userBelongRepository;
		this.setRepository(userBelongRepository);
	}
	@Override
	@Transactional
	public List<UserBelong> findBelongFor(User user) {
		logger.info("根据用户user {}  查找组织关系 ",user);
		return userBelongRepository.getBelongByUserPk(user);
	}
	@Override
	@Transactional
	public void updateBelongFor(User user, List<Organization> orgs) {
		int  i = userBelongRepository.removeBelongByUser(user);
		logger.info("({} row(s) affected)",i);
		
		for (Organization organization : orgs) {
			UserBelong userBelong = new UserBelong(user, organization);
			logger.info("UserBelong {}",userBelong.getPk());
			userBelongRepository.save(userBelong);
		}
	}
	@Override
	public String[] getBelongCodes(String userName) {
		User user = userRepository.findSameUser(userName);
		List<UserBelong> belongs =  findBelongFor(user);
		String[] belongCodes = null;
		if(belongs != null && belongs.size() >0) {
			belongCodes = new String[belongs.size()];
			int i = 0;
			for(UserBelong ub :belongs) {
				belongCodes[i++] = ub.getOrg().getCode();
			}			
		}
		return belongCodes;
	}
	@Override
	public void deleteBelongUser(Long id) {
		userBelongRepository.deleteBelongUser(id);
	}
	@Override
	public List getUserByOrg(String orgList) throws BusinessException {
		return userBelongRepository.getUserByOrg(orgList);
	}
}

