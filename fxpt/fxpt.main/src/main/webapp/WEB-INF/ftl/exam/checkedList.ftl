<#import "../commons/pageHead.ftl" as pageHead> 
<div class="page-content">
  <#assign title= exam.name + "考生核对结果<b style='font-size:14px;'>(核对人数总计" +checkin.examineeTotal+"人)</b>"  >
  <@pageHead.pageHead title=title buttons=[{"text":"<<返回考生核对列表","type":"button","icon":"icon-reply"}] type="button"/>
  <#assign pager={"curpage":query.curpage,"totalpage":query.totalpage,"pagesize":query.pagesize,"totalRows":query.totalRows}>
  <ul class="nav nav-tabs">
    <li class="active"><a href="javascript:void(0);">核对成功(${pager.totalRows!0})</a></li>
    <li ><a href="javascript:void(0);">核对不成功(${uc})</a></li>
  </ul>
  <div class="tab-content" style="margin-top:-1px;">
    <div class="row tab-pane active">
      <input type="hidden" name="examId" value="${exam.id}">
	  <div class="col-xs-12" id="form-01">
		<table class="table table-bordered table-striped table-hover ">
			<thead>
				<tr>
				  <th colspan="5">${exam.name}</th>
				  <th colspan="4">学生库</th>
				</tr>
				<tr>
					<th >学号</th>
					<th >姓名</th>
					<th >性别</th>
					<th >所属学校</th>
					<th >所属班级</th>
					<th >学号</th>
					<th >姓名</th>
					<th >性别</th>
					<th >所属学校</th>				
				</tr>				
			</thead>
			<tbody>
			<#list query.list as checked>
			  <tr>
			    <td>${checked["studentId"]!""}</td>
			    <td>${checked["name"]!""}</td>
			    <td>${checked["gender"]!""}</td>
			    <td>${checked["schoolname"]!""}</td>
			    <td>${checked["className"]!""}</td>
			    <td>${checked["xh"]!""}</td>
			    <td>${checked["stName"]!""}</td>
			    <td>${checked["sex"]!""}</td>
			    <td>${checked["mySchoolName"]!""}</td>
			  </tr>
			</#list>
			</tbody>
		</table>
		
		<div style="text-align: center">
		  <input type="hidden" id="pageNum" value="${pager.curpage}"> 
		  <input type="hidden" id="pageCount" value="${pager.totalpage}">
		  <input type="hidden" id="pagesize" value="${pager.pagesize}">
		  <input type="hidden" id="totalRows" value="${pager.totalRows!0}">		 
		  <div id="checkedPager" class="pager" style="margin-top: 20px; text-align: center"></div>
		</div>
	</div>
	</div>
  </div>
</div>  
