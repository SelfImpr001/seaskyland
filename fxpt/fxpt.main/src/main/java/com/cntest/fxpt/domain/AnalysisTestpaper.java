package com.cntest.fxpt.domain;

import java.util.List;

public class AnalysisTestpaper {
  private Long id;
  private String name;
  private boolean isComposite;
  private double fullScore;
  private double kgScore;
  private double zgScore;
  private int paperType;
  private Exam exam;
  private Subject subject;
  private Long subjectId;
  private TestPaper testPaper;
  private CombinationSubject combinationSubject;
  private boolean isSplitSubject = false;
  private List<Item> items;


  public Long getSubjectId() {
    if (this.subject != null) {
      return this.subject.getId();
    }
    return subjectId;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public double getFullScore() {
    return fullScore;
  }

  public double getKgScore() {
    return kgScore;
  }

  public double getZgScore() {
    return zgScore;
  }

  public Exam getExam() {
    return exam;
  }

  public Subject getSubject() {
    return subject;
  }

  public boolean isComposite() {
    return isComposite;
  }

  public void setComposite(boolean isComposite) {
    this.isComposite = isComposite;
  }

  public CombinationSubject getCombinationSubject() {
    return combinationSubject;
  }

  public void setCombinationSubject(CombinationSubject combinationSubject) {
    this.combinationSubject = combinationSubject;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setFullScore(double fullScore) {
    this.fullScore = fullScore;
  }

  public void setKgScore(double kgScore) {
    this.kgScore = kgScore;
  }

  public void setZgScore(double zgScore) {
    this.zgScore = zgScore;
  }

  public void setExam(Exam exam) {
    this.exam = exam;
  }

  public void setSubject(Subject subject) {
    this.subject = subject;
  }

  public TestPaper getTestPaper() {
    return testPaper;
  }

  public void setTestPaper(TestPaper testPaper) {
    this.testPaper = testPaper;
  }

  public List<Item> getItems() {
    return items;
  }

  public void setItems(List<Item> items) {
    this.items = items;
  }

  public int getPaperType() {
    return paperType;
  }

  public void setPaperType(int paperType) {
    this.paperType = paperType;
  }

  public boolean isSplitSubject() {
    return isSplitSubject;
  }

  public void setSplitSubject(boolean isSplitSubject) {
    this.isSplitSubject = isSplitSubject;
  }

  @Override
  public String toString() {
    return id + "-->" + name + "-->" + paperType;
  }

}
