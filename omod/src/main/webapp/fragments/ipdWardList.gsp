<script>
    var jq = jQuery;
    jq(function () {
        jq('#wardTable').DataTable();
    });
</script>
<table id="wardTable">
    <thead>
    <tr>
        <th>Ward name</th>
        <th>Ward code</th>
        <th>Bed strength</th>
        <th>Date created</th>
        <th>Created by</th>
    </tr>
    </thead>
    <tbody>
      <% if(wards) {%>
          <% wards.each { %>
              <tr>
                  <td>${it.wardName}</td>
                  <td>${it.wardCode}</td>
                  <td>${it.description}</td>
                  <td>${it.createdOn}</td>
                  <td>${it.createdBy}</td>
              </tr>
          <%}%>
      <%}%>
    </tbody>
 </table>