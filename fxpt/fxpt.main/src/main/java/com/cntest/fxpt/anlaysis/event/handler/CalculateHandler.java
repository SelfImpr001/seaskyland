/*
 * @(#)com.cntest.fxpt.anlaysis.service.impl.CalculateHandler.java	1.0 2014年11月28日:上午9:16:02
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.event.handler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.fxpt.anlaysis.bean.CalculateResult;
import com.cntest.fxpt.anlaysis.bean.CalculateTask;
import com.cntest.fxpt.anlaysis.bean.SubjectScoreContainer;
import com.cntest.fxpt.anlaysis.calculate.ICalcluator;
import com.cntest.fxpt.anlaysis.calculate.impl.AbilityCjCalculate;
import com.cntest.fxpt.anlaysis.calculate.impl.ItemCjCalculate;
import com.cntest.fxpt.anlaysis.calculate.impl.KnowledgeCjCalculate;
import com.cntest.fxpt.anlaysis.calculate.impl.KnowledgeContentCjCalculate;
import com.cntest.fxpt.anlaysis.calculate.impl.SubjectCjCalculate;
import com.cntest.fxpt.anlaysis.calculate.impl.TitleTypeCjCalculate;
import com.cntest.fxpt.anlaysis.filter.IStudentCjFilter;
import com.cntest.fxpt.anlaysis.filter.WLFilter;
import com.cntest.fxpt.anlaysis.uitl.OrgProxy;
import com.cntest.fxpt.domain.AnalysisTestpaper;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.Item;
import com.cntest.util.ExceptionHelper;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年11月28日 上午9:16:02
 * @version 1.0
 */
public class CalculateHandler implements EventHandler<CalculateTask>,
		WorkHandler<CalculateTask> {
	private static final Logger log = LoggerFactory
			.getLogger(CalculateHandler.class);

	@Override
	public void onEvent(CalculateTask event) throws Exception {
		OrgProxy op = new OrgProxy(event.getObj());

		IStudentCjFilter filter = event.getContext().getStatRankFilter(
				event.getAnalysisTestpaper());
		SubjectScoreContainer ssc = event.getSubjectScoreContainer()
				.getSubjectScoreContainer(filter);

		Exam exam = event.getContext().getExam();
		AnalysisTestpaper atp = event.getAnalysisTestpaper();

		try {
			if (ssc.isNotEmpty()) {
				calucate(event, atp.getPaperType(), ssc);
			}
			if (atp.getPaperType() == 0 && exam.isWlForExamStudent()
					&& !op.isClazz()) {
				SubjectScoreContainer tmpssc = ssc
						.getSubjectScoreContainer(new WLFilter(1));
				if (tmpssc.isNotEmpty()) {
					calucate(event, 1, tmpssc);
				}
				tmpssc = ssc.getSubjectScoreContainer(new WLFilter(2));
				if (tmpssc.isNotEmpty()) {
					calucate(event, 2, tmpssc);
				}
			}
		} catch (Exception e) {
			log.error(event.toString() + "==>>"
					+ ExceptionHelper.trace2String(e));
			e.printStackTrace();
		}

		log.debug("计算完毕" + event.toString());
	}

	private void calucate(CalculateTask event, int wl, SubjectScoreContainer ssc) {
		CalculateResult cr = event.getCalculateResult(wl);
		ICalcluator calcluator = null;
		List<Item> items = event.getAnalysisTestpaper().getItems();
		if (items != null && !items.isEmpty()) {
			calcluator = new ItemCjCalculate();
			calcluator.calculate(event, cr, ssc);
			calcluator = new AbilityCjCalculate();
			calcluator.calculate(event, cr, ssc);
			calcluator = new KnowledgeCjCalculate();
			calcluator.calculate(event, cr, ssc);
			calcluator = new KnowledgeContentCjCalculate();
			calcluator.calculate(event, cr, ssc);
			calcluator = new TitleTypeCjCalculate();
			calcluator.calculate(event, cr, ssc);
		}

		calcluator = new SubjectCjCalculate();
		calcluator.calculate(event, cr, ssc);
	}

	@Override
	public void onEvent(CalculateTask event, long arg1, boolean arg2)
			throws Exception {
		this.onEvent(event);
	}

}
