<?php 

require_once "utils/ApiManager.php";
require_once "DAO/RoutinesDAO.php";

class RoutinesManager extends ApiManager {
    
    public function GetMethod(){
        if(isset($_GET['id'])){
            if(isset($_GET['email'])){
                $result = RoutinesDAO::IsRoutineAddedByUser($_GET['email'], $_GET['id']);

                echo json_encode($result);

                exit();
                
            }
            // Get just one register by id 
            $this->RoutineById($_GET['id']);

        } else {

            if(isset($_GET['context'])){
                $context = $_GET['context'];

                switch($context){

                    case 'recent':
                       $this->RecentRoutines();
                    break;
                    case 'popular':
                        $this->PopularRoutines();
                    break;
                    case 'own':
                        $this->GetUserRoutine();
                        break;
                    case 'favorite':
                        $this->GetUserFavoriteRoutines();
                    default:
                    return;
                }

            } else if(isset($_GET['categoryId'])){
                $this->RoutinesByCategoryId($_GET['categoryId']);
            }

            // Get All routines without filtering
            $allRoutines = RoutinesDAO::GetAllRoutines();

            echo json_encode($allRoutines);
            exit();
        }
    }

    public function PostMethod() {
        $_POST = json_decode(file_get_contents('php://input'), true);
        if(isset($_POST['context'])){
            switch($_POST['context']){
                case 'delete':
                    if(isset($_POST['id'])){
                        $this->DeleteRoutine($_POST['id']);
                    }
                    break;
                case 'addFavoriteRoutine':
                    if(isset($_POST['id']) && isset($_POST['userId'])){
                        $result = RoutinesDAO::AddFavoriteRoutine($_POST['id'], $_POST['userId']);

                        echo json_encode($result);
                        exit();
                    }
                    break;
                case 'removeFavoriteRoutine':
                    if(isset($_POST['id']) && isset($_POST['userId'])){
                        $result = RoutinesDAO::RemoveFavoriteRoutine($_POST['id'], $_POST['userId']);

                        echo json_encode($result);
                        exit();
                    }
                    break;
                case 'editRoutine':
                    if(isset($_POST['Rutina']['Nombre']) && isset($_POST['Rutina']['Id_Rutina']) && isset($_POST['Rutina']['Ejercicios'])){
                        $result = RoutinesDAO::UpdateRoutine($_POST['Rutina']['Id_Rutina'], $_POST['Rutina']['Nombre'],$_POST['Rutina']["Ejercicios"]);
                        
                        echo json_encode($result);
                        exit();
                    }
                    break;

            }

        } else {

            if(isset($_POST['Nombre']) && isset($_POST['Id_Usuario']) && isset($_POST["Ejercicios"])){
                $result = RoutinesDAO::InsertRoutine($_POST['Nombre'], $_POST['Id_Usuario'],$_POST["Ejercicios"]);
                
                echo json_encode($result);
                exit();
            }

        }
    }

    public function PutMethod() {
  
        
    }

    public function DeleteMethod(){
        
        
    }
    

    private function RecentRoutines(){
        $recentRoutines = RoutinesDAO::GetRecentRoutines();
        echo json_encode($recentRoutines);
        exit();
    }

    private function RoutineById($id){

        $routine = RoutinesDAO::GetRoutineById($id);
        if($routine != null){
            header("HTTP/1.1 200 OK");
            echo json_encode($routine);
        }
        exit();
    }


    private function PopularRoutines(){

        $popularRoutines = RoutinesDAO::GetPopularRoutines();
        echo json_encode($popularRoutines);
        exit();
    }
    
    private function RoutinesByCategoryId($categoryId){

        $categoryRoutines = RoutinesDAO::GetCategoryRoutines($categoryId);
        echo json_encode($categoryRoutines);
        exit();
    }

    private function GetUserRoutine(){
        if(isset($_GET['userEmail'])){
            $userRoutines = RoutinesDAO::GetUserRoutinesByEmail($_GET['userEmail']);
    
            echo json_encode($userRoutines);
    
            exit();
        }

    }

    private function GetUserFavoriteRoutines(){

        if(isset($_GET['userEmail'])){
            $userRoutines = RoutinesDAO::GetUserFavoriteRoutinesByEmail($_GET['userEmail']);

            echo json_encode($userRoutines);

            exit();
            
        }


    }

    private function DeleteRoutine($routineId){
            $result = RoutinesDAO::DeleteRoutineById($routineId);

            echo json_encode($result);

            exit();
    }

 
}