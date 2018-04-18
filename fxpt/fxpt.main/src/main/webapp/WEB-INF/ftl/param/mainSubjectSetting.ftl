<@pageHead.pageHead title="设置学科类型"/>

<#assign tableNames = ["科目名称","主科","AB卷","是否参与分析"]>
<@table.table tableId="mainSubjectSetting" head=tableNames css="editTable">
 
<#list testPapers as tst>
		<tr>
			<td style="width: 25%;"><input value="${tst.id}"  type="hidden"
			class="form-control showEdit"><label class="showValue"></label>${tst.name}
			</td>
			<td style="width: 25%; text-align: center;">
              	 	<input name="mainType${tst.id}" type="radio" value="1" class="ace myData" ${(tst.masterSubject)?string( "checked","")}>
                    <span class="lbl"> </span>是
                      <span class="lbl">&nbsp;&nbsp;|&nbsp;&nbsp;</span>
              	 	<input  name="mainType${tst.id}" type="radio" value="0" class="ace myData" ${(tst.masterSubject)?string("","checked")}> 
                    <span class="lbl"></span>否
			</td>
			<td style="width: 25%; text-align: center;">
              	 	<input name="subjectType${tst.id}" type="radio" value="1" class="ace myData" ${(tst.containPaper)?string( "checked","")}>
                    <span class="lbl"> </span>是
                    <span class="lbl">&nbsp;&nbsp;|&nbsp;&nbsp;</span>
              	 	<input  name="subjectType${tst.id}" type="radio" value="0" class="ace myData" ${(tst.containPaper)?string("","checked")}> 
                    <span class="lbl"></span>否
			</td>
			
			<td style="width: 25%; text-align: center;">
              	 	<input name="analysisType${tst.id}" type="radio" value="1" class="ace myData" ${(tst.hasAnalysis)?string( "checked","")}>
                    <span class="lbl"> </span>是
                    <span class="lbl">&nbsp;&nbsp;|&nbsp;&nbsp;</span>
              	 	<input  name="analysisType${tst.id}" type="radio" value="0" class="ace myData" ${(tst.hasAnalysis)?string("","checked")}> 
                    <span class="lbl"></span>否
			</td>
		</tr>
</#list>
</@table.table>
