<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="mg.ticketing.model.Classe" %>
<%@ page import="mg.ticketing.model.Vol" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="mg.ticketing.model.Utilisateur" %>
<%@ page import="mg.ticketing.model.CategorieAge" %>
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
        header {
            background: linear-gradient(90deg, #007bff, #0056b3);
            color: white;
            padding: 1rem 2rem;
            display: flex;
            justify-content: space-between;
            align-items: center;
            border-radius: 0 0 8px 8px;
            box-shadow: 0 2px 6px rgba(0,0,0,0.15);
            width: 100%;
            margin-bottom: 1.5rem;
            margin-top: -2rem;
        }

        header h2 {
            margin: 0;
            font-size: 1.6rem;
            font-weight: 700;
            letter-spacing: 1px;
        }

        header .user-info {
            font-size: 1rem;
            font-weight: 500;
            background: rgba(255,255,255,0.15);
            padding: 0.5rem 1rem;
            border-radius: 20px;
            display: flex;
            align-items: center;
            gap: 0.5rem;
        }

        header .user-info span {
            font-weight: bold;
            color: #ffeb3b;
        }

        @media (max-width: 600px) {
            header {
                flex-direction: column;
                gap: 0.5rem;
                text-align: center;
            }
            header h2 {
                font-size: 1.3rem;
            }
            header .user-info {
                font-size: 0.9rem;
                padding: 0.4rem 0.8rem;
            }
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
          .success-message {
            background-color: #e6ffe6;
            color: #2e7d32;
            padding: 1rem;
            border-radius: 5px;
            margin-bottom: 1rem;
            text-align: center;
            font-size: 0.9rem;
            border: 1px solid #81c784;
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
     <header>
        <h2>Ticketing</h2>
        <div class="user-info">
            Connecté en tant que : 
            <%
                Utilisateur u = (Utilisateur) session.getAttribute("utilisateur");
                if (u != null) {
            %>
                <%= u.getUsername() %>
            <%
                } else {
            %>
                Invité
            <%
                }
            %>
        </div>
    </header>

    <div class="statistic-detail">
        <p><strong>ID:</strong> <%=vol.getId()%></p>
        <p><strong>Ville de Départ:</strong> <%=vol.getVilleDepart().getNom()%></p>
        <p><strong>Ville d'Arrivée:</strong> <%=vol.getVilleArrivee().getNom()%></p>
        <p><strong>Date/Heure de Départ:</strong> <%=vol.getDateHeureDepart()%></p>
    </div>

    <div class="form-container">
        <h1>Reservation de ce vol</h1>
        
        <form method="post" action="<%= request.getContextPath() %>/utilisateur/reservation/reserver">
             <% 
                afficherErreurs((List<String>) request.getAttribute("errors"), out); 
                String successMsg = (String) request.getAttribute("success");
                if (successMsg != null && !successMsg.isEmpty()) {
            %>
                <div class="success-message"><%= successMsg %></div>
            <% } %>
            <input name="volId" type="hidden" value="<%=vol.getId()%>">
            <div class="form-group">
                <label for="classeId">Classe:</label>
                <select id="classeId" name="classeId" >
                    <option value="">Sélectionnez une classe</option>
                    <%
                        List<Classe> classes = (List<Classe>) request.getAttribute("classes");
                        for (Classe classe : classes) { %>
                        <option value="<%=classe.getId()%>"><%=classe.getNom()%></option>
                    <% } %>
                </select>
               
            </div>

            <div class="form-group">
                <label for="placeNeed">Nombre de sièges a reserver:</label>
                <input type="number" id="placeNeed" name="placeNeed" >
            </div>
            <div class="form-group">
                <label for="categorieAgeId">Classe:</label>
                <select id="categorieAgeId" name="categorieAgeId" >
                    <option value="">Sélectionnez une categorie</option>
                    <%
                        List<CategorieAge> categories = (List<CategorieAge>) request.getAttribute("categorieAges");
                        for (CategorieAge categorie : categories) { %>
                        <option value="<%=categorie.getId()%>"><%=categorie.getNom()%></option>
                    <% } %>
                </select>
               
            </div>

            <div class="form-group">
                <label for="reduction">Date de reservation:</label>
                <input type="date" id="dateReservation" name="dateReservation" >
            </div>
            
            <div class="form-group">
                <button type="submit">Reserver</button>
                <button type="button" class="cancel-button" onclick="window.location.href='<%= request.getContextPath()%>/utilisateur/vol/'">Annuler</button>
            </div>
        </form>
    </div>
</body>
</html>