package com.cntest.validate.fluid;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.data.DataSet;
import com.cntest.data.Row;
import com.cntest.util.Executor;
import com.cntest.validate.ArrayHandlerGrop;
import com.cntest.validate.HandlerGroup;
import com.cntest.validate.ValidateHandler;
import com.cntest.validate.Validator;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.Util;

/**
 * 二给表格数据异步流式验证器，某个规则验证失败，不影响其他规则的执行
 * 组：规则没有先后顺序，可以入到同一组规则中；有先后顺序就必须放到不同的组中，验证按先进先出执行
 * @author liguiqing
 *
 */
public class GridFluidValidator implements Validator {
	private final static Logger logger = LoggerFactory.getLogger(GridFluidValidator.class);
	
	private Disruptor<RowEvent> disruptor;
	
	private DataSet waittingToDo;
	
	private ArrayList<HandlerGroup> handlerGroups = new ArrayList<>() ;
	
	private Boolean isInterrupt =  Boolean.TRUE;
	
	private Function<RowEvent,Boolean> successFunction ;
	
	private Function<RowEvent,Boolean> completeFunction ;
	
	public GridFluidValidator(ValidateHandler...handlers ) {
		Preconditions.checkNotNull(handlers,"验证处理器不能空");
		this.handlerWith(handlers);
	}
	
	public Validator handlerWith(ValidateHandler...handlers) {
		if(handlers != null) {
			ArrayHandlerGrop grop = new ArrayHandlerGrop(handlers);
			this.handlerGroups .add(grop);
		}
		return this;
	}
	

	@Override
	public Validator handlerWith(HandlerGroup... handlerGroups) {
		if(handlerGroups != null) {
			for(HandlerGroup group:handlerGroups) {
				this.handlerGroups.add(group);
			}
		}
		return this;
	}
	
	@Override
	public Validator input(DataSet dataSet) {
		this.waittingToDo = dataSet;
		return this;
	}

	@Override
	public void validate() {
		if(this.disruptor != null)
			return;
///Util.ceilingNextPowerOfTwo(1024)
		this.disruptor = new Disruptor<>(RowEvent.EVENTFACTORY,
				Util.ceilingNextPowerOfTwo(1024), Executor.getInstance().getExecutorService(),
				ProducerType.SINGLE, new YieldingWaitStrategy());
		
		EventHandlerGroup<RowEvent> eventGroup = null;
		for(HandlerGroup group:this.handlerGroups) {
			if(eventGroup == null) {				
				eventGroup = this.disruptor.handleEventsWith(this.createrEventEventHandlers(group));
			}else {
				eventGroup = eventGroup.handleEventsWith(this.createrEventEventHandlers(group));
			}
		}
		
		if(this.successFunction != null) {
			eventGroup = eventGroup.then(new EventHandler<RowEvent>() {

				@Override
				public void onEvent(RowEvent event, long sequence, boolean endOfBatch) throws Exception {
					if(event.isSuccess()) {
						successFunction.apply(event);
						logger.debug("Row {} handler success",event.resultOf());						
					}
				}
			});		
		}
		
		if(this.completeFunction != null) {
			eventGroup = eventGroup.then(new EventHandler<RowEvent>() {

				@Override
				public void onEvent(RowEvent event, long sequence, boolean endOfBatch) throws Exception {
					completeFunction.apply(event);
					logger.debug("Row {} handler complete",event.resultOf());						
					
				}
			});		
		}

		this.disruptor.start();
		
		Executor.getInstance().getExecutorService().execute(new Runnable() {

			@Override
			public void run() {
				Row nextRow = null;
				while((nextRow = waittingToDo.next()) !=null) {
					if(!isInterrupt)
						break;
					final Row row = nextRow;

					disruptor.publishEvent(new EventTranslator<RowEvent> () {
						@Override
						public void translateTo(RowEvent event, long sequence) {
							event.bindTo(row);
						}
					});
				}
				//disruptor.shutdown();
				
				System.out.println("**************************************************");
			}});
	}
	
	private EventHandler<RowEvent>[] createrEventEventHandlers(HandlerGroup group) {
        List<ValidateHandler> handlers = group.handlers();
		EventHandler<RowEvent> [] evenHandlers = new EventHandler[handlers.size()];
		int index = 0;
		for(final ValidateHandler vh: handlers) {
			evenHandlers[index++] = new EventHandler<RowEvent>() {

				@Override
				public void onEvent(RowEvent event, long sequence, boolean endOfBatch) throws Exception {
					logger.debug("validate with handler:{}" ,vh);
					event.on(vh);
				}
			};
		}
		
		return evenHandlers;
	}

	@Override
	public Validator onSuccess(Function<RowEvent,Boolean> function) {
		this.successFunction = function;
		return this;
	}

	@Override
	public void interrupt() {
		this.isInterrupt = Boolean.FALSE;
	}

	@Override
	public Validator onComplete(Function<RowEvent,Boolean> function) {
		this.completeFunction = function;
		return this;
	}
	
	public void shutDown(){
		if(this.disruptor!=null)
		disruptor.shutdown();
	}

}
