<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Catalogo Orologi</title>

    <!-- CSS manuale: puoi spostarlo in /css/style.css quando vuoi -->
    <style>
        body { font-family: Arial, sans-serif; margin: 0; padding: 0; }
        header { padding: 16px; background: #111; color: #fff; }
        main { padding: 16px; }
        .grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 16px; }
        .card { border: 1px solid #ddd; border-radius: 10px; padding: 12px; background: #fff; }
        .card img { width: 100%; height: 200px; object-fit: cover; border-radius: 8px; background: #f3f3f3; }
        .name { font-size: 16px; font-weight: 700; margin: 10px 0 6px; }
        .brand { color: #444; margin: 0 0 6px; }
        .price { font-weight: 700; margin: 0 0 10px; }
        .meta { color: #666; font-size: 14px; margin: 0; }
        .empty { padding: 16px; background: #fff3cd; border: 1px solid #ffeeba; border-radius: 8px; }
    </style>
</head>

<body>
<header>
    <h1>Catalogo</h1>
</header>

<main>
    <c:choose>
        <c:when test="${empty orologi}">
            <div class="empty">
                Nessun prodotto disponibile.
            </div>
        </c:when>

        <c:otherwise>
            <div class="grid">
                <c:forEach items="${orologi}" var="o">
                    <div class="card">
                        <c:choose>
                            <c:when test="${not empty o.immagine}">
                                <img src="${pageContext.request.contextPath}${o.immagine}"
                                     alt="Immagine ${o.nomeModello}">
                            </c:when>
                            <c:otherwise>
                                <img src="${pageContext.request.contextPath}/img/placeholder.jpg"
                                     alt="Immagine non disponibile">
                            </c:otherwise>
                        </c:choose>

                        <div class="name">${o.nomeModello}</div>
                        <p class="brand">${o.marca}</p>
                        <p class="price">€ ${o.prezzoAttuale}</p>
                        <p class="meta">Disponibili: ${o.stock}</p>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</main>
</body>
</html>
