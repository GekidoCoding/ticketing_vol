<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Espace Utilisateur</title>
</head>
<body>
    <h1>${message}</h1>
    <p>Connecté en tant qu'utilisateur: ${sessionScope.username}</p>
    <a href="${pageContext.request.contextPath}/auth/logout">Déconnexion</a>
</body>
</html>