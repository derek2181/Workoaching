<?php 

require_once "managers/UserManager.php";

$usersManager = new UserManager();

$usersManager->HandleRequest();