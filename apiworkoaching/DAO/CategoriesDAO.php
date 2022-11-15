<?php 
require_once "utils/DbConnection.php";

class CategoriesDAO {

    public static function GetAllCategories(){
        $query = "select Id_Categoria, Nombre from categorias";

        $rows = null;
        
        try {
            $statement = DbConnection::Connect()->prepare($query);

            $result = $statement->execute();

            if($result){

                $rows = $statement->fetchAll(PDO::FETCH_ASSOC);
            }


        } catch(Exception $e){

        }

        return $rows;

    }


}