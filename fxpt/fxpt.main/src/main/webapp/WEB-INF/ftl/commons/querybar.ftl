<#macro querybar showAngle=false>
<div class="row">
    <div class="col-xs-12 " >
      <div class="query-form clearfix down" style="overflow: visible;">
        <div class="form-side col-xs-10 col-md-10 col-sm-10">
          <form class="form-horizontal " role="form">
            <#nested>            
          </form>
        </div>
        <div class="form-side col-xs-2 col-md-2 col-sm-2">          
          <a class="btn btn-success " herf="javascript:void(0);" ><i class="icon-search" title="查询"></i></a>
          <a class="btn btn-success " herf="javascript:void(0);" ><i class="icon-undo" title="清空"></i></a>
          <a class="btn btn-success " herf="javascript:void(0);" <#if showAngle=false>style="display:none;"</#if>><i class="icon-double-angle-down"></i></a>
          <a class="btn btn-success " herf="javascript:void(0);" style="display:none;"><i class="icon-double-angle-up"></i></a>          
        </div>
      </div>
    </div>
  </div>
</#macro>