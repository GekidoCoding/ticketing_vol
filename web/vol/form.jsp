<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="mg.ticketing.model.Ville" %>
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
    <title><%= "create".equals(request.getAttribute("action")) ? "Créer" : "Éditer" %> un Vol</title>
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
            .form-container {
                padding: 1.5rem;
            }
            h1 {
                font-size: 1.5rem;
            }
        }
    </style>
</head>
<body>
    <div class="form-container">
        <h1><%= "create".equals(request.getAttribute("action")) ? "Créer un Vol" : "Éditer un Vol" %></h1>
        
        <form method="post" action="<%= request.getContextPath() %>/admin/vol/<%= "create".equals(request.getAttribute("action")) ? "creer" : "modifier" %>">
            <input type="hidden" name="id" value="<%= request.getAttribute("vol.id") != "" ? request.getAttribute("vol.id") : "" %>">
    
            <div class="form-group">
                <label for="avionId">Avion:</label>
                <select id="avionId" name="vol.avionId" >
                    <option value="">Sélectionnez un avion</option>
                    <%
                        List<mg.ticketing.model.Avion> avions = (List<mg.ticketing.model.Avion>) request.getAttribute("avions");
                        String avionId = (String) request.getAttribute("vol.avionId");
                        for (mg.ticketing.model.Avion avion : avions) {
                            boolean selected = avion.getId().toString().equals(avionId);
                    %>
                        <option value="<%= avion.getId() %>" <%= selected ? "selected" : "" %>>
                            <%= avion.getModele() %>
                        </option>
                    <% } %>
                </select>
                <% afficherErreurs((List<String>) request.getAttribute("error_vol.avionId"), out); %>
            </div>

            <div class="form-group">
                <label for="villeDepartId">Ville Départ:</label>
                <select id="villeDepartId" name="vol.villeDepartId" >
                    <option value="">Sélectionnez une ville</option>
                    <% 
                        List<Ville> villes = (List<Ville>) request.getAttribute("villes");
                        String villeDepartId = (String) request.getAttribute("vol.villeDepartId");
                        for (Ville ville : villes) {
                            boolean selected = ville.getId().toString().equals(villeDepartId);
                    %>
                        <option value="<%= ville.getId() %>" <%= selected ? "selected" : "" %>>
                            <%= ville.getNom() %>
                        </option>
                    <% } %>
                </select>
                <% afficherErreurs((List<String>) request.getAttribute("error_vol.villeDepartId"), out); %>
            </div>
            
            <div class="form-group">
                <label for="villeArriveeId">Ville Arrivée:</label>
                <select id="villeArriveeId" name="vol.villeArriveeId" >
                    <option value="">Sélectionnez une ville</option>
                    <% 
                        String villeArriveeId = (String) request.getAttribute("vol.villeArriveeId");
                        for (Ville ville : villes) {
                            boolean selected = ville.getId().toString().equals(villeArriveeId);
                    %>
                        <option value="<%= ville.getId() %>" <%= selected ? "selected" : "" %>>
                            <%= ville.getNom() %>
                        </option>
                    <% } %>
                </select>
                <% afficherErreurs((List<String>) request.getAttribute("error_vol.villeArriveeId"), out); %>
            </div>
            
            <div class="form-group">
                <label for="dateHeureDepart">Date/Heure Départ:</label>
                <input type="datetime-local" id="dateHeureDepart" name="vol.dateHeureDepart" 
                       value="<%= request.getAttribute("vol.dateHeureDepart") != null ? request.getAttribute("vol.dateHeureDepart") : "" %>" >
                <% afficherErreurs((List<String>) request.getAttribute("error_vol.dateHeureDepart"), out); %>
            </div>
            
            <div class="form-group">
                <button type="submit"><%= "create".equals(request.getAttribute("action")) ? "Créer" : "Mettre à jour" %></button>
                <button type="button" class="cancel-button" onclick="window.location.href='<%= request.getContextPath() %>/admin/vol/'">Annuler</button>
            </div>
        </form>
    </div>
</body>
</html>