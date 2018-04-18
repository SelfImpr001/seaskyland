package com.cntest.fxpt.service.etl;

import java.util.HashMap;
import java.util.List;

import com.cntest.fxpt.bean.DataField;


public interface IDataFieldService {

  public void add(DataField dataField);

  public void delete(DataField dataField);

  public void update(DataField dataField);

  public List<DataField> list();

  public List<DataField> list(Long dataCategoryId);

  public DataField findById(Long dataFieldId);

  public List<DataField> list(int schemeType, Long testPaperId);

  public List<DataField> list(int schemeType, boolean isValid);

  public boolean hasFiled(DataField dataField);

  /**
   * 得要需要显示与否的Map列表
   * 
   * @param type
   * @return
   */
  public HashMap getDateByType(String type);

  /**
   * 获取明细表考试名称的字段说明
   * 
   * @param schemeType 类型
   * @param fieldName 字段名称
   * @return
   */
  public String getTestPaperDescription(int schemeType, String fieldName);

  /**
   * 验证导入字段与设置必须导入子段是否匹配
   * 
   * @param listHead
   * @param schemeType
   * @return
   */
  public String initDataHead(List<String> listHead, int schemeType);

}
