/**
 * 
 */
package com.cntest.fxpt.etl;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.fxpt.etl.domain.RowLog;
import com.cntest.fxpt.etl.domain.RowSet;
import com.cntest.fxpt.etl.domain.StepLog;
import com.cntest.fxpt.etl.impl.NullStepHops;

/**
 * @author 刘海林 基本步骤实现方法
 */
public abstract class BaseStep implements IStep, IStepMetadata {
	private static final Logger log = LoggerFactory.getLogger(BaseStep.class);
	private String pk;
	private IEtlContext context;
	private String name;
	/**************** IStepMetadata ********/
	private IStepHops parentStepHops = new NullStepHops();
	private IStepHops childStepHops = new NullStepHops();
	private boolean isFinish = false;
	private int successRowNumber = 0;
	private int failRowNumber = 0;
	private int warnRowNumber = 0;
	private int ignoreRowNumber = 0;
	private StepLog stepLog = new StepLog();
	private HashMap<String, Object> stepParameter = new HashMap<String, Object>();

	public BaseStep(String name, IEtlContext context) {
		this.setContext(context);
		this.name = name;
	}

	public void setPk(String pk) {
		this.pk = pk;
	}

	/****************** IStep **************/
	@Override
	public IStepMetadata getStepMetadata() {
		return this;
	}

	@Override
	public void excuteStep() throws Exception {
		executeNextStep();
	}

	/***************** IStepMetadata **********************/

	@Override
	public HashMap<String, Object> getStepParameter() {
		return stepParameter;
	}

	@Override
	public String getPk() {
		return pk;
	}

	@Override
	public void begin() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void end() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void registerParentStepHops(IStepHops stepHops) {
		this.parentStepHops = stepHops;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void registerChildStepHops(IStepHops stepHops) {
		this.childStepHops = stepHops;
	}

	@Override
	public IStep getStep() {
		return this;
	}

	@Override
	public boolean isFinish() {
		return isFinish;
	}

	@Override
	public int getWarnRowNumber() {
		return warnRowNumber;
	}

	@Override
	public int getFailRowNumber() {
		return failRowNumber;
	}

	@Override
	public int getIgnoreRowNumber() {
		return ignoreRowNumber;
	}

	@Override
	public int getSuccessRowNumber() {
		return successRowNumber;
	}

	@Override
	public void putRow(RowSet row) {
		++successRowNumber;
		childStepHops.putRow(row);
	}

	@Override
	public RowSet getRow() {
		return parentStepHops.getRow();
	}

	@Override
	public StepLog getStepLog() {
		return stepLog;
	}

	@Override
	public IEtlContext getContext() {
		return context;
	}

	protected void setContext(IEtlContext context) {
		this.context = context;
		this.context.registerSetp(this);
	}

	protected void setFinish(boolean isFinish) {
		this.isFinish = isFinish;
	}

	protected void setSuccessRowNumber(int successRowNumber) {
		this.successRowNumber = successRowNumber;
	}

	protected void setFailRowNumber(int failRowNumber) {
		this.failRowNumber = failRowNumber;
	}

	protected void setWarnRowNumber(int warnRowNumber) {
		this.warnRowNumber = warnRowNumber;
	}

	protected void addLog(RowLog rowLog) {
		boolean isRecord = false;
		if (rowLog.isHasError()) {
			++failRowNumber;
			isRecord = true;
			stepLog.setHasError(true);
		}

		if (rowLog.isHasWarn()) {
			++warnRowNumber;
			isRecord = true;
			stepLog.setHasWarn(true);
		}

		if (rowLog.isHasIgnore()) {
			++ignoreRowNumber;
			isRecord = true;
			stepLog.setHasIgnore(true);
		}
		if (isRecord) {
			stepLog.addLog(rowLog.toString());
		}
	}

	public void setSetpParameter(String key, Object value) {
		stepParameter.put(key, value);
	}

	protected void executeNextStep() throws Exception {
		log.debug("执行下一个步骤");
		childStepHops.execSetp();
		log.debug("执行下一个步骤成功");
	}

}
