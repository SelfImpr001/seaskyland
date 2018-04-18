<table class="table progress">
  <input type="hidden" name="progress" value="${percent}">
  <tr>
    <td>${progress}</td>
  </tr>
</table>
<div id="progressbar"><div class="progress-label">正在加载...</div></div>

<style>
  .ui-progressbar {
    position: relative;
  }
  .progress-label {
    position: absolute;
    left: 50%;
    top: 4px;
    font-weight: bold;
    text-shadow: 1px 1px 0 #fff;
  }
</style>
