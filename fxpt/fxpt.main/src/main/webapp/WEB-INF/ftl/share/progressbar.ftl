<table class="table progress">
  <input type="hidden" name="progress" value="${progress.finished?string}">
  <tr>
    <td style="width: ${progress.completed}%;">&nbsp;</td>
    <td style="width: ${progress.left}%; text-align: left; color: #D9534F; background: transparent;<#if progress.finished=true>display:none;</#if>"></td>
  </tr>
</table>