<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registrazione</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="page">
    <div class="cartCard" style="max-width:520px;">
        <div class="cartHeader authHeader">
            <h1>Registrazione</h1>
            <div class="topActions">
                <a class="btnGhost" href="${pageContext.request.contextPath}/view/login.jsp">Hai già un account?</a>
                <a class="btnGhost" href="${pageContext.request.contextPath}/CatalogoServlet">Torna al catalogo</a>
            </div>
        </div>


        <c:if test="${not empty error}">
            <div class="formError">${error}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/RegisterServlet" method="post" class="form">

            <label>Nome</label>
            <input type="text" name="nome" required>

            <label>Cognome</label>
            <input type="text" name="cognome" required>

            <label>Email</label>
            <input type="email" name="email" required>

            <label>Password</label>
            <input type="password" name="password" minlength="6" required>

            <button class="btnPrimary btnCheckout" type="submit">Crea account</button>
        </form>
    </div>
</div>
</body>
</html>
