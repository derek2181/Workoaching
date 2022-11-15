<?php 
    require_once "managers/RoutinesManager.php";
    // echo "Soy el archivo de php".$_GET["firstVariable"];

    $routinesManager = new RoutinesManager();

    $routinesManager->HandleRequest();




