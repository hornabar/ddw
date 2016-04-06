<%--
  Created by IntelliJ IDEA.
  User: Bibi
  Date: 5. 4. 2016
  Time: 14:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Twitter Sentiment</title>
  </head>
  <body>
    <h1>Twitter Sentiment Analyses</h1>

    <form action="IndexServlet" method="POST">
      <label for="keyword">Key word:</label>
      <input id="keyword" name="keyword" type="text"/>
    </form>
  </body>
</html>
