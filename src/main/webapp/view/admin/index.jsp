<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="it">
<head>
  <meta charset="UTF-8">
  <title>Admin</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="page">
  <div class="cartCard">
    <div class="cartHeader">
      <h1>Pannello admin</h1>
      <a class="btnGhost" href="${pageContext.request.contextPath}/CatalogoServlet">Catalogo</a>
    </div>

    <a class="btnGhost" href="${pageContext.request.contextPath}/admin/ordini">Gestisci ordini</a>
    <a class="btnGhost" href="${pageContext.request.contextPath}/admin/prodotti">Gestisci prodotti</a>
    <a class="btnGhost" href="${pageContext.request.contextPath}/admin/prodotto">Nuovo prodotto</a>
  </div>
</div>
</body>
</html>
