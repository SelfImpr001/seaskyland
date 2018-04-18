<#import "../commons/modal.ftl" as rule>
<#assign title="核对规则">
<#assign footBtns=[{"text":"核对","id":"update","class":"btn-primary","closeBtn":false},
				  {"text":"取消","id":"","class":"btn-default","closeBtn":true}]>

<@rule.modal title=title footBtns=footBtns showHeader=true showFooter=true modalId="rule-modal" size="modal-md">
  <div class="row">
    <div class="col-md-12 " >
      <div class="clearfix down" style="overflow: visible;">
        <div class="col-md-3 col-sm-3" style="padding-top: 4px;font-size: 12pt;">
		  <span class="span4">核对条件</span>
        </div>
        <div class="col-md-9 col-sm-9">          
          <form class="form-horizontal " role="form">
			<div class="form-group  ">
			  <div class="col-md-6 col-sm-6 checkbox">
			    <label><input type="checkbox" name="spec" checked disabled value="SameStudentIDSpecification">学号相同</label>
			  </div>
			  <div class="col-md-6 col-sm-6 checkbox">
			    <label><input type="checkbox" name="spec" checked disabled value="SameNameSpecification">姓名相同</label>
			  </div>
			</div>
			<div class="form-group  ">
			  <div class="col-md-6 col-sm-6 checkbox">
			    <label><input type="checkbox" name="spec" checked disabled value="SameSchoolSpecification">所在学校相同</label>
			  </div>
			  <div class="col-md-6 col-sm-6 checkbox" style="display:none;">
			    <label><input type="checkbox" name="spec" checked disabled value="SameClazzSpecification">所在班级相同</label>
			  </div>
			</div> 
			<p style="margin-right: 90px;margin-left: -15px;display:none;">核对学生历次信息是将本次考试中的学生信息与学生库中的学生信息按照核对条件匹配建立学生历次考试信息关联，请慎重选择核对条件！</p> 			 						                    
          </form>            
        </div>
      </div>
    </div>  
  </div> 
</@rule.modal>