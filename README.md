Gestion des Réservations de Vols - README
Ce document décrit les principales fonctionnalités du système de gestion des réservations de vols, divisé en deux parties : Frontend (JSP) et Backend (MVC + JSP). L'estimation totale du projet est de 80 heures, avec un buffer recommandé de 10-15% pour les imprévus (~90 heures). Si le projet part de zéro, ajouter 20-30 heures pour l'initialisation.
Sommaire

Vue d'ensemble
Fonctionnalités Frontend
Fonctionnalités Backend
Répartition des efforts
Notes importantes

Vue d'ensemble
Le système permet aux utilisateurs de gérer des réservations de vols, incluant l'authentification, la gestion des vols, des promotions, des catégories d'âge, et des paramètres. Il est construit avec une architecture MVC, des pages JSP pour l'interface utilisateur, et une base de code réutilisable pour les services, DAO, et utilitaires.
Fonctionnalités Frontend
Les fonctionnalités frontend sont implémentées via des pages JSP, avec un design responsive et des validations client (CSS/JS global : 6 heures).
1. Page de Connexion (/auth/login.jsp) - 3 heures

Formulaire de connexion (1 heure) : Champs pour identifiant et mot de passe.
Bouton de connexion (0.5 heure) : Déclenche l'authentification.
Messages d'erreur (1 heure) : Affichage des erreurs (ex. identifiants incorrects).
Redirection après succès (0.5 heure) : Redirige vers la page principale après connexion.

2. Page de Réservation (/reservation/index.jsp) - 8 heures

Sélection du vol (1 heure) : Liste déroulante des vols disponibles.
Sélection de la classe (0.5 heure) : Choix de la classe (ex. économie, business).
Informations des passagers (1 heure) : Saisie des détails des passagers.
Bouton "Choisir" et vérifications (2 heures) : Validation des sélections et confirmation.
Statistiques du vol (2 heures) : Affichage des informations (ex. disponibilité, horaires).
Tableau "Mes réservations" (1 heure) : Liste des réservations de l'utilisateur.
Annulation de réservation (0.5 heure) : Option pour annuler une réservation.

3. Page de Gestion des Catégories d'Âge - 4 heures

Liste des catégories (1 heure) : Tableau affichant les catégories d'âge.
Bouton "Ajouter une catégorie" (0.5 heure) : Ouvre le formulaire d'ajout.
Formulaire d'ajout (2.5 heures) : Saisie et validation des nouvelles catégories.

4. Page de Paramétrage - 2 heures

Formulaire de paramétrage (1 heure) : Configuration des paramètres système.
Bouton "Enregistrer" (0.5 heure) : Sauvegarde des modifications.
Messages (0.5 heure) : Affichage des messages de succès ou d'erreur.

5. Page de Gestion des Vols - 6 heures

Liste des vols (2 heures) : Tableau des vols disponibles.
Bouton "Ajouter un vol" (0.5 heure) : Ouvre le formulaire de création.
Formulaire créer/modifier (3.5 heures) : Saisie des détails du vol (ex. départ, arrivée, avion).

6. Page des Promotions par Vol - 3 heures

Liste des promotions (2 heures) : Tableau des promotions associées à un vol.
Bouton "Ajouter promotion" (1 heure) : Ouvre le formulaire d'ajout.

7. Page d'Ajout de Promotion - 3 heures

Formulaire de promotion (2 heures) : Saisie des détails (ex. réduction, période).
Bouton "Enregistrer" (0.5 heure) : Sauvegarde de la promotion.
Validations (0.5 heure) : Vérifications des données saisies.

Fonctionnalités Backend
Le backend suit une architecture MVC avec des modèles, DAO, services, et contrôleurs, appuyés par des utilitaires et des filtres (total : 45 heures).
1. Infrastructure et Utilitaires - 6 heures

Filtres/Intercepteurs (AuthFilter) (2 heures) : Gestion de l'authentification et des autorisations.
Utilitaires (DatabaseConnection, DateUtil, PriceUtil) (2 heures) : Fonctions communes (connexion DB, formatage des dates, calcul des prix).
Services génériques (2 heures) : Base pour les services (AuthService, ReservationService).

2. Authentification - 5 heures

Modèle (Admin, Utilisateur) (1 heure) : Entités pour les utilisateurs et administrateurs.
DAO (AdminDAO, UtilisateurDAO) (2 heures) : Accès aux données des utilisateurs.
Service (AuthService) (1 heure) : Logique d'authentification.
Controller (AuthController) (1 heure) : Gestion des requêtes de connexion/déconnexion.

3. Paramétrage - 3 heures

DAO (ParametrageDAO) (1 heure) : Gestion des paramètres en base.
Service (ParametrageService) (1 heure) : Logique de gestion des paramètres.
Controller (ParametrageController) (1 heure) : Traitement des requêtes de paramétrage.

4. Catégorie d'Âge - 6 heures

Modèle (CategorieAge) (1 heure) : Entité pour les catégories d'âge.
DAO (CategorieAgeDAO) (2 heures) : Gestion des catégories en base.
Service (CategorieAgeService) (2 heures) : Logique métier pour les catégories.
Controller (CategorieAgeController) (1 heure) : Gestion des requêtes CRUD.

5. Gestion des Vols - 7 heures

Modèle (Vol) (1 heure) : Entité pour les vols.
DAOs (VolDAO, VilleDAO, AvionDAO) (3 heures) : Gestion des vols, villes, et avions.
Validations (1 heure) : Vérifications des données des vols.
Controller (VolController) (2 heures) : Gestion des requêtes CRUD pour les vols.

6. Promotions - 6 heures

Modèle (Promotion) (1 heure) : Entité pour les promotions.
DAO (PromotionDAO) (2 heures) : Gestion des promotions en base.
Service (PromotionService) (2 heures) : Logique métier pour les promotions.
Controller (PromotionController) (1 heure) : Gestion des requêtes CRUD.

7. Réservations - 12 heures

Modèle (Reservation) (2 heures) : Entité pour les réservations.
DAOs (ReservationDAO, VolDAO, etc.) (4 heures) : Gestion des réservations et dépendances.
Services (ReservationService) (3 heures) : Logique métier pour les réservations.
Controller (ReservationController) (3 heures) : Gestion des requêtes CRUD et annulations.

Répartition des efforts

Frontend (JSP) : 35 heures
Backend (MVC) : 45 heures
Modèles : 6 heures
DAO : 14 heures
Services : 10 heures
Contrôleurs : 10 heures
Infra/Utilitaires : 5 heures


Total estimé : 80 heures

Notes importantes

Les estimations incluent des tests unitaires de base et une intégration minimale.
Un buffer de 10-15% (~90 heures) est recommandé pour les imprévus.
Les tâches complexes (réservations, promotions) sont légèrement surévaluées pour couvrir les cas limites.
Si le projet part de zéro, ajouter 20-30 heures pour l'initialisation (framework MVC, templates JSP).

