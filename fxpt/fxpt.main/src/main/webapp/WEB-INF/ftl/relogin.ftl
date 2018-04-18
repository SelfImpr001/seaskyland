<#import "loginForm.ftl" as loginForm>
<div class="modal fade big-modal" id="big-modal" data-backdrop="static">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header" id="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h4 class="modal-title">用户管理>编辑用户信息${('>'+user.name)!}</h4>
      </div>
      <@loginForm.loginForm title="海云天数据分析与发布系统" showCode=showCode showAgreemet=false showRememberme=true/>
    </div>
  </div>
</div>