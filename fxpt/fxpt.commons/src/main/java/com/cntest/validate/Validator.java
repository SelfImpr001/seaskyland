package com.cntest.validate;

import com.cntest.data.DataSet;
import com.cntest.validate.fluid.RowEvent;
import com.google.common.base.Function;

/**
 * 规则验证，验证时按组进行
 * 
 * @author liguiqing
 *
 */
public interface Validator {

	/**
	 * 待验证的数据集
	 * @param dataSet
	 * @return
	 */
	public Validator input(DataSet dataSet);
	
	/**
	 * 创建一组新验证规则，连续调用时按顺序生成调用规则组，执行验证时按调用顺序进行
	 **/
	public Validator handlerWith(ValidateHandler...handlers);
	
	/**
	 * 增加一组验证规则，连续调用时按顺序增加，执行验证时按调用顺序进行
	 * @param handlerGroups
	 * @return
	 */
	public Validator handlerWith(HandlerGroup...handlerGroups);
	
	/**
	 * 执行验证，执行过程中，其他api都将失效，无法再改变验证执行过程
	 */
	public void validate();
	
	/**
	 * 中断验证执行
	 */
	public void interrupt();
	
	/**
	 * 验证成功后执行的回调
	 * @param function
	 * @return
	 */
	public Validator onSuccess(Function<RowEvent,Boolean> function);
	
	public Validator onComplete(Function<RowEvent,Boolean> function);
}
