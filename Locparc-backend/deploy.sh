#!/bin/bash

export JAVA_HOME="/usr/lib/jvm/jdk-17"

# Mettre à jour le code source
git pull

# Construire le projet avec Maven
bash mvnw package -P prod,sysadmin  --settings /home/debian/.m2/settings.xml

# Construire l'image Docker
docker build --no-cache -t locparc .

# Arreter le conteneur existant
docker stop locparc

# Supprimer le conteneur existant
docker rm locparc

# Lancer un nouveau conteneur
docker run -d --net debian_default --name=locparc -p 8181:8080 locparc