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


Produktbeskrivning<br>
Produkten är ett strategispel utvecklat i Java. Spelet kallas för Stratego, vilket är ett brädspel för två spelare. Stratego spelas på ett 10x10 stort rutnät. Varje spelare har 40 pjäser, varav en är en fana. Målet med spelet är att erövra motståndarens fana eller slå ut alla rörliga pjäser. 
Fokus för spelet ska ligga på PvP, antingen genom spel på en lokal dator (Hot-seat) eller över nätverk med två uppkopplade spelare. I mån av tid kommer animeringar, ljudfunktioner samt PvE-stöd att läggas till. Med PvE avses att en spelare kan möta en AI spelare. PvE-stödet är något gruppen gärna försöker sig på men möjligheterna att implementera ser små ut. Att överföra spelet till android är något som skulle vara bra men som inte kommer att utföras.
 
Målgrupp<br>
Då spelet har en rekommenderad ålder från 8 år, så är målgruppen människor intresserade av strategispel från 8 år och uppåt. Målgruppen specificeras ytterligare med: 
Spelare som redan är bekanta med Stratego och/eller gillar sällskapsspel i övrigt, tidspressade individer som kan tänkas nyttja slumpade startpositioner och som kan tänkas vilja spara/ladda pågående spel eller geografiskt isolerade personer som kan tänkas vilja nyttja nätverksanslutning. 
