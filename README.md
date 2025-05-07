# Application Web pour la getions des patients 

* On utilisant Spring MVC avec Thymeleaf et Sécurité avec Spring security


### Sécurité avec Spring security :

* Pour utiliser Spring sécurité, il faut d'abord ajouter la dépendance Spring sécurité, on peut l'ajouter à traver le site web starter.spring.io:
puis ajouter la dépendance dans le fichier pom.xml : 

<br>
    <img src="captures/dep_securite.png">
*Aprés d'avoir ajouté spring sécurité, spring suppose que tous les 
requettes doivent etre authentifier, et génere une interface d'authentification et aussi un mot de passe pour acceder

Password: 

<img src="captures/pass.png">

Login Interface:

<img src="captures/login.png">

#### NOTES: 
* Cette configuration est juste pour la periode de developement.

### Personnalisé la configuration de spring security: 
* Il faut creé une classe de configuration SecurityConfig :

### In Memory Authentication: 
* Pour définir les utilisateurs ayant le droit d’accéder à l’application, il faut spécifier où Spring Security va chercher ces utilisateurs.

* Parmi les méthodes disponibles, il y a l’authentification en mémoire, qui consiste à définir directement dans le code quels sont les utilisateurs autorisés à accéder à l’application.
<img src="captures/classSec.png">


* Il existe également une méthode permettant de protéger les méthodes via des annotations. Il suffit d’ajouter l’annotation @EnableMethodSecurity(prePostEnabled = true).

* Au niveau des contrôleurs, il faut spécifier quelles méthodes doivent être protégées et avec quels rôles elles doivent être accessibles.

### EXEMPLE :
<img src="captures/roleAdmin.png">

* Après avoir ajouté l’annotation @PreAuthorize("hasRole('ROLE_ADMIN')"), seuls les administrateurs pourront exécuter cette méthode.

#### Note :
* Cette méthode de sécurité permet d’éviter l’escalade de privilèges. Elle empêche les utilisateurs non autorisés d’accéder à des fonctionnalités qui ne leur sont pas destinées.
### Encodeur SprintSecurity :
* Spring Security utilise par défaut un encodeur de mots de passe (hasher).
Pour indiquer qu’un mot de passe n’est pas haché, on peut ajouter le préfixe {noop} devant le mot de passe.

* Cependant, il est fortement recommandé de créer un PasswordEncoder pour sécuriser les mots de passe.

* On peut, par exemple, utiliser BCryptPasswordEncoder, un algorithme robuste qui permet le hachage sécurisé des mots de passe.

### Afficher l'utilisateur qui est authentifié : 
* Premierement il faut ajouter une dependance de tymeleaf nommer "thymeleaf extras springsecurity6"

<img src="captures/thymleaf.png">

* Pour afficher le nom de l'utilisateur, il faut modifier le template en ajoutant l'expression suivante :
    * th:text="${#authentication.name}"

* Bien sûr, il existe d'autres méthodes, mais celle-ci est la plus simple.

<img src="captures/auth.png">

* Affichage de l'utilisateur Authentifier (User1) : 

<img src="captures/user.png">

### Ajouter le LOGOUT : 
* On peut Utiliser "th:href="@{/logout}" , mais il y a d'autres methodes.

* Quand on fait un logout, cela ne doit pas se faire via une requête GET, pour des raisons de sécurité.
 En effet, Spring Security ne permet pas de déclencher une action sensible comme la déconnexion via une requête GET, 
 afin d’éviter les attaques de type CSRF. 
##### Ainsi, pour se déconnecter correctement :
  * On effectue un clic sur un bouton.
  * Ce bouton envoie une requête POST à Spring Security, qui traite la déconnexion.

    <img src="captures/buttonlog.png">


##### On peut aussi creer un formulaire :

 <img src="captures/formlogout.png">

* Actuellement, lorsque l’on clique sur le bouton Logout, on est directement redirigé vers la page de déconnexion, sans passer par 
le mécanisme sécurisé de Spring Security (le bouton utilisant une requête POST). 


<img src="captures/logout.png">

### Droits d’accès et Roles :
* La gestion des accès et des rôles est un aspect indispensable dans la gestion des utilisateurs, afin de préserver la sécurité, notamment la confidentialité.

* Pour cela, il faut créer une méthode dans la classe SecurityConfig comme suit :

  <img src="captures/securityRole.png">
***** 
### Note :
* Après l'ajout des rôles, il est nécessaire de modifier les chemins (paths) et les redirections, afin d'éviter des erreurs d'accès.
***** 
* Désormais, si un utilisateur standard tente de modifier ou de supprimer, une erreur 403 s'affichera, ce qui signifie "non autorisé".

<img src="captures/erro403.png">

#### - Pour éviter cette confusion, il est préférable d'utiliser la contextualisation afin de masquer les fonctionnalités non nécessaires pour un utilisateur.
*  Pour chaque fonctionnalité que l'on souhaite masquer, on ajoute une expression nommée authorization.expression.
<img src="captures/contex.png">
******* 

#### En tant qu'utilisateur" :
<img src="captures/userrole.png"> 

* On remarque que les fonctionnalités edit et supprimer sont masqué pour un utilisateur ordinaire.

#### En tant qu'admin : 
<img src="captures/adminrole.png">

* On remarque que les fonctionnalités edit et supprimer ne sont masqué pas pour un utilisateur avec un role Admin.

#### Personnaliser le formulaire LOG IN :
* Si l'on souhaite personnaliser le formulaire de connexion, il faut utiliser la méthode loginPage dans formLogin, dans la classe SecurityConfig, comme suit :

    <img src="captures/formlogin.png">
  

* Il faut également créer une méthode dans la classe SecurityController pour gérer la redirection vers la page de connexion personnalisée :

    <img src="captures/logget.png">

  * Comme par défaut, tout accès aux ressources nécessite une authentification, il faut ajouter une autorisation pour permettre au formulaire d'accéder aux ressources Bootstrap.

        auth.requestMatchers("/webjars/**").permitAll(); 

#### - Formulaire Login Personnalisé : 

<img src="captures/loginperso.png">

### JDBC Authentication : 
* C’est une stratégie présente dans Spring Security depuis longtemps, qui permet de stocker les utilisateurs dans une base de données relationnelle via un accès JDBC.
* Il s’agit d’un modèle simplifié qui consiste à connecter Spring Security à une base de données relationnelle, dans laquelle Spring Security pourra retrouver les utilisateurs ainsi que leurs rôles.

#### COMMENT UTILISER JDBC Authentication : 

* Premièrement, on crée une méthode qui retourne un objet JdbcUserDetailsManager, dans laquelle on établit la liaison avec la base de données via l’injection de l’objet DataSource.
---
<img src="captures/jdbc.png">

---
* Ensuite, il faut créer deux tables : une table pour les utilisateurs et une autre pour les rôles. Ces deux tables doivent avoir une structure reconnue par Spring Security.
Pour cela, il est recommandé de récupérer la structure officielle depuis la dépendance spring-security-core (org.springframework.security:spring-security-core) en recherchant le fichier users.ddl.

#### Pour la création des tables, deux méthodes sont possibles :
* Créer manuellement les tables via l’interface phpMyAdmin.

* Créer un fichier SQL dans le dossier resources, nommé schema.sql, contenant la structure des tables.

----
<img src="captures/sql.png">

----

* Enfin, il faut ajouter les deux options suivantes dans le fichier application.properties :
        
        < spring.datasource.schema=classpath:schema.sql >
        < spring.sql.init.mode=always >

### Créations des utilisateurs : 

Pour initialiser des utilisateurs et rôles dès le démarrage de l’application, on peut créer une méthode "CommandLineRunner" dans la classe `PatientAppApplication`.

### 💡 Objectif

Grâce à l’injection de l’objet `JdbcUserDetailsManager`, on peut utiliser directement ses méthodes pour :
- Créer des utilisateurs
- Ajouter des rôles
- Chercher ou supprimer des utilisateurs

👉 **Il n'est donc pas nécessaire de créer des `Repository` manuellement** pour gérer les utilisateurs et leurs rôles. L’interface `JdbcUserDetailsManager` fournit déjà tout ce qu’il faut.

---
<img src="captures/commandrunn.png">

---

### Visualisation des utilisateurs dans la base de données :
#### TABLE USERS :

<img src="captures/user.png">

----

#### TABLE AUTHORITY : 

<img src="captures/role.png">

------- 

#### EXEMPLE D'AUTHENTIFICATION :


<img src="captures/user12.png">

-----





