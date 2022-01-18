<%@tag description="Overall Page template" pageEncoding="windows-1256"%>
<%@attribute name="header" fragment="true" %>
<%@attribute name="footer" fragment="true" %>
<html>
  <body>
    <div id="pageheader">
      <jsp:invoke fragment="header"/>
    </div>
    <div id="body">
      <jsp:doBody/>
    </div>
    <div id="pagefooter">
      <jsp:invoke fragment="footer"/>
    </div>
  </body>
</html>