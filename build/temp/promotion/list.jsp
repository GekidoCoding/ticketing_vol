<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="mg.ticketing.model.Promotion" %>
<%@ page import="mg.ticketing.model.Vol" %>

<html>
<head>
    <title>Promotions pour ce vol</title>
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
        .statistic-detail {
            background-color: white;
            padding: 1.5rem;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 1400px;
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
            color: #d32f2f;
            text-decoration: none;
            padding: 0.5rem;
            transition: color 0.3s;
        }
        a.action-link:hover {
            color: #b71c1c;
            text-decoration: underline;
        }
        @media (max-width: 768px) {
            .table-container, .statistic-detail {
                margin: 1rem;
                padding: 1.5rem;
            }
            table {
                font-size: 0.9rem;
            }
            th, td {
                padding: 0.75rem;
            }
        }
        @media (max-width: 480px) {
            .table-container, .statistic-detail {
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
    <% Vol vol = (Vol)request.getAttribute("vol"); %>
    
    <div class="statistic-detail">
        <p><strong>ID:</strong> <%=vol.getId()%></p>
        <p><strong>Ville de Départ:</strong> <%=vol.getVilleDepart().getNom()%></p>
        <p><strong>Ville d'Arrivée:</strong> <%=vol.getVilleArrivee().getNom()%></p>
        <p><strong>Date/Heure de Départ:</strong> <%=vol.getDateHeureDepart()%></p>
       
    </div>

    <div class="table-container">
        <h1>Liste des Promotions pour ce vol</h1>
        
        <% if (request.getAttribute("success") != null) { %>
            <div class="success-message"><%= request.getAttribute("success") %></div>
        <% } %>
        
        <a href="<%= request.getContextPath() %>/admin/promotion/nouveau?id=<%=vol.getId()%>" class="create-link">Créer une promotion</a>
        
        <div class="table-wrapper">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Classe</th>
                        <th>Nombre de sièges disponibles</th>
                        <th>Réduction (%)</th>
                        <th>Prix </th>
                        <th>Date Butoire </th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% 
                    List<Promotion> promotions = (List<Promotion>)request.getAttribute("promotions");
                    for (Promotion promotion : promotions) { 
                    %>
                        <tr>
                            <td><%= promotion.getId() %></td>
                            <td><%= promotion.getClasse().getNom() %></td>
                            <td><%= promotion.getNbrSiegeDispo() %></td>
                            <td><%= promotion.getReduction() %></td>
                            <td><%= promotion.getPrix() %></td>
                            <td><%= promotion.getDateButoire() %></td>
                            
                            <td>
                                <a href="<%= request.getContextPath() %>/admin/promotion/supprimer?id=<%=promotion.getId()%>" 
                                   class="action-link" 
                                   onclick="return confirm('Êtes-vous sûr de vouloir supprimer cette promotion?')">Supprimer</a>
                            </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>