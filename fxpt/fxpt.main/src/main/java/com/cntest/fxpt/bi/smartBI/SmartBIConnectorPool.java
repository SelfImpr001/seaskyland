/**
 * 
 */
package com.cntest.fxpt.bi.smartBI;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import smartbi.sdk.ClientConnector;

import com.cntest.fxpt.bi.BIConnectionDAO;
import com.cntest.fxpt.bi.BIConnectorPool;
import com.cntest.fxpt.bi.domain.BiUser;

/**
 * @author Administrator
 * 
 */
@Component("bi.BIConnectorPool")
public class SmartBIConnectorPool implements BIConnectorPool {
	private static final Logger log = LoggerFactory
			.getLogger(SmartBIConnectorPool.class);
	@Autowired(required = false)
	@Qualifier("bi.BIConnectionDAO")
	private BIConnectionDAO dao;

	private ArrayBlockingQueue<SmartBIConnector> pools = new ArrayBlockingQueue<SmartBIConnector>(
			100);
	private ReentrantLock lock = new ReentrantLock();

	@Override
	public SmartBIConnector getConnector() {
		SmartBIConnector connector = null;
		lock.lock();
		try {
			if (pools.isEmpty()) {
				this.initSmartBIConnectors();
			}

			connector = new SmartBIConnector();
			connector = pools.poll();

//			if (!connector.isValid()) {
				connector.open();
//			}
			pools.add(connector);
		} finally {
			lock.unlock();
		}
		return connector;
	}

	// @PostConstruct
	public void initSmartBIConnectors() {
		log.debug("连接smartBI。。。");
		List<BiUser> users = dao.getBiUsers();
		log.debug("连接smartBi用户数:" + users.size());
		for (int i = 0; i < users.size(); i++) {
			BiUser biUser = users.get(i);
			log.debug("连接smartBi-------->" + biUser.getUserName());
			pools.add(createConnector(biUser.getUserName(),
					biUser.getUserPassword(), biUser.getBiInfo().getUrl()));
		}
		log.debug("连接smartBI完毕");
	}

	public SmartBIConnector createConnector(String user, String password,
			String smartBiURL) {
		SmartBIConnector smartBIConnector = new SmartBIConnector();
		smartBIConnector.setUrl(smartBiURL);
		smartBIConnector.setPassword(password);
		smartBIConnector.setUserName(user);
		;
		smartBIConnector.open();

		return smartBIConnector;
	}

	@PreDestroy
	public void closeSmartBiConnector() {
		log.debug("注销与smartBI的连接....");
		for (int i = 0; i < pools.size(); i++) {
			SmartBIConnector smartBIConnector = pools.poll();
			if (smartBIConnector != null) {
				smartBIConnector.getClientConnector().close();
			}
		}
		log.debug("注销与smartBI的连接完毕");
	}

	@Override
	public void clean() {
		lock.lock();
		try {
			closeSmartBiConnector();
		} finally {
			lock.unlock();
		}
	}

}
