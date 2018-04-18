<#import "../commons/breadcrumbs.ftl" as breadcrumbs>
<#import "../commons/grid.ftl" as userGrid>
<#import "../commons/contentTop.ftl" as userContentTop>
<div class="page-content">
<@breadcrumbs.breadcrumbs currentPath="${title!'用户管理'}">
<!-- 表单内容  {"text":"批量授权","trigger":"batchGrant"} 删除批量授权功能-->
  
  <#assign buttons=[{"text":"新增","trigger":"create"},{"text":"批量删除","trigger":"moreDelete"}] >
  <#if currentUser.name == "admin">
  	<#-- {"text":"用户同步","trigger":"synchronous"}, -->
  	<#assign buttons=[{"text":"新增","trigger":"create"},{"text":"批量删除","trigger":"moreDelete"}] >
  </#if>
  <@userContentTop.contentTop title="${title!'用户列表'}" buttons=buttons/>
  
  <#assign headers=["用户名","姓名","手机","邮箱","状态","操作"]>
  <#assign pager={"curpage":query.curpage,"totalpage":query.totalpage,"pagesize":query.pagesize}>
  <#assign rows=[]>
  <!--{"type":"text","title":"权限分配","trigger":"permission","class":"btn btn-xs btn-success mr-2 "}, 删除权限分配功能	 
  	{"type":"text","title":"指定考试","trigger":"permissionForexam","class":"btn btn-xs btn-success mr-2 "},
  
  
  -->
  
  
  <#assign buttons1=[{"type":"text","title":"修改密码","trigger":"updatePassword","class":"icon-pencil text-success"},
  					{"type":"text","title":"修改","trigger":"update","class":"icon-edit text-primary"}, 				
  					{"type":"text","title":"用户详情","trigger":"userDetails","class":"icon-search text-danger"}]>
  					
  <#assign buttons=[{"type":"text","title":"修改密码","trigger":"updatePassword","class":"icon-pencil text-success"},
  					{"type":"text","title":"修改","trigger":"update","class":"icon-edit  text-primary"},
  					{"type":"text","title":"删除","trigger":"remove","class":"icon-trash text-danger"}, 				
  					{"type":"text","title":"用户详情","trigger":"userDetails","class":"icon-search text-warning"}]>  					
  <#assign optBtn=[]>
  <#assign isAdmin={ }>
  <#if query.results??>
    <#list query.results as user>
      <#assign statusText="可用">
      <#if user.status=2>
        <#assign statusText="禁用">
      </#if>
	  <#if user.status=3>
        <#assign statusText="锁定">
      </#if>      
      <#assign cells=[user.pk,user.name,user.userInfo.realName,user.userInfo.cellphone!"--",user.userInfo.email!"--",statusText]>
      <#assign rows=rows+[cells]>
      <#if user.type=1>
        <#assign optBtn=optBtn+[{"pk":user.pk,"buttons":buttons1}]>
         <#assign isAdmin=isAdmin+{user.pk:user.pk}>
      <#else>
        <#assign optBtn=optBtn+[{"pk":user.pk,"buttons":buttons}]>
      </#if>
    </#list>
  </#if>
  <@userGrid.grid  headers=headers rows=rows hasNested=true
    buttons=optBtn pager=pager showPager=true hasFirstCheckbox=true showSeq=false idPreFix="user" isAdmin = isAdmin>
      <div class="query-form clearfix down" style="overflow: visible;">
        <div class="form-side col-xs-10 col-md-10 col-sm-10">
          <form class="form-horizontal " role="form">
			<div class="form-group  ">
			  <div class="col-md-12 col-sm-12">
			    <input type="text" class="form-control" name="q1" placeholder="请输入查询条件">
			  </div>	  
			</div>            
          </form>
        </div>
        <div class="form-side col-xs-2 col-md-2 col-sm-2">          
          <a class="btn btn-success " herf="javascript:void(0);" title="查询">查询</a>
          <a class="btn btn-success " herf="javascript:void(0);" title="重置">清空</a>          
        </div>
      </div>  
  </@userGrid.grid>
</@breadcrumbs.breadcrumbs>
</div>