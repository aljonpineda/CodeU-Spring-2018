<%@ page import="codeu.model.data.User" %>
<%@ page import="codeu.model.store.basic.UserStore" %>

<!DOCTYPE html>
<html>
<head>
  <title>Salvatorian Realm</title>
  <link rel="icon" href="https://greggarcia.org/img/exp/10-1-1-exp.png">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>

  <nav>
    <a id="navTitle" href="/"><i class="fa fa-home"></i></a>
    <a href="/conversations">Conversations</a>
    <% if(request.getSession().getAttribute("user") != null){ %>
      <a href="/user/<%= request.getSession().getAttribute("user") %>"><%= request.getSession().getAttribute("user") %>'s Profile</a>
      <a href="/friendslist">Friends</a>
    <% } else{ %>
      <a href="/login">Login</a>
    <% } %>
    <a href="/about.jsp">About</a>
    <a href="/activityfeed">Activity Feed</a>
    <% if(request.getSession().getAttribute("user") != null){
        String username = (String) request.getSession().getAttribute("user");
        UserStore userStore = UserStore.getInstance();
        User user = userStore.getUser(username);
        if (user.isAdmin()){
        %>
          <a href="/admin">Admin</a>
        <%
        }
      }%>
  </nav>

  <div id="container">
    <div
      style="width:75%; margin-left:auto; margin-right:auto; margin-top: 50px;">

      <h1>The Salvatorians' Chat App</h1>
      <h2>Welcome to the Salvatorian Realm!</h2>

      <ul>
        <% if(request.getSession().getAttribute("user") == null) {%>
        <li><a href="/login">Login</a> to get started.</li>
        <%} %>
        <li>Go to the <a href="/conversations">conversations</a> page to
            create or join a conversation.</li>
        <li>View the <a href="/about.jsp">about</a> page to learn more about the
            project.</li>
      </ul>
    </div>
  </div>
</body>
</html>
