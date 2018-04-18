<#import "shoolTermInfo.ftl" as shoolTermInfo>
<!--面包屑导航-->
  <div class="breadcrumbs" id="breadcrumbs">
    <ul class="breadcrumb">
      <li class="active">首页</li>
    </ul>
    <!-- .breadcrumb -->
  </div>
  <!--页面内容部分-->
  <div class="page-content">
    <!-- page-header -->
    <div class="page-header">
      <h1> <i class="icon-hand-right"></i> 最近考试时间轴
      </h1>
    </div>
    <!-- /.page-header -->
    <div class="row">
      <div class="col-xs-12">
        <!--考试时间轴-->
        <div id="timeLine">
          <div class="row">
            <div class="col-xs-12 col-sm-10 col-sm-offset-1">
              <#if examOfSchoolTerms??>
              	<#list examOfSchoolTerms as examOfSchoolTerm>
              		<@shoolTermInfo.shoolTermInfo examOfSchoolTerm=examOfSchoolTerm/>
              		 <hr/>
              	</#list>
              </#if>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>