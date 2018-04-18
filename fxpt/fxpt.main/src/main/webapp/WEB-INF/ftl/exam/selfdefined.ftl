<#import "../commons/breadcrumbs.ftl" as breadcrumbs>

<#assign reportCatalogs=[{"id":"231","name":"综合汇总表"}]>
<#assign indexes=[{"formulaType":"AVG","name":"平均分","fixed":"2"},{"formulaType":"STD","name":"标准差","fixed":"2"},
{"formulaType":"MIN","name":"最低分","fixed":"0"},{"formulaType":"MAX","name":"最高分","fixed":"0"},
{"formulaType":"DFL","name":"得分率","fixed":"2"},{"formulaType":"SKRS","name":"实考人数","fixed":"0"}]>
<div class="page-content">
<link rel="stylesheet" href="${request.contextPath}/static/resources/css/jquery.treetable.css"/>
<link rel="stylesheet" href="${request.contextPath}/static/resources/css/jquery.treetable.theme.default.css"/>
<style>
.self-form{
  background:none;
  border:0px;
  border-top:solid #CCC 1px ;
  padding:0px;
}

.self-form ul {
  margin-top:5px;
}

.self-form ul li{
  float:left;
  margin:2px 50px 4px 0px;
}

.self-form ul li a{
  text-decoration:none;
  padding: 1px 6px;
}

.self-form .control-label{
  padding-right:-1px;
}

.self-form .form-horizontal .control-label{
  text-align:left;
}
.self-form .form-horizontal .form-group{
  margin-bottom:8px;
}

.form-tree{
  padding-left:0px;
}

.defined,.self-form ul li a:hover{
  background-color:#fae5e5;
  color:#c7254e;
  border-radius:4px;
}

.self-form ul li a:hover{
  background-color:#c4dcf4;
}

.table.treetable thead tr th{
  text-align: center;
}

.multi-checked{
  border-color:#449D44;
  color:#449D44;
  margin-top: 6px;
}

.multi-check,.analyse{
  margin-top: 6px;
}
</style>
<@breadcrumbs.breadcrumbs currentPath="${title!'自定义分析报表'}">  
  <#import "../commons/contentTop.ftl" as top>
  <@top.contentTop title="${title!'自定义分析报表'}" />
  <@panel multi=false>
    <@formgroup label="考试" btn={"drawed":false} itemName="EXAM">
      <#if exams??>
	    <#list exams as ex><li><a href="javascript:void(0);" class="<#if ex.id=exam.id>defined</#if>" data-tt-type="exam" data-tt-value="${ex.id}" >${ex.name}</a></li></#list>
	  </#if>
    </@formgroup>
  </@panel>	

  <@panel multi=false>
    <@formgroup label="报表" btn={"drawed":false} itemName="REPORT">
	  <#if reportCatalogs??>
	    <#list reportCatalogs as catalog><li><a href="javascript:void(0);" class="<#if catalog_index=0>defined</#if>" data-value="${catalog.id}">${catalog.name}</a></li></#list>
	  </#if>
    </@formgroup>
  </@panel>	  

  <@panel multi=true>
    <@formgroup label="学科" itemName="testPapers">
	  <#if papers??>
	    <#list papers as paper><li><a href="javascript:void(0);" data-value="${paper.id}">${paper.name}</a></li></#list>
	  </#if>
    </@formgroup>
    <div class="form-group  form-tree ">
      <label class="col-md-12 col-sm-12 control-label">考生:</label>
    </div>
    <@formgroup label="性别" labelStyle="text-align: right;" btn={"drawed":false} itemName="GENDER">	  
	  <li><a href="javascript:void(0);" data-value="1">男</a></li>
	  <li><a href="javascript:void(0);" data-value="2">女</a></li>	  
    </@formgroup>
    <#if nations??>
    <@formgroup label="民族" labelStyle="text-align: right;" btn={"drawed":false} itemName="NATION">	  
	  <#list nations as nation><li><a href="javascript:void(0);" data-value="${nation}">${nation}</a></li></#list>
    </@formgroup> 
    </#if>        

    <#if exam.wlForExamStudent>
      <@formgroup label="文理科" labelStyle="text-align: right;" btn={"drawed":false} itemName="WL">	  
	   <li><a href="javascript:void(0);" data-value="1">文科</a></li>
	   <li><a href="javascript:void(0);" data-value="2">理科</a></li>	  
      </@formgroup> 
    </#if>
    
    <input type="hidden" name="rootName" value="${rootEdu.code}">
    <input type="hidden" name="rootCode" value="${rootEdu.name}">
    <input type="hidden" name="rootLevel" id="rootLevel"  value="${exam.levelCode}">              
	<@eduform parentEdu=rootEdu edus=rootEdu.childs levelCode=exam.levelCode+1 display="block"/>	        
  </@panel>	

  <@panel multi=true >
    <@formgroup label="分析指标" itemName="Formula" btn={"type":"btn-success","icon":"icon-cogs","class":"analyse","text":"分析","drawed":true}>
     <#if indexes??> 
       <#list indexes as index>
         <li><a href="javascript:void(0);" data-value="${index.formulaType}" fixed="${index.fixed}">${index.name}</a></li>
       </#list> 
     </#if>
    </@formgroup>
  </@panel>	
 
  <div class="row" id = "dataRow">
    <h3 id="reportTitle" style="text-align:center;"></h3>
    <input type="text" name="datastatus" id="datastatus" style="display:none;">
    <div class="col-xs-12 " id="gridContainer">
      <table class="table table-bordered  table-hover" id="datagrid">
        <thead>
          <tr>
            <!--<th>a</th><th>b</th><th>c</th><th>d</th><th>e</th>-->
          </tr>
        </thead>
        <tbody>
          <!--
          <tr data-tt-id="1" >
            <td>a1</td><td>b1</td><td>c1</td><td>d1</td><td>e1</td>
          </tr>
          <tr data-tt-id="1-1"  data-tt-parent-id="1">
            <td>a11</td><td>b11</td><td>c11</td><td>d11</td><td>e11</td>
          </tr>
          <tr data-tt-id="1-1"  data-tt-parent-id="1">
            <td>a12</td><td>b12</td><td>c12</td><td>d12</td><td>e12</td>
          </tr>
          <tr data-tt-id="2" >
            <td>a2</td><td>b2</td><td>c2</td><td>d2</td><td>e2</td>
          </tr>
          <tr data-tt-id="2-1"  data-tt-parent-id="2">
            <td>a21</td><td>b21</td><td>c21</td><td>d21</td><td>e21</td>
          </tr> 
          -->                                       
        </tbody>
      </table>
    </div>
  </div>    
</@breadcrumbs.breadcrumbs>
</div>  

<#macro eduform parentEdu edus levelCode=2 display="none" isChild=false>
<#if edus?? && edus?size gt 0>
  <#assign outerAttrs='data-tt-parent="${parentEdu.code}" '>
  <#if isChild>
    <#assign outerAttrs=outerAttrs+'data-tt-child="true" data-tt-level-parent="${levelCode-1}" '>
  </#if>
  <#switch levelCode><#case 1><#assign label="省"><#break><#case 2><#assign label="市"><#break><#case 3><#assign label="区/县"><#break><#default><#assign label="学校"></#switch>
  <@formgroup label=label  outerStyle="display:"+display+";" outerAttrs=outerAttrs itemName="org" >
	<#list edus as edu>
	  <li <#if isChild>data-tt-parent="${parentEdu.code}"</#if>>
	    <a href="javascript:void(0);" data-tt-parent="${parentEdu.code}" data-tt-level="${levelCode}"  data-tt-value="${edu.code}" >${edu.name}</a>
	  </li>
	</#list>
  </@formgroup>  
  <#assign levelCode=levelCode+1>
  <#list edus as edu><@eduform parentEdu=edu edus=edu.childs levelCode=levelCode+1 display="none" isChild=true/></#list>
</#if>
</#macro>

<#macro panel multi=false>
  <div class="row">
    <div class="col-xs-12 " >       
      <div class="query-form clearfix down self-form <#if multi=true>multi-selected<#else>single-selected</#if>" style="overflow: visible;">
        <div class="form-side col-xs-12 col-md-12 col-sm-12">
          <form class="form-horizontal " role="form">
			<#nested>  			 
          </form>        
        </div>
      </div>      
    </div>
  </div>
</#macro>

<#macro formgroup label="" labelStyle="" outerStyle="" outerAttrs="" itemName=""
   btn={"type":"btn-default","icon":"glyphicon-plus","class":"multi-check","text":"全选","drawed":true}>
  <div class="form-group  form-tree " style="${outerStyle}" ${outerAttrs}>
    <label class="col-md-1 col-sm-1 control-label" style="${labelStyle}">${label}:</label>
    <div class=" <#if btn.drawed>col-md-10 col-sm-10<#else>col-md-11 col-sm-11</#if> ">
      <div class="input-group">
        <ul data-tt-item="${itemName}"><#nested></ul>
      </div>
    </div>
  <#if btn.drawed>
    <div class="form-side col-md-1 col-sm-1">
      <button class="btn ${btn.type} btn-xs ${btn.class}">
        <span class="glyphicon ${btn.icon}"></span> ${btn.text}
      </button>
    </div>
  </#if>  
  </div>
</#macro>
