<div class="page-content">
<#import "/commons/breadcrumbs.ftl" as breadcrumbs>
<#import "/commons/grid.ftl" as dataFieldGuid>
<#import "/commons/contentTop.ftl" as dataFieldContentTop>
<@breadcrumbs.breadcrumbs currentPath="${title!'导入字段管理'}">
<!-- 表单内容-->
 <#assign buttons=[{"text":"新增","id":"filed_create","class":"btn-primary","closeBtn":false} ] >
 <@dataFieldContentTop.contentTop title="${title!'导入字段管理'}" buttons=buttons/>
  <div class="row">
    <div class="col-xs-12 col-md-12 col-sm-12" >
      <div class="query-form clearfix down" style="overflow: visible;">
        <div class="form-side col-xs-12 col-md-12 col-sm-12">
          <form id="dataFieldform" class="form-horizontal " role="form">           
            <div class="form-group">           
              <label class="col-md-1 col-sm-1 control-label">所属数据项</label>
              <div class="col-md-3 col-sm-3">
                <select class="form-control selectpicker myData" id="dataCategoryId" name="dataCategoryId" data-width="200px">
                <#if dataCategorys??> <#list dataCategorys as dataCategory> 
                  <#if dataCategoryId == dataCategory.id>
                  <option value="${dataCategory.id}" selected>${dataCategory.name}</option> 
                  <#else>
                  <option value="${dataCategory.id}">${dataCategory.name}</option>
                   </#if> 
                </#list></#if>
                </select>
              </div>
              <div class="col-md-8 col-sm-8"/>
            </div>                                 
          </form>
        </div>
      </div>
    </div>
  </div>
  <#assign headers=["字段名称","别名","默认对应字段","是否必须导入","是否有效","排序号","所属数据项","字段描述","操作"]>
  <#assign rows=[]>
  <#assign buttons=[					
  					{"type":"text","title":"编辑","trigger":"update","class":"icon-edit text-success"},
  					{"type":"text","title":"删除","trigger":"remove","class":"icon-trash text-primary"}]>
  <#assign optBtn=[]>
  <#if dataFields??>
	<#list dataFields as dataField>
	  <#assign cells=[dataField.fieldName,dataField.asName,dataField.defaultName,dataField.need?string("是","不是"),dataField.valid?string("有效","无效"),dataField.sortNum,(dataField.dataCategory.name)!"无",(dataField.description)!"无"]>
	  <#assign rows=rows+[cells]>
	  <#assign optBtn=optBtn+[{"pk":dataField.id,"buttons":buttons}]>
	</#list>
  </#if>
  <@dataFieldGuid.grid headers=headers rows=rows buttons=optBtn pager=[] showPager=false hasFirstCheckbox=false showSeq=true idPreFix="dataField"/>
</@breadcrumbs.breadcrumbs>
</div>