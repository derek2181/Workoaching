<?php 
    require_once "managers/CategoriesManager.php";
    // echo "Soy el archivo de php".$_GET["firstVariable"];

    $categoriesManager = new CategoriesManager();

    $categoriesManager->HandleRequest();

