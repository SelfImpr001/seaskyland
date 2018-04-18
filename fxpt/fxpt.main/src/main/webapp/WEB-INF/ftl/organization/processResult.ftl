<span">
<!--组织导入导出信息提示 -->
<#if !processResult.hasError>
操作成功！
<#else>
操作失败！${processResult.message}
</#if>
</span>