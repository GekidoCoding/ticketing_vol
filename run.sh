#!/bin/bash

# === Configuration ===
TOMCAT_DIR="/opt/tomcat/apache-tomcat-9.0.105"   # ➜ remplace avec ton vrai chemin Tomcat
WAR_FILE="./target/ticketing.war"  # ➜ le WAR généré par ton build
APP_NAME="ticketing"               # ➜ nom du contexte

# === Déploiement ===
echo "[INFO] Suppression ancienne appli..."
rm -rf "$TOMCAT_DIR/webapps/$APP_NAME"
rm -f "$TOMCAT_DIR/webapps/$APP_NAME.war"

echo "[INFO] Copie du WAR vers Tomcat..."
cp "$WAR_FILE" "$TOMCAT_DIR/webapps/"

# === Démarrage Tomcat ===
echo "[INFO] Démarrage de Tomcat..."
"$TOMCAT_DIR/bin/catalina.sh" run