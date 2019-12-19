# CS-107 - Mini projet 2 - Mini jeu

### Edouard Michelin & Julien Jordan

---

![QR Code - I love java](https://i.pinimg.com/600x315/84/63/a9/8463a9a0886485b8d3504b9ce32ea7e6.jpg)

---

## Contexte

Ce projet a été donné dans le cadre du cours d'introduction à la programmation (CS-107).

## Objectif

L'objectif premier de ce projet est d'être capable d'appliquer les premiers principes de programmation enseignés lors du cours dans le but de pouvoir mettre en pratique l'aspect orienté objet de la programmation.

---

## Partie 1 (ARPG de base)

Préparation du jeu ARPG. Premiers personnages et premières intéractions.

## Partie 2 (Points de vie et ressources)

Implémentation des points de vie du personnage et de l'inventaire.

## Partie 3 (Monstres et batailles)

Certainement la partie la plus longue, création et implémentation des monstres ainsi que des moyens de les combattre.

## Partie 4 (Extensions)

Nous avons travaillé différents bonus, à savoir

- Affichage d'une cascade sur l'aire `Route`
- Possibilité de pouvoir courir à l'aide de la touche `X`
- Ajout de plusieurs aires (`MaisonFerme`, `Grotte`, `RouteTemple`, `Temple`)
- Ajout de personnages non joueurs (PNJ) avec lesquels il est possible d'intéragir avec la touche `E` (les PNG sont soit immobiles, soit en mouvement. Les PNJ en mouvement s'arrêtent auprès du joueur en attendant que vous vouliez bien leur parler)
- Ajout de fleurs
- Ajout d'un nouveau monstre (Gobelin) dans la grotte
- Ajout d'items ramassables à différents endroits (VOIR SCENARIO)
- Ajout d'éléments réagissant à un signal (Pont sur l'aire route avec l'orbe)
- Il est possible de passer de l'affichage de l'argent à l'adffichage de la fortune


## Scénario

Le scénario (assez simple) se déroule de la manière suivante

- Le personnage principal apparait dans un nouvel univers chez des inconnus.
- Il sort ensuite, parle avec les différents villageois et en apprend plus sur son environnement.
- En tentant de sortir de la ferme par le Nord-Est, il découvre alors qu'il est bloqué par de solides touffes d'herbe.
- Il peut en revanche sortir de la ferme par le côté Sud-Ouest pour découvrir le village.
- Dans le village il rencontre de nouveau villageois. Il se rend alors compte que lesdits habitants de ce monde ne vont pas lui être d'une grande utilité.
- En se promenant dans le village, et en tentant de sortir par le coté Nord-Est, il tombe à nouveau sur de solides et infranchissables touffes d'herbe.
- En revenant et en se promenant sur la plage il trouve une épée qu'il s'empresse de ramasser.
- Cette épée lui servira à couper les hautes herbes ; il pourra enfin pleinement accéder à l'aire route.
- De féroces buches lui font face quand il essaye de s'approcher des quelques bombes laissées sur la route, qui lui serviront plus tard.
- En sortant par le coté Nord de la route, il tombe sur le chateau de la ville, gardé par le terrible seigneur des tenebres, qu'il ne peut pour le moment combattre.
- Il revient alors au village et découvre une faille dans le mur coté Nord.
- Bien évidemment, il décide alors de faire usage des bombes qu'il a auparavent trouvées pour forcer le passage de ce qui semble être l'entrée d'une grotte.
- En arrivant dans la grotte, il y découvre le méchant vilain gobelin, contaminé, qui le blessera au moindre contact.
- Voulant débarrasser le village de la vermine, il s'empresse de le zigouiller à coups d'épée.
- Le gobelin laisse alors tomber un arc de sa poche. Notre personnage pourra alors s'en servir avec les flèches qu'il avait bien évidemment dans son carquois au début de l'aventure.
- Il remonte ensuite vers la route pour, par pur hasard, tirer à l'arc sur l'orbe magique qui laisse alors apparaitre un pont magique.
- L'esprit aventurier, il prend le chemin de ce pont pour arriver vers un temple magique qui renferme un baton magique.
- Il découvre alors les pouvoirs magiques de son bâton et en déduit qu'il pourra remonter les bretelles du seigneur des ténebres.
- Une fois cela fait, il s'apercoit que le méchant a laissé tombé une clef, qui n'est autre que la clef du chateau.
- Il y rentre et y découvre alors un étrange personnage lui annonçant qu'il a finit le jeu.

## Attribution des touches

    public static int MOVE_UP = Keyboard.UP;
    public static int MOVE_DOWN = Keyboard.DOWN;
    public static int MOVE_LEFT = Keyboard.LEFT;
    public static int MOVE_RIGHT = Keyboard.RIGHT;
    public static int INTERACTION_KEY = Keyboard.E;
    public static int RUN = Keyboard.X;
    public static int SWITCH_ITEM = Keyboard.TAB;
    public static int SWITCH_COINS_DISPLAY = Keyboard.L;
    public static int CONSUME_ITEM = Keyboard.SPACE;