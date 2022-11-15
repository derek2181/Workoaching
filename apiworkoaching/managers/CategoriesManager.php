<?php 

require_once "DAO/CategoriesDAO.php";
require_once "utils/ApiManager.php";

class CategoriesManager extends ApiManager {

    public function GetMethod()
    {
        $categories = CategoriesDAO::GetAllCategories();

        echo json_encode($categories);
        
        exit();
    }

    public function PostMethod()
    {
        
    }

    public function PutMethod()
    {
        
    }

    public function DeleteMethod()
    {
        
    }

}