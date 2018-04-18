<div class="page-content">
<#import "/commons/breadcrumbs.ftl" as breadcrumbs>
<#import "/commons/grid.ftl" as dataTransFormGuid>
<#import "/commons/contentTop.ftl" as dataTransFormContentTop>
<@breadcrumbs.breadcrumbs currentPath="${title!'数据导入配置管理'}">
<!-- 表单内容-->
 <#assign buttons=[{"text":"新增","id":"trans_create","class":"btn-primary","closeBtn":false} ] >
 <@dataTransFormContentTop.contentTop title="${title!'数据导入配置管理'}" buttons=buttons/>
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
  <#assign headers=["名称","类型","是否有效","所属数据项","操作"]>
  <#assign rows=[]>
  <#assign buttons=[					
  					{"type":"text","title":"编辑","trigger":"update","class":"icon-edit text-success"},
  					{"type":"text","title":"删除","trigger":"remove","class":"icon-trash text-primary"}]>
  <#assign optBtn=[]>
  <#if dataTransforms??>
	<#list dataTransforms as dataTransform>
	    <#if dataTransform.type==1>
             <#assign text="验证">
        <#elseif dataTransform.type==2>
              <#assign text="转换">
        </#if>		
	  <#assign cells=[dataTransform.name,text,dataTransform.valid?string("有效","无效"),(dataTransform.dataCategory.name)!"无"]>
	  <#assign rows=rows+[cells]>
	  <#assign optBtn=optBtn+[{"pk":dataTransform.id,"buttons":buttons}]>
	</#list>
  </#if>
  <@dataTransFormGuid.grid headers=headers rows=rows buttons=optBtn pager=[] showPager=false hasFirstCheckbox=false showSeq=true idPreFix="dataTransForm"/>
</@breadcrumbs.breadcrumbs>
</div>