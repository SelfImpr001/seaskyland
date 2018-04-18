<div class="page-content">

<!-- 索引栏-->
<#import "../commons/breadcrumbs.ftl" as breadcrumbs>
<@breadcrumbs.breadcrumbs currentPath="${title!'组织管理'}">  
<!-- 表单内容-->

<!-- 表单头-->
<#import "../commons/contentTop.ftl" as orgContentTop>
<#assign buttons=[{"text":"新增","id":"addBtn"},{"text":"导入组织","id":"report"},{"text":"组织模板下载","id":"temDownLoad"}] >
<@orgContentTop.contentTop title="${title!'组织管理'}"  buttons=buttons />
<!-- 表单体-->
<#import "../commons/grid.ftl" as orgGrid>
<#assign headers=["组织代号","组织名称","上级组织","所属机构","下级组织","对应人数","操作"]>
<#assign rows=[]>
<#assign buttons=[
				  {"type":"text","title":"修改","trigger":"update","class":"icon-edit text-success"},
				  {"type":"text","title":"导出","trigger":"orgOut","class":"icon-cloud-download text-primary"},
				  {"type":"text","title":"删除","trigger":"remove","class":"icon-trash text-danger"}]>
<#assign optBtn=[]>
<#if orgList??>
    <#list orgList![] as org>
      <#assign typeText="省教育厅">
      <#if org.type=2>
      　　　　<#assign typeText="市教育局">
      <#elseif org.type=3>
      　　　　<#assign typeText="县（区）教育局">
      <#elseif org.type=4>
      　　　　<#assign typeText="学校">
      </#if>
      
      <#if org.orgCount != 0>
        <#assign cells=[org.code!"",org.name!"",(org.parent.name)!"",typeText,"<a href='javascript:void(0);' pk='"+org.pk+"' orgName='"+org.name+"' trigger='nextOrg' count='"+org.orgCount+"'>"+org.orgCount+"</a>","<a href='javascript:void(0);' pk='"+org.pk+"' orgName='"+org.name+"' trigger='orglist' count='"+org.userCount+"'>"+org.userCount!""+"</a>"]>
      <#else>
        <#assign cells=[org.code!"",org.name!"",(org.parent.name)!"",typeText," ","<a href='javascript:void(0);' pk='"+org.pk+"' orgName='"+org.name+"' trigger='orglist' count='"+org.userCount+"'>"+org.userCount!""+"</a>"]>
      </#if>
     
      <#assign rows=rows+[cells]>
      <#assign optBtn=optBtn+[{"pk":org.pk,"buttons":buttons}]>
    </#list>
</#if>
<@orgGrid.grid  headers=headers rows=rows buttons=optBtn pager=[] showPager=false hasFirstCheckbox=false showSeq=false showTree=true idPreFix="orgTree" hasNested=true>
      <div class="query-form clearfix down" style="overflow: visible;">
        <div class="form-side col-xs-10 col-md-10 col-sm-10">
          <form class="form-horizontal " role="form">
			<div class="form-group  ">
			  <label class="col-md-1 col-sm-1 control-label">组织信息</label>
			  <div class="col-md-11 col-sm-11">
			    <input type="text" class="form-control" name="orgName" placeholder="请输入组织名称或组织代码">
			  </div>	
			</div>            
          </form>
        </div>
        <div class="form-side col-xs-2 col-md-2 col-sm-2">          
          <a class="btn btn-success icon-search" herf="javascript:void(0);" title="查询"></a>
          <a class="btn btn-success icon-undo" herf="javascript:void(0);" title="清空"></a>          
        </div>
      </div>
</@orgGrid.grid>
</@breadcrumbs.breadcrumbs>
</div>