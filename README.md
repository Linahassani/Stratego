# Stratego
Java game based on a board game Stratego

Made by:<br>
Anders Qvist<br>
André Hansson<br>
Henrik Sandström<br>
Lukasz Kurasinski<br>


To start the application run the Controller.java class.

To start the server run the StartUp.java class in the server package.

The ip address to the server can be changed in Controller.java.

The applications uses a database to keep track of highscore and the different players in multiplayer. To be able to access this, you need 
to download the postgresSQL from their website: https://www.postgresql.org/download/ and follow their instructions. After all the installations are done you need to go into your eclipse (or any other IDE thats running this program). Right-click on your "stratego" folder and click on properties. Then click on Java Build Path -> Libaries -> Add External Jars... Then find the postgresSQL.jar file (that you got from the postgresSQL download and click on it. Click on open and then apply and close. Now your eclipse or any other IDE should work with postgresSQL. 


Product Description <br>
The product is a strategy game developed in Java. The game is called Stratego, which is a board game for two players. Stratego is played on a 10x10 large grid. Each player has 40 pieces, one of which is a flag. The aim of the game is to conquer the opponent's flag or knock out all moving pieces.
The focus of the game should be on PvP, either through games on a local computer (Hot-seat) or over networks with two connected players. As time goes by, animations, audio functions and PvE support will be added. PvE means that a player can face an AI player. The PvE support is something the group would like to try, but the possibilities to implement look small. Transferring the game to android is something that would be good but will not be performed.
 
Target <br>
Since the game has a recommended age from 8 years, the target group is people interested in strategy games from 8 years and up. The target group is further specified by:
Players who are already familiar with Stratego and / or like board games, players who may like a challenge with random starting positions and who may wish to save / load ongoing games or people who want to play board games online.

