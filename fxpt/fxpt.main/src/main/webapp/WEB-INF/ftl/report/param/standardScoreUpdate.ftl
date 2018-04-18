<#import "../../commons/pageHead.ftl" as pageHead> 
<#import "../../commons/table.ftl" as table>

<div class="page-content">
	<@pageHead.pageHead title="导出分数设置" buttons=[{"id":"returnsetStandardScoreView","name":"","text":"<<返回上级页面 "}]/>
	<div class="row">
	<form class="form-horizontal " role="form">
		<div class="form-group">
			<label class="col-md-1 col-sm-1 control-label">文理</label>
			<div class="col-md-3 col-sm-3">
				<select class="form-control selectpicker myData" id="wlSelect">
					<#if exam.wlForExamStudent>
						<option value="1" ${(wl==1)?string("selected","")}>理科</option>
						<option value="2" ${(wl==2)?string("selected","")}>文科</option>
					<#else>
						<option value="0">未分科</option>
					</#if>
				</select>
			</div>
		</div>
	</form>
	</div>


<#assign tableNames = ["编号","学科","学科满分","Z分数权重(%)"]>
<@table.table tableId="" head=tableNames css="editTable">
<#list reslist as res>
	<tr >
		<td width="10%">${res_index+1}</td>
		<td width="20%">${res[2]}</td>
		<td width="20%">${res[5]}</td>
		<td width="30%"><input type="text" subjectid="${res[1]}" subjectname="${res[2]}" examid="${res[4]}" zwl="${res[3]}" VALUE="${res[6]!}" class="change" style=""><p id="s${res[1]}" style="color:red;"></p></td>
	</tr>
</#list>
<tr><td></td><td>总分</td><td></td><td><span id="stxt"></span></td></tr>
<tr><td colspan="4"> 导出分数 = <input type="text" value="${defaultvalue!500}" id="defaultvalue" style="border:#ff0000 1px solid;" maxlength="4"> + <input type="text" id="zvalue" value="${zvalue!100}" style="border:#ff0000 1px solid;" maxlength="4"> *Z(总分)</td></tr>
</@table.table>


<div class="row">
	<div class="col-md-12 col-sm-12">
		<div class="pull-right">
      	  <button class="btn btn-primary " type="button" trigger="" id="zsaves" name="">确定</button>
      	</div>
	</div>
</div>
<input type="hidden" id="examid" value="${exam.id}"/>
<input type="hidden" id="wl" value="${wl}"/>

</div>