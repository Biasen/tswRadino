<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="page">
    <div class="cartCard" style="max-width:520px;">
        <div class="cartHeader">
            <h1>Accedi</h1>
            <a class="btnGhost" href="${pageContext.request.contextPath}/CatalogoServlet">Torna al catalogo</a>
        </div>

        <c:if test="${not empty error}">
            <div class="formError">${error}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/LoginServlet" method="post" class="form">
            <label>Email</label>
            <input type="email" name="email" required>

            <label>Password</label>
            <input type="password" name="password" required>

            <button class="btnPrimary btnCheckout" type="submit">Login</button>
        </form>

        <p style="margin-top:12px;">
            Non hai un account?
            <a href="${pageContext.request.contextPath}/view/register.jsp">Registrati</a>
        </p>
    </div>
</div>
</body>
</html>
