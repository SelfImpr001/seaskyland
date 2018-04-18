<tbody id="role-rows">
<#if roles ??>
  <#list roles as role>
  <tr>
    <td><input name="roleChecked" value="${role.pk}" type="checkbox" class="ace"><span class="lbl"></span></td>
    <td>${(role.code)!""}111</td>
    <td>${(role.name)!""}</td>
    <td>${(role.desc)!""}</td>
    <td>${role.available?string("有效","无效")}</td>
  </tr>
  </#list>
</#if>
</tbody>
