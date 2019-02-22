# VideoGen

## Binôme 

* Jammal Mahmoud
* Moutarajji Mouhyi


## Projet

[Sujet du projet](https://docs.google.com/document/d/1_PBrBHf9irX9g8LcRIlRNAVC08lXCZNp3w8dWrpFIPg/edit#)

## Introduction :

L’objectif du projet est d’offrir une solution logicielle pour déployer un générateur ou
Web de vidéos à partir d’une spécification textuelle (VideoGen). Les utilisateurs
du site Web pourront par exemple visualiser des variantes de vidéos générées aléatoirement ou
avec des probabilités. Les créateurs de générateurs de vidéos pourront utiliser le langage
VideoGen ainsi que quelques outils pour raisonner sur la durée et taille des vidéos.

## Solution : 

Avant de commencer de développer l’outil qui génère les variantes, nous avons commencer par réfléchir à comment couvrir toutes les variantes possibles. Donc nous avons implémenté des méthodes qui permet calculer toutes les combinaisons possibles pour les optionnelles et les alternatives dans le but de créer plusieurs variantes et pas une seule vidéo générer à chaque fois. 

Nous avons testé nous méthodes implémentés via des testes fonctionnels et à l’aide aux tableaux mathématiques de logique. 

Après avoir générer une liste de variantes, nous exécutons ffmpeg pour  générer la concaténation des variantes. Nous générons aussi un GIF ainsi qu’un fichier CSV qui contient toutes la variantes avec un attribut TRUE ou FALSE pour indiquer si elles sont présentes dans la liste générée. Le fichier CSV contient aussi la taille et la durée totale des variantes. 
![GitHub Logo](/screenCSV_File_IDM.png)

## Fonctionnalités du générateur : 
  * concaténation des séquences vidéos avec ffmpeg. 
  * gestion des probabilités lors du tirage aléatoire des variantes. 
  * export GIF des variantes vidéo.
  * traitement/filtres sur les vidéos.
  * outil d’analyse pour produire les durées des variantes.
  * outil d’analyse pour produire les tailles des variantes.

## Difficultés rencontrées : 
* Le plus difficile de ce projet, c’était de comprendre le sujet et la grammaire. Dans le cas pour gérer les optionnelles, nous avons hésité entre les mettre une seule fois dans la vidéo générée ou plusieurs fois. Au final nous avons décidé de les mettre plusieurs fois et la taille de la variante est 2^⁽nb optionnelles). 
* Pour la génération des log, nous avons pas pu associé chaque videoGen à plusieurs video séquence, du coup nous avons pas pu générer les logs.
* La prise en main de ffmpeg n'a pas été facile, nous avons eu des problèmes après la génération des vidéo liés à la qualité de la vidéo généré. 
* Nous avons pas pu développer un front pour visualiser les vidéo générer car nous avons essayé de créer l’application via Jhipster et c’était difficile de comprendre le fonctionnement et nous avons pas eu le temps de créer une autre technologie. 
