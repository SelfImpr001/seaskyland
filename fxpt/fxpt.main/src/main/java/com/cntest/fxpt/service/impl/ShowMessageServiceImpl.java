
package com.cntest.fxpt.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.cntest.fxpt.bean.DataField;
import com.cntest.fxpt.domain.ShowMessage;
import com.cntest.fxpt.repository.IShowMessageDao;
import com.cntest.fxpt.service.IShowMessageService;

@Repository("IShowMessageService")
public class ShowMessageServiceImpl  implements
IShowMessageService {
	private static final Logger log = LoggerFactory
			.getLogger(ShowMessageServiceImpl.class);
	
	@Autowired(required = false)
	@Qualifier("IShowMessageDao")
	private IShowMessageDao showMessageDao;

	@Override
	public void deleteMessageByExamid(Long examid,int showType,Long testPaperId) {
		showMessageDao.deleteMessageByExamid(examid,showType,testPaperId);
	}

	@Override
	public void addMessageByExamid(Long examid,Long testPaperId ,int showType, List<DataField> dataField) {
		showMessageDao.addMessageByExamid(examid, testPaperId,showType, dataField);		
	}

	@Override
	public List<ShowMessage> findShowMessageByExamid(Long examid,Long testPaperId, int showType) {
		return showMessageDao.findShowMessageByExamid(examid, testPaperId,showType);
	}

	
}
