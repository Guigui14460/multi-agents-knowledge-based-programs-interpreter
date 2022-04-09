# Multi-agent knowledge-based program interpreter

University project proposed in the subject "Personal supervised work" in 1st year of MSc of Computer Science at the University of Caen Normandy. The main goal is to use our knowledge and raisonning capacity to succeed to implement the chosen project.

## Table of contents

  - [Table of contents](#table-of-contents)
  - [Introduction](#introduction)
  - [Setup](#setup)
  - [Commands](#commands)
  - [Authors](#authors)
  - [License](#license)

## Introduction
In this project supervised by [Bruno ZANUTTINI](https://zanuttini.users.greyc.fr/), we realized a multi-agent knowledge-based program interpreter in order to complete a code library maintained by the [MAD (Models, Agents, Decision) team](https://www.greyc.fr/en/equipes/mad-2/) of the [computer science laboratory GREYC](https://www.greyc.fr/en/home/) of the University of Caen Normandy. 

Our interpreter is able to solve problems of dynamic epistemic logic by implementing the desired environment and calling it the appropriate methods of the interpreter to start making automatic deductions.

## Setup
To install the packages needed to use the application, you can run the command sequence specified below:
```sh
$ sudo apt-get update
$ sudo apt-get install openjdk-8-jdk openjdk-8-doc openjdk-8-jre ant
```

## Commands
- To launch the application (the very first time), you can launch :
```bash
$ ant run
```
or simply
```bash
$ ant
```
- To launch the application without rebuilding everything, you can launch :
```bash
$ ant run_dist
```

- To run the unit tests of the implemented classes, you can run :
```bash
$ ant test
```

- For a complete list of ANT targets:
```bash
$ ant -p
```

## Authors
- [PIERRE Corentin (21803752)](https://github.com/coco-ia)
- [LETELLIER Guillaume](https://github.com/Guigui14460)

## License
Project under the "GNU General Public License v3.0" license.
