# Interpréteur de programmes à base de connaissances multi-agents

## Setup
Pour installer les packages nécessaires à l'utilisation de l'application, vous pouvez exécuter la suite de commandes spécifiées ci-dessous :
```sh
$ sudo apt-get update
$ sudo apt-get install openjdk-8-jdk openjdk-8-doc openjdk-8-jre ant
```

## Commandes
- Pour lancer l'application (la toute première fois), vous pouvez lancer :
```bash
$ ant run
```
ou simplement
```bash
$ ant
```
- Pour lancer l'application sans tout reconstruire, vous pouvez lancer :
```bash
$ ant run_dist
```

- Pour lancer les tests unitaires des classes implémentées, vous pouvez lancer :
```bash
$ ant test
```

- Pour connaître la liste complète des cibles ANT :
```bash
$ ant -p
```
