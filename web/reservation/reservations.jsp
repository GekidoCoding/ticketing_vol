<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="mg.ticketing.model.Vol" %>
<%@ page import="mg.ticketing.model.Ville" %>
<%@ page import="mg.ticketing.model.Avion" %>
<%@ page import="mg.ticketing.model.Utilisateur" %>
<%@ page import="mg.ticketing.model.Admin" %>
<%@ page import="mg.ticketing.model.Reservation" %>
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
        .error-message {
            background-color: #fff1f0;
            color: #c62828;
            padding: 1.2rem;
            border-radius: 8px;
            margin-bottom: 1.5rem;
            text-align: left;
            font-size: 0.95rem;
            border: 1px solid #ef9a9a;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            animation: slideIn 0.3s ease-in-out;
            position: relative;
            overflow: hidden;
        }
        .error-message::before {
            content: '⚠';
            font-size: 1.2rem;
            margin-right: 0.5rem;
            vertical-align: middle;
        }
        .error-message span {
            display: block;
            margin: 0.5rem 0;
            padding-left: 1.8rem;
        }
        @keyframes slideIn {
            from {
                transform: translateY(-20px);
                opacity: 0;
            }
            to {
                transform: translateY(0);
                opacity: 1;
            }
        }
        .success-message {
            background-color: #e6ffe6;
            color: #2e7d32;
            padding: 1.2rem;
            border-radius: 8px;
            margin-bottom: 1.5rem;
            text-align: left;
            font-size: 0.95rem;
            border: 1px solid #a5d6a7;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            animation: slideIn 0.3s ease-in-out;
        }
        .success-message::before {
            content: '✔';
            font-size: 1.2rem;
            margin-right: 0.5rem;
            vertical-align: middle;
        }
        h1 {
            text-align: center;
            color: #333;
            margin-bottom: 1.5rem;
            font-size: 1.8rem;
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
        .confirm-popup {
            display: none;
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            background-color: white;
            padding: 2rem;
            border-radius: 8px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
            z-index: 1000;
            max-width: 400px;
            width: 90%;
            text-align: center;
        }
        .confirm-popup h3 {
            margin: 0 0 1rem;
            color: #333;
            font-size: 1.2rem;
        }
        .confirm-popup p {
            margin: 0 0 1.5rem;
            color: #555;
            font-size: 0.95rem;
        }
        .confirm-popup .buttons {
            display: flex;
            justify-content: center;
            gap: 1rem;
        }
        .confirm-popup button {
            padding: 0.6rem 1.2rem;
            border-radius: 5px;
            font-size: 0.95rem;
            cursor: pointer;
            transition: background-color 0.3s, transform 0.1s;
        }
        .confirm-popup .confirm-btn {
            background-color: #d32f2f;
        }
        .confirm-popup .confirm-btn:hover {
            background-color: #b71c1c;
            transform: translateY(-1px);
        }
        .confirm-popup .cancel-btn {
            background-color: #007bff;
        }
        .confirm-popup .cancel-btn:hover {
            background-color: #0056b3;
            transform: translateY(-1px);
        }
        .popup-overlay {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.5);
            z-index: 999;
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
            .confirm-popup {
                padding: 1.5rem;
                width: 95%;
            }
        }
    </style>
</head>
<body>
    <% Utilisateur u = (Utilisateur) session.getAttribute("utilisateur"); %>
    <header>
        <h2>Ticketing</h2>
        <div class="nav-links">
            <a href="<%= request.getContextPath() %>/utilisateur/vol/">Vols</a>
            <a href="<%= request.getContextPath() %>/utilisateur/reservations">Vos Réservations</a>
            <button onclick="window.location.href='<%= request.getContextPath() %>/auth/logout'">Déconnexion</button>
        </div>
        <div class="user-info">
            Connecté en tant que : 
            <%
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

    <% 
        afficherErreurs((List<String>) request.getAttribute("errors"), out); 
        String successMsg = (String) request.getAttribute("success");
        if (successMsg != null && !successMsg.isEmpty()) {
    %>
        <div class="success-message"><%= successMsg %></div>
    <% } %>

    <div class="table-container">
        <h1>Vos Reservations</h1>
        <div class="table-wrapper">
            <table>
                <thead>
                    <tr>
                        <th>Statut</th>
                        <th>Avion</th>
                        <th>Companie</th>
                        <th>Ville Départ</th>
                        <th>Ville Arrivée</th>
                        <th>Date/Heure Départ</th>
                        <th>Date de reservation</th>
                        <th>Classe</th>
                        <th>Nombre de sieges reservees</th>
                        <th>Prix total</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% 
                    List<Reservation> reservations = (List<Reservation>) request.getAttribute("reservations");
                    for (Reservation reservation : reservations) { 
                    %>
                        <tr>
                            <td><%= reservation.getStatut().getNom() %></td>
                            <td><%= reservation.getVol().getAvion().getModele() %></td>
                            <td><%= reservation.getVol().getAvion().getCompany().getNom() %></td>
                            <td><%= reservation.getVol().getVilleDepart().getNom() %></td>
                            <td><%= reservation.getVol().getVilleArrivee().getNom() %></td>
                            <td><%= reservation.getVol().getDateHeureDepart() %></td>
                            <td><%= reservation.getDateReservation() %></td>
                            <th><%= reservation.getClasse().getNom() %></th>
                            <th><%= reservation.getNbSieges() %></th>
                            <th><%= reservation.getPrix() %></th>
                            <td>
                                <a href="<%= request.getContextPath() %>/promotion/reservation?reservationId=<%= reservation.getId() %>" 
                                   class="action-link voir-link">Voir Promotions</a>
                                <a href="#" 
                                   class="action-link delete-link cancel-reservation" 
                                   data-url="<%= request.getContextPath() %>/promotion/reservation/annuler?reservationId=<%= reservation.getId() %>">Annuler</a>
                            </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>

    <div class="popup-overlay"></div>
    <div class="confirm-popup">
        <h3>Confirmation</h3>
        <p>Voulez-vous vraiment annuler votre réservation ?</p>
        <div class="buttons">
            <button class="confirm-btn">Confirmer</button>
            <button class="cancel-btn">Annuler</button>
        </div>
    </div>

    <script>
        document.addEventListener('DOMContentLoaded', () => {
            const cancelLinks = document.querySelectorAll('.cancel-reservation');
            const popup = document.querySelector('.confirm-popup');
            const overlay = document.querySelector('.popup-overlay');
            const confirmBtn = document.querySelector('.confirm-btn');
            const cancelBtn = document.querySelector('.cancel-btn');
            let cancelUrl = '';

            cancelLinks.forEach(link => {
                link.addEventListener('click', (e) => {
                    e.preventDefault();
                    cancelUrl = link.getAttribute('data-url');
                    popup.style.display = 'block';
                    overlay.style.display = 'block';
                });
            });

            confirmBtn.addEventListener('click', () => {
                window.location.href = cancelUrl;
            });

            cancelBtn.addEventListener('click', () => {
                popup.style.display = 'none';
                overlay.style.display = 'none';
                cancelUrl = '';
            });

            overlay.addEventListener('click', () => {
                popup.style.display = 'none';
                overlay.style.display = 'none';
                cancelUrl = '';
            });
        });
    </script>
</body>
</html>