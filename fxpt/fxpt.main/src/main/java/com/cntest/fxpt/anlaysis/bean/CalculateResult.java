/*
 * @(#)com.cntest.fxpt.anlaysis.bean.CalculateResult.java	1.0 2014年11月28日:下午2:29:10
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.bean;

import java.util.ArrayList;
import java.util.List;

import com.cntest.fxpt.anlaysis.uitl.Container;
import com.cntest.fxpt.domain.Item;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年11月28日 下午2:29:10
 * @version 1.0
 */
public class CalculateResult {
	private TargetResultContainer totalScoreTargetResults = new TargetResultContainer();
	private List<ScoreInfo> totalScoreScoreInfo = new ArrayList<>();
	private Container<Long, TargetResultContainer> itemTargetResult = new Container<>();
	private Container<String, TargetResultContainer> abilityTargetResult = new Container<>();
	private Container<String, TargetResultContainer> knowledgeContentTargetResult = new Container<>();
	private Container<String, TargetResultContainer> knowledgeTargetResult = new Container<>();
	private Container<String, TargetResultContainer> titleTypeTargetResult = new Container<>();

	private ArrayList<TargetResultContainer> results = new ArrayList<>();

	public Container<Long, TargetResultContainer> getItemTargetResult() {
		return itemTargetResult;
	}

	public Container<String, TargetResultContainer> getAbilityTargetResult() {
		return abilityTargetResult;
	}

	public Container<String, TargetResultContainer> getKnowledgeContentTargetResult() {
		return knowledgeContentTargetResult;
	}

	public Container<String, TargetResultContainer> getKnowledgeTargetResult() {
		return knowledgeTargetResult;
	}

	public Container<String, TargetResultContainer> getTitleTypeTargetResult() {
		return titleTypeTargetResult;
	}

	public void setItemTargetResult(
			Container<Long, TargetResultContainer> itemTargetResult) {
		this.itemTargetResult = itemTargetResult;
	}

	public void setAbilityTargetResult(
			Container<String, TargetResultContainer> abilityTargetResult) {
		this.abilityTargetResult = abilityTargetResult;
	}

	public void setKnowledgeContentTargetResult(
			Container<String, TargetResultContainer> knowledgeContentTargetResult) {
		this.knowledgeContentTargetResult = knowledgeContentTargetResult;
	}

	public void setKnowledgeTargetResult(
			Container<String, TargetResultContainer> knowledgeTargetResult) {
		this.knowledgeTargetResult = knowledgeTargetResult;
	}

	public void setTitleTypeTargetResult(
			Container<String, TargetResultContainer> titleTypeTargetResult) {
		this.titleTypeTargetResult = titleTypeTargetResult;
	}

	public TargetResultContainer getTotalScoreTargetResults() {
		return totalScoreTargetResults;
	}

	public List<ScoreInfo> getTotalScoreScoreInfo() {
		return totalScoreScoreInfo;
	}

	public void setTotalScoreTargetResults(
			TargetResultContainer totalScoreTargetResults) {
		this.totalScoreTargetResults = totalScoreTargetResults;
	}

	public void setTotalScoreScoreInfo(List<ScoreInfo> totalScoreScoreInfo) {
		this.totalScoreScoreInfo = totalScoreScoreInfo;
	}

	public TargetResultContainer getItemTargetResult(Item item) {
		TargetResultContainer result = itemTargetResult.get(item.getId());
		if (result == null) {
			result = new TargetResultContainer();
			itemTargetResult.put(item.getId(), result);
		}
		return result;
	}

	public TargetResultContainer getAbilityTargetResult(ItemGroup itemGroup) {
		TargetResultContainer result = abilityTargetResult.get(itemGroup
				.getName());
		if (result == null) {
			result = new TargetResultContainer();
			abilityTargetResult.put(itemGroup.getName(), result);
		}
		return result;
	}

	public TargetResultContainer getKnowledgeContentTargetResult(
			ItemGroup itemGroup) {
		TargetResultContainer result = knowledgeContentTargetResult
				.get(itemGroup.getName());
		if (result == null) {
			result = new TargetResultContainer();
			knowledgeContentTargetResult.put(itemGroup.getName(), result);
		}
		return result;
	}

	public TargetResultContainer getKnowledgeTargetResult(ItemGroup itemGroup) {
		TargetResultContainer result = knowledgeTargetResult.get(itemGroup
				.getName());
		if (result == null) {
			result = new TargetResultContainer();
			knowledgeTargetResult.put(itemGroup.getName(), result);
		}
		return result;
	}

	public TargetResultContainer getTitleTypeTargetResult(ItemGroup itemGroup) {
		TargetResultContainer result = titleTypeTargetResult.get(itemGroup
				.getName());
		if (result == null) {
			result = new TargetResultContainer();
			titleTypeTargetResult.put(itemGroup.getName(), result);
		}
		return result;
	}

	public List<TargetResultContainer> getResults() {
		return results;
	}

	public void setResults(ArrayList<TargetResultContainer> results) {
		this.results = results;
	}

	public void addResult(TargetResultContainer result) {
		this.results.add(result);
	}

}
