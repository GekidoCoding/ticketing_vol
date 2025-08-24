<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="mg.ticketing.model.Classe" %>
<%@ page import="mg.ticketing.model.Vol" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>

<%! 
    void afficherErreurs(List<String> erreurs, JspWriter out) throws java.io.IOException {
        if (erreurs != null && !erreurs.isEmpty()) {
            out.println("<div class='error-message'>");
            for (String err : erreurs) {
                out.println("<span>" + err + "</span>");
            }
            out.println("</div>");
        }
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Ajouter Promotion</title>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            background-color: #f0f2f5;
            display: flex;
            flex-direction: column;
            align-items: center;
            min-height: 100vh;
            margin: 0;
            padding: 2rem;
        }
        .statistic-detail {
            background-color: white;
            padding: 1.5rem;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 600px;
            margin-bottom: 1rem;
        }
        .statistic-detail p {
            margin: 0.5rem 0;
            color: #333;
            font-size: 1rem;
            display: flex;
            justify-content: space-between;
            border-bottom: 1px solid #eee;
            padding-bottom: 0.5rem;
        }
        .statistic-detail p:last-child {
            border-bottom: none;
        }
        .statistic-detail strong {
            color: #007bff;
            font-weight: 600;
        }
        .form-container {
            background-color: white;
            padding: 2rem;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 600px;
        }
        h1 {
            text-align: center;
            color: #333;
            margin-bottom: 1.5rem;
            font-size: 1.8rem;
        }
        .error-message {
            background-color: #ffe6e6;
            color: #d32f2f;
            padding: 1rem;
            border-radius: 5px;
            margin-bottom: 1rem;
            text-align: center;
            font-size: 0.9rem;
        }
        .error-message span {
            display: block;
            margin-bottom: 0.5rem;
        }
        form {
            display: flex;
            flex-direction: column;
            gap: 1.2rem;
        }
        .form-group {
            display: flex;
            flex-direction: column;
        }
        label {
            color: #444;
            margin-bottom: 0.5rem;
            font-weight: 600;
            font-size: 0.95rem;
        }
        input, select {
            padding: 0.8rem;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 1rem;
            transition: border-color 0.3s, box-shadow 0.3s;
        }
        input:focus, select:focus {
            outline: none;
            border-color: #007bff;
            box-shadow: 0 0 5px rgba(0, 123, 255, 0.3);
        }
        button {
            background-color: #007bff;
            color: white;
            padding: 0.8rem;
            border: none;
            border-radius: 5px;
            font-size: 1rem;
            cursor: pointer;
            transition: background-color 0.3s, transform 0.1s;
        }
        button:hover {
            background-color: #0056b3;
            transform: translateY(-1px);
        }
        button:active {
            transform: translateY(0);
        }
        .cancel-button {
            background-color: #d32f2f;
        }
        .cancel-button:hover {
            background-color: #b71c1c;
        }
        .form-group:last-child {
            display: flex;
            gap: 1rem;
            justify-content: center;
            margin-top: 1rem;
        }
        @media (max-width: 480px) {
            body {
                padding: 1rem;
            }
            .form-container, .statistic-detail {
                padding: 1.5rem;
            }
            h1 {
                font-size: 1.5rem;
            }
        }
    </style>
</head>
<body>
    <% Vol vol = (Vol)request.getAttribute("vol"); %>

    <div class="statistic-detail">
        <p><strong>ID:</strong> <%=vol.getId()%></p>
        <p><strong>Ville de Départ:</strong> <%=vol.getVilleDepart().getNom()%></p>
        <p><strong>Ville d'Arrivée:</strong> <%=vol.getVilleArrivee().getNom()%></p>
        <p><strong>Date/Heure de Départ:</strong> <%=vol.getDateHeureDepart()%></p>
    </div>

    <div class="form-container">
        <h1>Ajouter une promotion</h1>
        
        <form method="post" action="<%= request.getContextPath() %>/admin/promotion/create">
            <input name="promotion.volId" type="hidden" value="<%=vol.getId()%>">

            <div class="form-group">
                <label for="classeId">Classe:</label>
                <select id="classeId" name="promotion.classeId" >
                    <option value="">Sélectionnez une classe</option>
                    <%
                        List<Classe> classes = (List<Classe>) request.getAttribute("classes");
                        for (mg.ticketing.model.Classe classe : classes) { %>
                        <option value="<%=classe.getId()%>"><%=classe.getNom()%></option>
                    <% } %>
                </select>
                <% afficherErreurs((List<String>) request.getAttribute("error_promotion.classeId"), out); %>
            </div>

            <div class="form-group">
                <label for="nbrSiege">Nombre de sièges promus:</label>
                <input type="number" id="nbrSiege" name="promotion.nbrSiege" >
                <% afficherErreurs((List<String>) request.getAttribute("error_promotion.nbrSiege"), out); %>
            </div>

            <div class="form-group">
                <label for="nbrSiege">Prix :</label>
                <input type="number" id="prix" name="promotion.prix" >
                <% afficherErreurs((List<String>) request.getAttribute("error_promotion.prix"), out); %>
            </div>

            <div class="form-group">
                <label for="reduction">Réduction (%):</label>
                <input type="number" id="reduction" step="0.01" name="promotion.reduction" >
                <% afficherErreurs((List<String>) request.getAttribute("error_promotion.reduction"), out); %>
            </div>

              <div class="form-group">
                <label for="reduction">Date Butoire:</label>
                <input type="date" id="dateButoire" name="promotion.dateButoire" >
                <% afficherErreurs((List<String>) request.getAttribute("error_promotion.dateButoire"), out); %>
            </div>
            
            <div class="form-group">
                <button type="submit">Créer</button>
                <button type="button" class="cancel-button" onclick="window.location.href='<%= request.getContextPath()%>/admin/promotion?id=<%=vol.getId()%>'">Annuler</button>
            </div>
        </form>
    </div>
</body>
</html>