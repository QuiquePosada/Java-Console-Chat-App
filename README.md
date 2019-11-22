# Java-Console-Chat-App
Console chat room system made in java using sockets and multithreading for Programming Languages class

This program is a multithreaded program that imlpements sockets.

<b>How to Setup :</b>
In order to run the program, you must have java version 13 installed. In order to successfully run both the server and client programs with the different computers, you must do the following : 
<ol>
  <li>Download both <em>"Server.java"</em> and <em>"Client.java"</em></li>
  <li>Open and edit the file <em>"Client.java"</em> and look for the ip variable, in which if you want to communicate with other computers within a local network, then you must enter the ip address of the computer that will run the <em>"Server.java"</em> file, and save it
  <li>Then, in the <b>server</b> computer, run the command "javac Server.java" or if you already have the .class file then run the command "java Server"
    <ul>
      <li>Then at the server computer leave that window alone and the program will run it's course</li>
    </ul></li></li>
  <li>For the clients, do the same step provided that was done in <b>step 3</b> in a different computer (or the same computer if that's your desire)
    <ul>
      <li>All instructions for the client program are provided when running the program</li>
      <li>After running the command to run the client program, enter your username and then, if the server is connected and you provided the correct ip address for the server computer, you will receive a welcome message,and from there you will be able of sending all messages as well as read messages</li>
      <li>In order to exit the client program, your <b>MUST</b> enter the line <b><em>exit()</em></b> which will disconnect you from the server and provide the command to stop the program. <strong><em>**NOTE</em>: not ending the program adequately will crash the server program, to which that program must be stopped running "ctrl + C" in the server terminal</strong></li>
    </ul></li></li>
</ol>

