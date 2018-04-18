package com.cntest.validate.fluid;

import java.util.ArrayList;
import java.util.List;

import com.cntest.data.Row;
import com.cntest.validate.RowMessage;
import com.cntest.validate.ValidateHandler;
import com.lmax.disruptor.EventFactory;

public class RowEvent {
    private Row row;
    
    private Boolean success = Boolean.TRUE;
    
    private ArrayList<RowMessage> messages = new ArrayList<>();
    
	public static final EventFactory<RowEvent> EVENTFACTORY = new EventFactory<RowEvent>() {

		@Override
		public RowEvent newInstance() {
			return new RowEvent();
		}
		
	};
	
	public void bindTo(Row row) {
		this.row = row;
	}
	
	public Row resultOf() {
		return this.row;
	}
	
	public List<RowMessage> messages(){
		return this.messages;
	}
	
	public void addErrorMessage(String message) {
		addMessage(message,RowMessage.MessageType.ERROR);
	}
	
	public void addMessage(String message,RowMessage.MessageType type) {
		messages.add(new RowMessage(message,type));
	}
	
	public void failure() {
		this.success = Boolean.FALSE;
	}
	
	public Boolean isSuccess() {
		return this.success;
	}
	
	public void on(ValidateHandler handler) {
		handler.onEvent(this);
	}
}
