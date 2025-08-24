<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="mg.ticketing.model.Vol" %>
<%@ page import="mg.ticketing.model.Ville" %>
<%@ page import="mg.ticketing.model.Avion" %>
<%@ page import="mg.ticketing.model.Utilisateur" %>
<%@ page import="mg.ticketing.model.Admin" %>

<html>
<head>
    <title>Gestion des Vols</title>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            background-color: #f0f2f5;
            margin: 0;
            padding: 2rem;
            min-height: 100vh;
            display: flex;
            flex-direction: column;
            align-items: center;
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
        header .nav-links {
            display: flex;
            gap: 1rem;
        }
        header .nav-links a, header .nav-links button {
            color: white;
            text-decoration: none;
            padding: 0.5rem 1rem;
            border-radius: 5px;
            font-size: 1rem;
            transition: background-color 0.3s, transform 0.1s;
            border: none;
            cursor: pointer;
            background: rgba(255,255,255,0.15);
        }
        header .nav-links a:hover, header .nav-links button:hover {
            background: rgba(255,255,255,0.25);
            transform: translateY(-1px);
        }
        header .nav-links button {
            background: #d32f2f;
        }
        header .nav-links button:hover {
            background: #b71c1c;
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
            header .nav-links {
                flex-direction: column;
                width: 100%;
            }
            header .nav-links a, header .nav-links button {
                width: 100%;
                text-align: center;
            }
        }
        .form-container {
            background-color: white;
            padding: 2rem;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 1400px;
            margin-bottom: 1rem;
        }
        .form-container form {
            display: flex;
            flex-wrap: wrap;
            gap: 1.2rem;
        }
        .form-group {
            display: flex;
            flex-direction: column;
            flex: 1;
            min-width: 200px;
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
        .table-container {
            background-color: white;
            padding: 2rem;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 1400px;
            margin-bottom: 2rem;
        }
        h1 {
            text-align: center;
            color: #333;
            margin-bottom: 1.5rem;
            font-size: 1.8rem;
        }
        .success-message {
            background-color: #e6ffe6;
            color: #2e7d32;
            padding: 1rem;
            border-radius: 5px;
            margin-bottom: 1.5rem;
            text-align: center;
            font-size: 0.9rem;
        }
        a.create-link {
            display: inline-block;
            background-color: #007bff;
            color: white;
            padding: 0.8rem 1.5rem;
            border-radius: 5px;
            text-decoration: none;
            margin-bottom: 1.5rem;
            transition: background-color 0.3s, transform 0.1s;
        }
        a.create-link:hover {
            background-color: #0056b3;
            transform: translateY(-1px);
        }
        a.create-link:active {
            transform: translateY(0);
        }
        .table-wrapper {
            max-height: 500px;
            overflow-y: auto;
            margin-bottom: 1rem;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            font-size: 0.95rem;
        }
        th, td {
            padding: 1rem;
            text-align: left;
            border: 1px solid #ddd;
        }
        th {
            background-color: #f8f9fa;
            color: #333;
            font-weight: 600;
            position: sticky;
            top: 0;
            z-index: 1;
        }
        tr:nth-child(even) {
            background-color: #f9f9f9;
        }
        tr:hover {
            background-color: #f1f1f1;
        }
        a.action-link {
            color: #007bff;
            text-decoration: none;
            padding: 0.5rem;
            transition: color 0.3s;
        }
        a.action-link:hover {
            color: #0056b3;
            text-decoration: underline;
        }
        a.delete-link {
            color: #d32f2f;
        }
        a.delete-link:hover {
            color: #b71c1c;
        }
        a.voir-link {
            color: #2e7d32;
        }
        a.voir-link:hover {
            color: #1b5e20;
        }
        @media (max-width: 768px) {
            .table-container, .form-container {
                margin: 1rem;
                padding: 1.5rem;
            }
            table {
                font-size: 0.9rem;
            }
            th, td {
                padding: 0.75rem;
            }
            .form-container form {
                flex-direction: column;
            }
        }
        @media (max-width: 480px) {
            .table-container, .form-container {
                padding: 1rem;
            }
            table {
                font-size: 0.85rem;
            }
            th, td {
                padding: 0.5rem;
            }
        }
    </style>
</head>
<body>
    <header>
        <h2>Ticketing</h2>
        <div class="nav-links">
            <a href="<%= request.getContextPath() %>/utilisateur/vol/">Vols</a>
            <a href="<%= request.getContextPath() %>/utilisateur/reservations/">Vos Réservations</a>
            <button onclick="window.location.href='<%= request.getContextPath() %>/utilisateur/logout'">Déconnexion</button>
        </div>
        <div class="user-info">
            Connecté en tant que : 
            <%
                Admin u = (Admin) session.getAttribute("utilisateur");
                if (u != null) {
            %>
                <span><%= u.getUsername() %></span>
            <%
                } else {
            %>
                Invité
            <%
                }
            %>
        </div>
    </header>

    <div class="form-container">
        <form method="get" action="<%= request.getContextPath() %>/admin/vol/search">
            <div class="form-group">
                <label for="villeDepartId">Ville de Départ:</label>
                <select id="villeDepartId" name="villeDepartId">
                    <option value="">Sélectionnez une ville</option>
                    <%
                        List<Ville> villes = (List<Ville>) request.getAttribute("villes");
                        if (villes != null) {
                            for (Ville ville : villes) {
                    %>
                        <option value="<%= ville.getId() %>"><%= ville.getNom() %></option>
                    <% 
                            }
                        }
                    %>
                </select>
            </div>
            <div class="form-group">
                <label for="villeArriveeId">Ville d'Arrivée:</label>
                <select id="villeArriveeId" name="villeArriveeId">
                    <option value="">Sélectionnez une ville</option>
                    <%
                        if (villes != null) {
                            for (Ville ville : villes) {
                    %>
                        <option value="<%= ville.getId() %>"><%= ville.getNom() %></option>
                    <% 
                            }
                        }
                    %>
                </select>
            </div>
            <div class="form-group">
                <label for="avionId">Avion:</label>
                <select id="avionId" name="avionId">
                    <option value="">Sélectionnez un avion</option>
                    <%
                        List<Avion> avions = (List<Avion>) request.getAttribute("avions");
                        if (avions != null) {
                            for (Avion avion : avions) {
                    %>
                        <option value="<%= avion.getId() %>"><%= avion.getModele() %></option>
                    <% 
                            }
                        }
                    %>
                </select>
            </div>
            <div class="form-group">
                <label for="date1">Date de début:</label>
                <input type="date" id="date1" name="date1">
            </div>
            <div class="form-group">
                <label for="date2">Date de fin:</label>
                <input type="date" id="date2" name="date2">
            </div>
            <button type="submit">Rechercher</button>
        </form>
    </div>

    <div class="table-container">
        <h1>Liste des Vols</h1>
        
        <% if (request.getAttribute("success") != null) { %>
            <div class="success-message"><%= request.getAttribute("success") %></div>
        <% } %>
        
        <a href="<%= request.getContextPath() %>/admin/vol/nouveau" class="create-link">Créer un nouveau vol</a>
        
        <div class="table-wrapper">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Avion</th>
                        <th>Companie</th>
                        <th>Ville Départ</th>
                        <th>Ville Arrivée</th>
                        <th>Date/Heure Départ</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% 
                    List<Vol> vols = (List<Vol>) request.getAttribute("vols");
                    for (Vol vol : vols) { 
                    %>
                        <tr>
                            <td><%= vol.getId() %></td>
                            <td><%= vol.getAvion().getModele() %></td>
                            <td><%= vol.getAvion().getCompany().getNom() %></td>
                            <td><%= vol.getVilleDepart().getNom() %></td>
                            <td><%= vol.getVilleArrivee().getNom() %></td>
                            <td><%= vol.getDateHeureDepart() %></td>
                            <td>
                                <a href="<%= request.getContextPath() %>/admin/vol/editer?id=<%= vol.getId() %>" class="action-link">Éditer</a>
                                <a href="<%= request.getContextPath() %>/admin/vol/supprimer?id=<%= vol.getId() %>" 
                                   class="action-link delete-link" 
                                   onclick="return confirm('Êtes-vous sûr de vouloir supprimer ce vol?')">Supprimer</a>
                                <a href="<%= request.getContextPath() %>/admin/promotion?id=<%= vol.getId() %>" 
                                   class="action-link voir-link">Voir Promotions</a>
                            </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>