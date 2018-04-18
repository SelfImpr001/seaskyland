<!-- 弹出的模态框 -->
<form action="/res/update" method="get" id="form">
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
                  <input type="text" value="${resource.name!}" class="form-control myData" name="name" id="name" placeholder="资源名称" />
                  <input type="hidden" name="pk" value="${resource.pk!}" class="myData" />
                </div>
              </div>
            </td>
            
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
