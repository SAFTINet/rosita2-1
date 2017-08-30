<tr>
    <td rowspan="2">
      ${rule.description}
    </td>
    <td><g:radio name="${rule.name}" value="true" checked="${rule.allow ? 'checked' : ''}" /><img src="${resource(file: 'check.png', dir: 'images/icons')}" width="16" height="16"/>  ${rule.allowMessage}</td>
</tr>
<tr>
    <td><g:radio name="${rule.name}" value="false" checked="${!rule.allow ? 'checked' : ''}" /><img src="${resource(file: 'close_small.png', dir: 'images/icons')}"/> Fail validation</td>
</tr>
<tr><td colspan="3" class="tableseparator"></td></tr>