 <#if page.list??>
		 <#list page.list as student>
		  <tr>
			  <td>${student_index+1}</td>
			  <#if map.studentId==1><td>${student.studentId!""}</td></#if>
			  <#if map.name==1> <td>${student.name!""}</td></#if>
			  <#if map.domicile==1> <td>${student.domicile!""}</td></#if>
			  <#if map.nation==1> <td>${student.nation!""}</td></#if>
			  <#if map.zkzh==1><td>${student.zkzh!""}</td></#if>
			  <#if map.learLanguage==1><td></td></#if>
			  <#if map.wl==1><td>
			  	 <#if student.wl??>
			  	 	<#if student.wl==2>文科<#elseif student.wl==1>理科<#else>不分文理
				  	</#if>
				  </#if>
			  </td></#if>
			  <#if map.isPast==1><td>
			  	<#if student.pastValue??>
			  	 	<#if student.pastValue=1>是<#else>否
				  	</#if>
				  </#if>
			  </td></#if>
			  <#if map.isTransient==1><td>
			  		<#if student.trantValue??>
			  	 	<#if student.trantValue=1>是<#else>否
				  	</#if>
				  </#if>
			  </td></#if>
			  <#if map.studentType==1><td>${student.studentType!""}</td></#if>
			  <#if map.languagePattern==1><td>${student.languagePattern!""}</td></#if>
			  <#if map.languageType==1><td>${student.languageType!""}</td></#if>
			  <#if map.area==1><td>${student.area!""}</td></#if>
			  <#if map.gender==1><td>
				 <#if student.gender??>
					  <#if student.gender=1>男<#elseif student.gender=2>女<#else>
					  </#if>
			  	</#if>
			  </td></#if>
			  <#if map.schoolCode==1><td><#if student.school??>${student.school.code!""}</#if></td></#if>
			  <#if map.schoolName==1><td><#if student.school??>${student.school.name!""}</#if></td></#if>
			  <#if map.classCode==1> <td><#if student.clazz??>${student.clazz.code!""}</#if></td></#if>
			  <#if map.className==1> <td><#if student.clazz??>${student.clazz.name!""}</#if></td></#if>
			  
		  </tr>
		 </#list>
</#if>	
<input type="hidden" id="pageNum" value="${page.curpage}"> 
<input type="hidden" id="pageCount" value="${page.totalpage}">
<input type="hidden" id="pagesize" value="${page.pagesize}">
<input type="hidden" id="pageRows" value="${page.totalRows}">
<input type="hidden" id="examId" value="${examId}"> 
