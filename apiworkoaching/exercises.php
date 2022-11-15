<?php 
    require_once "managers/ExercisesManager.php";
    // echo "Soy el archivo de php".$_GET["firstVariable"];

    $exercisesManager = new ExercisesManager();

    $exercisesManager->HandleRequest();

