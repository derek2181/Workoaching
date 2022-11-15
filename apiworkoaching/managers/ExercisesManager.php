<?php 

require_once "utils/ApiManager.php";
require_once 'DAO/ExercisesDAO.php';

class ExercisesManager extends ApiManager {
    public function GetMethod()
    {
        if(isset($_GET['id']) ){
            $exercises = $this->getExerciseByCategoryId($_GET['id']);
            echo json_encode($exercises);
            exit();

        }else{
            $exercises=$this->getAllExercises();

            echo json_encode($exercises);
        }
    }
    public function PostMethod()
    {
        $_POST = json_decode(file_get_contents('php://input'), true);
        
    }

    public function PutMethod()
    {
        
    }
    
    public function DeleteMethod()
    {
        
    }

  
    private function getExerciseByCategoryId($categoryId){
        return ExercisesDAO::getExercisesByCategoryId($categoryId);
    }
    private function getAllExercises(){
        return ExercisesDAO::getAllExercises();
    }
}