<?php


class DbConnection {
    public static function Connect(){

        $connection = new PDO("mysql:host=localhost;dbname=workoaching", "root", "");

        $connection->exec("set names utf8");

        return $connection;
    }

}
