 <div style="text-align: right">
 	
		<a id="addTestPaper" href="#" title="增加试卷"> <span class="glyphicon glyphicon-plus">增加试卷</span> </a>
			<hr/>	
	</div>
<table class="table table-bordered table-striped table-hover">
      <thead>
        <tr>
          <th  colspan="10" style="text-align:center">考生试卷信息</th>
        </tr>
        <tr>
          <th>试卷名称</th>
          <th>试卷满分</th>
          <th>客观题满分</th>
          <th>主观题满分</th>
          <th>对应的科目</th>
          <th>试卷类型</th>
          <th>是否综合试卷</th>
          <th>已导入小题</th>
          <th>已导入成绩</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
      	<#list testPapers as testPaper>
      	<tr>
          <td>${testPaper.name}
            </td>
          <td>${testPaper.fullScore?string("0.00")}
            </td>
          <td>${testPaper.kgScore?string("0.00")}
            </td>
          <td>${testPaper.zgScore?string("0.00")}
            </td>
          <td>${testPaper.subject.name}</td>
          
          <td>
          <#if testPaper.paperType == 1>
          	理科试卷
          <#elseif testPaper.paperType == 2>
          	文科试卷
          <#else>
          	未分卷
          </#if>
          </td>
          <td>${testPaper.composite?string("是","否")}</td>
          <td>
          <#if testPaper.hasItem>
          	已导入<span class="separator">|</span> <a href="#" title="删除" objectId="${testPaper.id}" class="deleteItem">删除小题</a>
          <#else>
          	<a href="#"  objectId="${testPaper.id}" objectName="${testPaper.name}" class="importItem">导入小题</a>
          </#if>
          <td>
          <#if testPaper.hasCj>
          	 已导入|</span> <a href="#" title="删除" objectId="${testPaper.id}" class="deleteCj">删除成绩</a>
          <#else>
          	<a href="#"  objectId="${testPaper.id}" objectName="${testPaper.name}" class="importCj">导入成绩</a>
          </#if>
          
          
           <td class="opera">
           <a href="#" title="编辑" objectId="${testPaper.id}" class="update"><i class="icon-edit text-success"></i></a>
           <span class="separator">|</span> <a href="#" title="删除" objectId="${testPaper.id}" class="delete"><i class="icon-trash"></i></a>
           </td>
        </tr>
      	</#list>
      </tbody>
    </table>