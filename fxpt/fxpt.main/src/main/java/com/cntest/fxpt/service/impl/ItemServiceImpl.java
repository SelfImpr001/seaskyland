/*
 * @(#)com.cntest.fxpt.service.impl.ItemServiceImpl.java 1.0 2014年5月22日:下午4:21:38
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cntest.fxpt.bean.DataField;
import com.cntest.fxpt.domain.AnalysisTestpaper;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.Item;
import com.cntest.fxpt.domain.Subject;
import com.cntest.fxpt.domain.TestPaper;
import com.cntest.fxpt.repository.IAnalysisTestPaperDao;
import com.cntest.fxpt.repository.IExamDao;
import com.cntest.fxpt.repository.IITemDao;
import com.cntest.fxpt.repository.IShowMessageDao;
import com.cntest.fxpt.repository.ISubjectDao;
import com.cntest.fxpt.repository.ITestPaperDao;
import com.cntest.fxpt.service.IEtlLogService;
import com.cntest.fxpt.service.IItemService;
import com.cntest.fxpt.service.etl.IDataFieldService;
import com.cntest.fxpt.util.GroupUtil;
import com.cntest.fxpt.util.SaveEtlProcessResultToFile;
import com.cntest.fxpt.util.SystemConfig;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月22日 下午4:21:38
 * @version 1.0
 */
@Service("IItemService")
public class ItemServiceImpl implements IItemService {
  @Autowired(required = false)
  @Qualifier("IITemDao")
  private IITemDao itemDao;

  @Autowired(required = false)
  @Qualifier("ITestPaperDao")
  private ITestPaperDao testPaperDao;

  @Autowired(required = false)
  @Qualifier("IAnalysisTestPaperDao")
  private IAnalysisTestPaperDao analysisTestPaperDao;

  @Autowired(required = false)
  @Qualifier("IEtlLogService")
  private IEtlLogService etlLogService;

  @Autowired(required = false)
  @Qualifier("IExamDao")
  private IExamDao examDao;

  @Autowired(required = false)
  @Qualifier("ISubjectDao")
  private ISubjectDao subjectDao;

  @Autowired(required = false)
  @Qualifier("etl.IDataFieldService")
  private IDataFieldService dataFieldService;

  @Autowired(required = false)
  @Qualifier("IShowMessageDao")
  private IShowMessageDao showMessageDao;

  /*
   * (non-Javadoc)
   * 
   * @see com.cntest.fxpt.service.IItemService#deleteByTestPaperId(int)
   */
  @Override
  public void importFail(Long testPaperId) {
    itemDao.deleteItem(testPaperId);
    TestPaper testPaper = testPaperDao.get(testPaperId);
    testPaperDao.delete(testPaper);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.cntest.fxpt.service.IItemService#listByTestPaperId(int)
   */
  @Override
  public List<Item> listByTestPaperId(Long testPaperId) {
    return itemDao.list(testPaperId);
  }


  @Override
  public void importSuccess(Long examId, TestPaper testPaper) {
    long testPaperId = testPaper.getId();
    testPaperDao.updateTestPaperImportItemStatus(testPaperId, true);

    boolean isContainABpaper = itemDao.isContainABpaper(examId, testPaperId);

    createAnlaysisTestPaper(testPaperId);

    if ("wuhou".equals(SystemConfig.newInstance().getValue("area.org.code"))) {
      if (isContainABpaper) {
        testPaperDao.updateTestpaperStatus(testPaperId, examId);
        createAnlaysisTestPaperA(testPaperId);
        createAnlaysisTestPaperB(testPaperId);
      }
      createAnlaysisTestPaperSX(testPaperId);
    }

    // if("wuhou".equals(SystemConfig.newInstance().getValue(
    // "area.org.code"))){
    // String subjectstr = this.loadContainsABSubjects();
    // if(subjectstr.indexOf(testPaper.getName())!=-1){
    // createAnlaysisTestPaperA(testPaperId);
    // createAnlaysisTestPaperB(testPaperId);
    // }
    // createAnlaysisTestPaperSX(testPaperId);
    // }

    Exam exam = examDao.findById(examId);
    // 叠加明细表的条数（原来的）
    // exam.setImpItemCount(exam.getImpItemCount() + 1);
    // 叠加明细表的条数
    exam.setImpItemCount(examDao.getItemNumByExamid(examId));
    examDao.update(exam);
    // 查询当前系统的导入字段信息
    List<DataField> dataField = dataFieldService.list(6L);
    // 添加字段导入信息(保存最后一次的设置)
    showMessageDao.addMessageByExamid(examId, testPaperId, 2, dataField);


  }

  private void createAnlaysisTestPaper(Long testPaperId) {
    TestPaper testPaper = testPaperDao.get(testPaperId);
    List<Item> items = itemDao.list(testPaperId);
    Map<String, List<Item>> map = new HashMap<String, List<Item>>();

    HashMap<Long, List<Item>> subjectItemMap = new HashMap<Long, List<Item>>();
    HashMap<Long, Subject> subjectMap = new HashMap<Long, Subject>();

    double kgScore = 0;
    double zgScore = 0;
    for (Item item : items) {
      Long subjectId = item.getSubject().getId();
      List<Item> subjectItems = subjectItemMap.get(subjectId);
      if (!item.isChoice()) {
        if (subjectItems == null) {
          subjectItems = new ArrayList<Item>();
          subjectItemMap.put(subjectId, subjectItems);
          subjectMap.put(subjectId, item.getSubject());
        }
        subjectItems.add(item);

        if (item.getOptionType() == 0) {
          zgScore += item.getFullScore();
        } else {
          kgScore += item.getFullScore();
        }
      } else {
        if (map.containsKey(item.getChoiceGroup())) {
          map.get(item.getChoiceGroup()).add(item);
        } else {
          List<Item> list = new ArrayList<Item>();
          list.add(item);
          map.put(item.getChoiceGroup(), list);
        }

      }
    }
    if (!map.isEmpty()) {
      List<Item> choiceitems = groupChoiceCount(map);
      for (Item item : choiceitems) {

        if (item.getOptionType() == 0) {
          zgScore += item.getFullScore();
        } else {
          kgScore += item.getFullScore();
        }
      }
    }

    List<AnalysisTestpaper> analysisTestpapers = new ArrayList<AnalysisTestpaper>();

    AnalysisTestpaper analysisTestpaper = new AnalysisTestpaper();
    analysisTestpaper.setExam(testPaper.getExam());
    analysisTestpaper.setName(testPaper.getName());
    analysisTestpaper.setFullScore(kgScore + zgScore);
    analysisTestpaper.setKgScore(kgScore);
    analysisTestpaper.setZgScore(zgScore);
    analysisTestpaper.setTestPaper(testPaper);
    // if("wuhou".equals(SystemConfig.newInstance().getValue(
    // "area.org.code"))){
    // analysisTestpaper.setComposite(true);
    // }
    if (subjectMap.size() == 1) {
      Subject subject = subjectMap.values().iterator().next();
      // 英语科目满分写死150，主观题和客观题按照原来的（2018-2-2 乔一行，朱烨，刘倩倩提的需求）
      if (subject.getId() == 2) {
        analysisTestpaper.setFullScore(150);
      }
      analysisTestpaper.setSubject(subject);
    } else {
      Subject subject = subjectDao.findSubjectListWith(testPaper.getName());
      analysisTestpaper.setSubject(subject);
      analysisTestpaper.setComposite(true);
    }
    analysisTestpapers.add(analysisTestpaper);

    if (subjectItemMap.size() > 1) {
      for (Long subjectId : subjectItemMap.keySet()) {
        List<Item> subjectItems = subjectItemMap.get(subjectId);
        double subjectKGScore = 0;
        double subjectZGScore = 0;
        for (Item item : subjectItems) {
          if (item.getOptionType() == 0) {
            subjectZGScore += item.getFullScore();
          } else {
            subjectKGScore += item.getFullScore();
          }
        }

        if (!map.isEmpty()) {
          List<Item> choiceitems = groupChoiceCount(map);
          choiceitems = choiceitems.stream().filter(c -> c.getSubject().getId() == subjectId)
              .collect(Collectors.toList());
          for (Item item : choiceitems) {
            if (item.getOptionType() == 0) {
              subjectZGScore += item.getFullScore();
            } else {
              subjectKGScore += item.getFullScore();
            }
          }
        }

        Subject subject = subjectMap.get(subjectId);
        analysisTestpaper = new AnalysisTestpaper();
        analysisTestpaper.setExam(testPaper.getExam());
        analysisTestpaper.setName(subject.getName());
        analysisTestpaper.setFullScore(subjectKGScore + subjectZGScore);
        analysisTestpaper.setKgScore(subjectKGScore);
        analysisTestpaper.setZgScore(subjectZGScore);
        analysisTestpaper.setTestPaper(testPaper);
        analysisTestpaper.setSubject(subject);
        analysisTestpaper.setSplitSubject(true);
        if ("wuhou".equals(SystemConfig.newInstance().getValue("area.org.code"))) {
          analysisTestpaper.setSplitSubject(true);
        }
        analysisTestpapers.add(analysisTestpaper);
      }
    }

    for (AnalysisTestpaper tmp : analysisTestpapers) {
      analysisTestPaperDao.add(tmp);
      if (tmp.getSubject() != null) {
        itemDao.updateItemAnaysisTestPaperId(tmp);
      }
    }

  }

  public List<Item> groupChoiceCount(Map<String, List<Item>> map) {
    List<Item> result = new ArrayList<Item>();
    for (List<Item> itemlist : map.values()) {
      // 找到这一组选做题是选几 num
      int num = 0;
      if (!itemlist.isEmpty()) {
        String choicenum = itemlist.get(0).getChoiceNumber().split("\\|")[0];
        num = Integer.parseInt(choicenum);
      }

      // 第二次分组，把不同给分点的小题分为一组，一组即为一个题目
      Map<String, List<Item>> itemmap =
          new GroupUtil().group(itemlist, new GroupUtil.Groupby<String>() {
            @Override
            public String groupby(Object obj) {
              Item i = (Item) obj;
              return i.getChoiceNumber().split("\\|")[1];
            }
          });

      int m = 0;
      for (List<Item> il : itemmap.values()) {
        List<Item> ss = il;
        if (m < num) {
          for (Item item : ss) {
            result.add(item);
          }
        } else {
          break;
        }
        m++;
      }

    }
    return result;
  }

  private void createAnlaysisTestPaperA(Long testPaperId) {
    TestPaper testPaper = testPaperDao.get(testPaperId);
    List<Item> items = itemDao.list(testPaperId);

    HashMap<Long, List<Item>> subjectItemMap = new HashMap<Long, List<Item>>();
    HashMap<Long, Subject> subjectMap = new HashMap<Long, Subject>();

    double kgScore = 0;
    double zgScore = 0;
    for (Item item : items) {
      if ("A".equals(item.getPaper())) {
        Long subjectId = item.getSubject().getId();
        List<Item> subjectItems = subjectItemMap.get(subjectId);
        if (subjectItems == null) {
          subjectItems = new ArrayList<Item>();
          subjectItemMap.put(subjectId, subjectItems);
          subjectMap.put(subjectId, item.getSubject());
        }
        subjectItems.add(item);

        if (item.getOptionType() == 0) {
          zgScore += item.getFullScore();
        } else {
          kgScore += item.getFullScore();
        }
      }
    }

    List<AnalysisTestpaper> analysisTestpapers = new ArrayList<AnalysisTestpaper>();

    AnalysisTestpaper analysisTestpaper = new AnalysisTestpaper();
    analysisTestpaper.setExam(testPaper.getExam());
    analysisTestpaper.setName(testPaper.getName() + "A");
    analysisTestpaper.setFullScore(kgScore + zgScore);
    analysisTestpaper.setKgScore(kgScore);
    analysisTestpaper.setZgScore(zgScore);
    analysisTestpaper.setTestPaper(testPaper);
    if ("wuhou".equals(SystemConfig.newInstance().getValue("area.org.code"))) {
      analysisTestpaper.setComposite(true);
    }

    if (subjectMap.size() == 1) {
      Subject subject = subjectMap.values().iterator().next();
      analysisTestpaper.setSubject(subject);
    } else {
      Subject subject = subjectDao.findSubjectListWith(testPaper.getName());
      analysisTestpaper.setSubject(subject);
      analysisTestpaper.setComposite(true);
    }
    analysisTestpapers.add(analysisTestpaper);

    if (subjectItemMap.size() > 1) {
      for (Long subjectId : subjectItemMap.keySet()) {
        List<Item> subjectItems = subjectItemMap.get(subjectId);
        double subjectKGScore = 0;
        double subjectZGScore = 0;
        for (Item item : subjectItems) {
          if (item.getOptionType() == 0) {
            subjectZGScore += item.getFullScore();
          } else {
            subjectKGScore += item.getFullScore();
          }
        }

        Subject subject = subjectMap.get(subjectId);
        analysisTestpaper = new AnalysisTestpaper();
        analysisTestpaper.setExam(testPaper.getExam());
        analysisTestpaper.setName(subject.getName());
        analysisTestpaper.setFullScore(subjectKGScore + subjectZGScore);
        analysisTestpaper.setKgScore(subjectKGScore);
        analysisTestpaper.setZgScore(subjectZGScore);
        analysisTestpaper.setTestPaper(testPaper);
        analysisTestpaper.setSubject(subject);
        analysisTestpaper.setSplitSubject(true);
        analysisTestpapers.add(analysisTestpaper);
      }
    }

    for (AnalysisTestpaper tmp : analysisTestpapers) {
      analysisTestPaperDao.add(tmp);
    }

  }

  private void createAnlaysisTestPaperB(Long testPaperId) {
    TestPaper testPaper = testPaperDao.get(testPaperId);
    List<Item> items = itemDao.list(testPaperId);

    HashMap<Long, List<Item>> subjectItemMap = new HashMap<Long, List<Item>>();
    HashMap<Long, Subject> subjectMap = new HashMap<Long, Subject>();

    double kgScore = 0;
    double zgScore = 0;
    for (Item item : items) {
      if ("B".equals(item.getPaper())) {
        Long subjectId = item.getSubject().getId();
        List<Item> subjectItems = subjectItemMap.get(subjectId);
        if (subjectItems == null) {
          subjectItems = new ArrayList<Item>();
          subjectItemMap.put(subjectId, subjectItems);
          subjectMap.put(subjectId, item.getSubject());
        }
        subjectItems.add(item);

        if (item.getOptionType() == 0) {
          zgScore += item.getFullScore();
        } else {
          kgScore += item.getFullScore();
        }
      }
    }

    List<AnalysisTestpaper> analysisTestpapers = new ArrayList<AnalysisTestpaper>();

    AnalysisTestpaper analysisTestpaper = new AnalysisTestpaper();
    analysisTestpaper.setExam(testPaper.getExam());
    analysisTestpaper.setName(testPaper.getName() + "B");
    analysisTestpaper.setFullScore(kgScore + zgScore);
    analysisTestpaper.setKgScore(kgScore);
    analysisTestpaper.setZgScore(zgScore);
    analysisTestpaper.setTestPaper(testPaper);
    if ("wuhou".equals(SystemConfig.newInstance().getValue("area.org.code"))) {
      analysisTestpaper.setComposite(true);
    }
    if (subjectMap.size() == 1) {
      Subject subject = subjectMap.values().iterator().next();
      analysisTestpaper.setSubject(subject);
    } else {
      Subject subject = subjectDao.findSubjectListWith(testPaper.getName());
      analysisTestpaper.setSubject(subject);
      analysisTestpaper.setComposite(true);
    }
    analysisTestpapers.add(analysisTestpaper);

    if (subjectItemMap.size() > 1) {
      for (Long subjectId : subjectItemMap.keySet()) {
        List<Item> subjectItems = subjectItemMap.get(subjectId);
        double subjectKGScore = 0;
        double subjectZGScore = 0;
        for (Item item : subjectItems) {
          if (item.getOptionType() == 0) {
            subjectZGScore += item.getFullScore();
          } else {
            subjectKGScore += item.getFullScore();
          }
        }

        Subject subject = subjectMap.get(subjectId);
        analysisTestpaper = new AnalysisTestpaper();
        analysisTestpaper.setExam(testPaper.getExam());
        analysisTestpaper.setName(subject.getName());
        analysisTestpaper.setFullScore(subjectKGScore + subjectZGScore);
        analysisTestpaper.setKgScore(subjectKGScore);
        analysisTestpaper.setZgScore(subjectZGScore);
        analysisTestpaper.setTestPaper(testPaper);
        analysisTestpaper.setSubject(subject);
        analysisTestpaper.setSplitSubject(true);
        analysisTestpapers.add(analysisTestpaper);
      }
    }

    for (AnalysisTestpaper tmp : analysisTestpapers) {
      analysisTestPaperDao.add(tmp);
    }

  }

  private void createAnlaysisTestPaperSX(Long testPaperId) {
    TestPaper testPaper = testPaperDao.get(testPaperId);
    List<Item> items = itemDao.list(testPaperId);

    HashMap<Long, List<Item>> subjectItemMap = new HashMap<Long, List<Item>>();
    HashMap<Long, Subject> subjectMap = new HashMap<Long, Subject>();

    double kgScoreA = 0;
    double zgScoreA = 0;
    double kgScoreB = 0;
    double zgScoreB = 0;
    for (Item item : items) {

      Long subjectId = item.getSubject().getId();
      List<Item> subjectItems = subjectItemMap.get(subjectId);
      if (subjectItems == null) {
        subjectItems = new ArrayList<Item>();
        subjectItemMap.put(subjectId, subjectItems);
        subjectMap.put(subjectId, item.getSubject());
      }
      subjectItems.add(item);

      if ("A".equals(item.getPaper())) {
        if (item.getOptionType() == 0) {
          kgScoreA += item.getFullScore();
        } else {
          zgScoreA += item.getFullScore();
        }
      } else {
        if (item.getOptionType() == 0) {
          kgScoreB += item.getFullScore();
        } else {
          zgScoreB += item.getFullScore();
        }

      }
    }



    List<AnalysisTestpaper> analysisTestpapers = new ArrayList<AnalysisTestpaper>();

    AnalysisTestpaper analysisTestpaper = new AnalysisTestpaper();
    analysisTestpaper.setExam(testPaper.getExam());
    analysisTestpaper.setName(testPaper.getName() + "SX");
    double sxScore = SaveEtlProcessResultToFile.convertSubjectScoreToSX(kgScoreA + zgScoreA,
        kgScoreB + zgScoreB, testPaper.getName());
    analysisTestpaper.setFullScore(sxScore);
    analysisTestpaper.setKgScore(kgScoreA + kgScoreB);
    analysisTestpaper.setZgScore(zgScoreA + zgScoreB);
    analysisTestpaper.setTestPaper(testPaper);

    if ("wuhou".equals(SystemConfig.newInstance().getValue("area.org.code"))) {
      analysisTestpaper.setComposite(true);
    }
    if (subjectMap.size() == 1) {
      Subject subject = subjectMap.values().iterator().next();
      analysisTestpaper.setSubject(subject);
    } else {
      Subject subject = subjectDao.findSubjectListWith(testPaper.getName());
      analysisTestpaper.setSubject(subject);
      analysisTestpaper.setComposite(true);
    }
    analysisTestpapers.add(analysisTestpaper);

    if (subjectItemMap.size() > 1) {
      for (Long subjectId : subjectItemMap.keySet()) {
        List<Item> subjectItems = subjectItemMap.get(subjectId);
        double subjectKGScore = 0;
        double subjectZGScore = 0;
        for (Item item : subjectItems) {
          if (item.getOptionType() == 0) {
            subjectZGScore += item.getFullScore();
          } else {
            subjectKGScore += item.getFullScore();
          }
        }

        Subject subject = subjectMap.get(subjectId);
        analysisTestpaper = new AnalysisTestpaper();
        analysisTestpaper.setExam(testPaper.getExam());
        analysisTestpaper.setName(subject.getName());
        analysisTestpaper.setFullScore(subjectKGScore + subjectZGScore);
        analysisTestpaper.setKgScore(subjectKGScore);
        analysisTestpaper.setZgScore(subjectZGScore);
        analysisTestpaper.setTestPaper(testPaper);
        analysisTestpaper.setSubject(subject);
        analysisTestpaper.setSplitSubject(true);
        analysisTestpapers.add(analysisTestpaper);
      }
    }

    for (AnalysisTestpaper tmp : analysisTestpapers) {
      analysisTestPaperDao.add(tmp);
    }

  }

  private String loadContainsABSubjects() {
    String filed = SystemConfig.newInstance().getValue("containsABSubject");
    return filed;
  }


  @Override
  public List<Item> listByAnlaysisTestPaperId(Long AnlaysisTestPaperId) {
    return itemDao.listByAnlaysisTestPaperId(AnlaysisTestPaperId);
  }

  @Override
  public String findPaperByItemId(Long itemId, Long examid, String studentid) {
    return itemDao.findPaperByItemId(itemId, examid, studentid);
  }

  @Override
  public String findOptionTypeByItemId(Long itemId, Long examid, String studentid) {
    return itemDao.findOptionTypeByItemId(itemId, examid, studentid);
  }

  @Override
  public String findPaperTotalScoreByItemId(Long examid, String studentid, Long testpaperid,
      String paper) {
    return itemDao.findPaperTotalScoreByItemId(examid, studentid, testpaperid, paper);
  }

  public String findRespectiveScoreByItemId(Long testpaperid, Long examid, String optiontype,
      String studentid, String paper) {
    return itemDao.findRespectiveScoreByItemId(testpaperid, examid, optiontype, studentid, paper);
  }

  @Override
  public boolean validate(String subjectName, String examId) {
    return itemDao.validate(subjectName, examId);
  }

}
