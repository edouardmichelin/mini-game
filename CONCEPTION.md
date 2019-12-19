## Structure générale

Nous avons en grande partie suivie la structure générale proposée par l'énoncé.

Afin d'éviter au maximum l'utilisation non maintenable de valeurs littérales dans le code, nous avons regoupé plusieurs constantes et méthodes générales dans des classes de configuration / des classes de données / des classes helpers.
Ces classes peuvent être trouvées dans les packages `*arpg.config.*` et `*rpg.misc.*`

Des classes d'items, en plus de l'énumération `ARPGItems` ont été ajoutées afin de regrouper la configuration. Ces classes (`*arpg.items*`) contiennent aussi des méthodes permettant de construire les objets auxquels ils sont reliés (ex `drop()`, `consume()`), évitant par la même occasion de laisser la construction d'objets aux acteurs qui les invoquent.
Ces classes agissent ainsi comme des <i>Factory</i>.

Certaines interfaces et abstractions ont été implémentées dans le code afin d'obtenir une meilleure encapsulation des méthodes et de regrouper les classes concrètes en types plus généraux. (`CollectibleAreaEntity`, `Dropable`, `Destroyable`)

## Directives indirectement suivies

- Les flèches n'éteignent pas toutes les flammes d'une ligne (elles sont détruites après le contact avec la première flamme) mais rasent toute une ligné d'herbe.
