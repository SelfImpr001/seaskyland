<!-- 弹出的模态框 -->
<form action="/res/add" method="post" id="resViewForm">
<div class="modal fade index-modal1" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
  aria-hidden="true">
  <div class="modal-dialog self-dialog">
    <div class="modal-content">
      <div class="modal-header" style="border-: 1px solid red;">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h3>资源信息</h3>
      </div>
      <div class="modal-body">
        <table class="table table-bordered" style="font-size:12px;font-weight:normal;">
          <tr>
            <td>
              <div class="form-group">
                <label for="name" class="col-sm-3 control-label">资源名称<span class="mustItem">*</span></label>
                <div class="col-sm-9">
                  <input type="text"  class="form-control myData" name="name" id="name" placeholder="资源名称">
                </div>
              </div>
            </td>
            <td>
              <div class="form-group">
                <label for="resType" class="col-sm-3 control-label">资源类型<span class="mustItem">*</span></label>
                <div class="col-sm-9">
                  <input type="text" name="type"  class="form-control myData" id="resType" placeholder="资源类型">
                </div>
              </div>
            </td>
          </tr>
          <tr>
            <td>
              <div class="form-group">
                <label for="resUrl" class="col-sm-3 control-label">资源URL<span class="mustItem">*</span></label>
                <div class="col-sm-9">
                  <input type="text" name="url"  class="form-control myData" id="resUrl" placeholder="资源URL">
                </div>
              </div>
            </td>
            <td class="radio-td">
              <div class="form-group">
                <label for="parentRes" class="col-sm-3 control-label">子资源<span class="mustItem">*</span>：</label>
                <div class="col-sm-9  radio-content">
                   <select id="parentRes"  class="selectpicker show-tick form-control" data-width="200px">
                   	<#list (resource.children)![] as child>
                     <option >111</option>
                    </#list>
                   </select>
                </div>
              </div>
            </td>
          </tr>
          <tr>
            <td>
              <div class="form-group">
                <label for="exampleInputFile" class="col-sm-3 control-label">图标(icon)</label>
                <div class="col-sm-9">
                  <input type="file" name="icon"  class="myData" id="exampleInputFile" />
                  <p class="help-block">测试</p>
                </div>
              </div>
            </td>
            <td>
              <div class="form-group">
                <label for="orderBy" class="col-sm-3 control-label">排序</label>
                <div class="col-sm-9">
                  <input type="text" name="reorder"  class="form-control myData" id="orderBy" placeholder="">
                </div>
              </div>
            </td>
          </tr>
          <tr>
            <td>
              <div class="form-group">
                <label for="isChild" class="col-sm-3 control-label">是否子节点</label>
                <div class="col-sm-9  radio-content">
                  <div class="download-check">
                    <input   id="isChild" type="radio" class="ace" checked="checked">
                    <span class="lbl">是</span>
                    <input  type="radio" class="ace">
                    <span class="lbl">否</span>
                  </div>
                </div> 
              </div>
            </td>
            <td>
              <div class="form-group">
                <label for="isAvaila" class="col-sm-3 control-label">有效性</label>
                <div class="col-sm-9  radio-content">
                  <div class="download-check">
                    <input name="available" value="true" type="radio" id="isAvaila" class="ace myData" checked="checked">
                    <span class="lbl">是</span>
                    <input name="available" value="false" type="radio" class="ace myData">
                    <span class="lbl">否</span>
                  </div>
                </div> 
              </div>
            </td>
          </tr>
          <tr>
            <td colspan="2">
              <div class="form-group">
                <label for="desc" class="col-sm-3 control-label">描述</label>
              </div>
              <textarea id="desc" class="form-control myData" rows="3" name="remarks"></textarea>
          </tr>
        </table>
      </div>
      <div class="modal-footer">
        <div class="self-modal-footer">
          <button type="button" class="btn btn-primary" name="commit">确定</button>
          <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
        </div>
      </div>
    </div>
    <!-- /.modal-content -->
  </div>
  <!-- /.modal-dialog -->
</div>
</form>
