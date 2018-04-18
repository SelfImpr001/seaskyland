<#import "../../commons/pageHead.ftl" as pageHead> 
<#import "../../commons/table.ftl" as table>

<div class="page-content">
	<@pageHead.pageHead title="等级设置及赋分" buttons=[{"id":"returnUperPage","name":"","text":"<<返回上级页面 "}]/>
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
	
	
			<label class="col-md-1 col-sm-1 control-label">科目</label>
			<div class="col-md-3 col-sm-3">
				<select class="form-control selectpicker myData" id="atpSelect">
					<#list atpList as atp>
						<#if atp.id == curAtp.id>
							<option value="${atp.id}" data-fullScore="${atp.fullScore}" selected>${atp.name}</option>
						<#else>
							<option value="${atp.id}" data-fullScore="${atp.fullScore}">${atp.name}</option>
						</#if>
					</#list>
					</select>
			</div>
	
	
			<label class="col-md-1 col-sm-1 control-label">划分等级</label>
			<div class="col-md-3 col-sm-3">
				<select class="form-control selectpicker myData" id="levelNumSelecte">
					<option value="4">4</option>
					<option value="5">5</option>
					<option value="6">6</option>
					<option value="7">7</option>
					<option value="8">8</option>
					<option value="9">9</option>
					<option value="10">10</option>
					<option value="11">11</option>
					<option value="12">12</option>
					<option value="13">13</option>
					<option value="14">14</option>
					<option value="15">15</option>
					<option value="16">16</option>
					<option value="17">17</option>
					<option value="18">18</option>
					<option value="19">19</option>
					<option value="20">20</option>
					<option value="21">21</option>
					<option value="22">22</option>
					<option value="23">23</option>
					<option value="23">24</option>
					<option value="25">25</option>
				</select>
			</div>
		</div>
	</form>
	</div>


<#assign tableNames = ["等级序号","起始分数","结束分数","人数","人数比例","等级类别","等级分"]>
<@table.table tableId="levelScoreTable" head=tableNames css="editTable">
<tr class="template">
	<td style="width: 10%;" name="id"></td>
	<td><input type="text" name="beginScore" class="form-control showEdit"><label class="showValue"></label></td>
	<td><input type="text" name="endScore"	class="form-control showEdit"><label class="showValue"></label></td>
		<td style="width: 10%;" name="num"></td>
	   <td style="width: 10%;" name="percent"></td>
	<td><input type="text" name="levelName"	class="form-control showEdit"><label class="showValue"></label></td>
	<td><input type="text" name="levelScore" class="form-control showEdit"><label class="showValue"></label></td>

</tr>
</@table.table>
<div class="row">
	<div class="col-md-12 col-sm-12">
		<div class="pull-right">
      	  <button class="btn btn-primary " type="button" trigger="" id="saves" name="">确定</button>
      	</div>
	</div>
</div>

</div>