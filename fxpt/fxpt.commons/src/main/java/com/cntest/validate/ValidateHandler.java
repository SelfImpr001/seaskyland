package com.cntest.validate;

import com.cntest.validate.fluid.RowEvent;

public interface ValidateHandler {
	public void onEvent(RowEvent row);
}
