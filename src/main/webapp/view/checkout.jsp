<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="it">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Checkout</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
  <style>
    /* opzionale: fa sembrare i summary dei bottoni */
    .choice summary{
      list-style: none;
      cursor: pointer;
    }
    .choice summary::-webkit-details-marker { display:none; }
    .choice summary .btnLike {
      display:inline-block;
      padding:10px 12px;
      border-radius:10px;
      border:1px solid rgba(255,255,255,.25);
      background: transparent;
      color: inherit;
    }
    .choice[open] summary .btnLike{
      border-color: rgba(255,255,255,.6);
    }
    .choiceBox { margin-top: 10px; }
  </style>
</head>
<body>
<div class="page">
  <div class="cartCard">
    <div class="cartHeader">
      <h1>Checkout</h1>
      <a class="btnGhost" href="${pageContext.request.contextPath}/CarrelloViewServlet">Torna al carrello</a>
    </div>

    <c:if test="${not empty error}">
      <div class="formError">${error}</div>
    </c:if>
    <c:if test="${not empty ok}">
      <div class="formOk">${ok}</div>
    </c:if>

    <!-- =======================
         SCELTA INDIRIZZO (2 TASTI)
         ======================= -->
    <div class="sectionBox">
      <h3>Indirizzo di spedizione</h3>

      <!-- 1) Scegli tra indirizzi salvati -->
      <details class="choice" id="savedChoice">
        <summary><span class="btnLike">Scegli tra indirizzi salvati</span></summary>

        <div class="choiceBox">
          <c:choose>
            <c:when test="${empty indirizzi}">
              <div class="formError">Non hai indirizzi salvati.</div>
            </c:when>
            <c:otherwise>
              <label for="addrSelect">Indirizzo salvato</label>
              <select id="addrSelect">
                <c:forEach items="${indirizzi}" var="a">
                  <option value="${a.id}">
                      ${a.via}, ${a.citta} (${a.provincia}) ${a.cap}
                  </option>
                </c:forEach>
              </select>

              <div style="margin-top:10px; display:flex; gap:10px; flex-wrap:wrap;">
                <form id="deleteAddrForm" action="${pageContext.request.contextPath}/IndirizzoDeleteServlet" method="post">
                  <input type="hidden" name="idIndirizzo" id="deleteIdIndirizzo">
                  <button class="btnGhost" type="submit">Rimuovi indirizzo selezionato</button>
                </form>
              </div>
            </c:otherwise>
          </c:choose>
        </div>
      </details>

      <!-- 2) Inserisci nuovo indirizzo -->
      <details class="choice" id="newChoice" style="margin-top:10px;">
        <summary><span class="btnLike">Scegli nuovo indirizzo</span></summary>

        <div class="choiceBox">
          <form id="newAddrForm" action="${pageContext.request.contextPath}/IndirizzoAddServlet" method="post">
            <div class="addrGrid">
              <div>
                <label for="via">Via</label>
                <input id="via" type="text" name="via" required>
              </div>
              <div>
                <label for="citta">Città</label>
                <input id="citta" type="text" name="citta" required>
              </div>
              <div>
                <label for="cap">CAP</label>
                <input id="cap" type="text" name="cap" required>
              </div>
              <div>
                <label for="provincia">Provincia</label>
                <input id="provincia" type="text" name="provincia" maxlength="2" required>
              </div>
              <div>
                <label for="nazione">Nazione</label>
                <input id="nazione" type="text" name="nazione" value="Italia" required>
              </div>
              <div>
                <label for="telefono">Telefono</label>
                <input id="telefono" type="text" name="telefono">
              </div>
            </div>

            <button class="btnPrimary" type="submit">Salva nuovo indirizzo</button>
          </form>
        </div>
      </details>

      <!-- campo hidden usato dal checkout: verrà riempito via JS -->
      <input type="hidden" id="checkoutIdIndirizzo">
    </div>

    <!-- =======================
         PAGAMENTO + CONFERMA
         ======================= -->
    <div class="sectionBox" style="margin-top:14px;">
      <h3>Pagamento + conferma</h3>

      <form id="checkoutForm" action="${pageContext.request.contextPath}/CheckoutServlet" method="post">
        <!-- verrà valorizzato prima del submit -->
        <input type="hidden" name="idIndirizzo" id="checkoutIdIndirizzoField">

        <label for="idMetodo">Metodo di pagamento</label>
        <select id="idMetodo" name="idMetodo" required>
          <c:forEach items="${metodi}" var="m">
            <option value="${m.id}">${m.tipo} - ${m.circuito}</option>
          </c:forEach>
        </select>

        <div class="totalPanel" style="margin:16px 0 0; max-width:420px;">
          <div class="totalRow">
            <span class="totalLabel">Totale</span>
            <span class="totalValue">€ ${totale}</span>
          </div>
          <button class="btnPrimary btnCheckout" type="submit">Conferma ordine</button>
        </div>
      </form>
    </div>

    <script>
      // 1) Se apri "indirizzi salvati", chiudo "nuovo indirizzo" e viceversa
      const savedChoice = document.getElementById('savedChoice');
      const newChoice = document.getElementById('newChoice');
      if (savedChoice && newChoice) {
        savedChoice.addEventListener('toggle', () => { if (savedChoice.open) newChoice.open = false; });
        newChoice.addEventListener('toggle', () => { if (newChoice.open) savedChoice.open = false; });
      }

      // 2) Sincronizzo id indirizzo selezionato -> delete + checkout
      const sel = document.getElementById('addrSelect');
      const hidDelete = document.getElementById('deleteIdIndirizzo');
      const hidCheckout = document.getElementById('checkoutIdIndirizzoField');

      function syncFromSelect() {
        const val = sel ? sel.value : "";
        if (hidDelete) hidDelete.value = val;
        if (hidCheckout) hidCheckout.value = val;
      }
      if (sel) { sel.addEventListener('change', syncFromSelect); syncFromSelect(); }

      // 3) Prima del submit checkout, controllo che l'utente abbia scelto "indirizzo salvato"
      //    (perché l'ordine richiede un idIndirizzo esistente nel tuo CheckoutServlet)
      const checkoutForm = document.getElementById('checkoutForm');
      if (checkoutForm) {
        checkoutForm.addEventListener('submit', (e) => {
          // Se l'utente non ha aperto "indirizzi salvati", blocco e chiedo di scegliere/salvare prima
          if (!savedChoice || !savedChoice.open) {
            e.preventDefault();
            alert("Per confermare l'ordine devi prima scegliere un indirizzo salvato (oppure salvare un nuovo indirizzo).");
            return;
          }
          if (!hidCheckout || !hidCheckout.value) {
            e.preventDefault();
            alert("Seleziona un indirizzo salvato.");
          }
        });
      }
    </script>

  </div>
</div>
</body>
</html>
