<?php 

require_once "utils/ApiManager.php";
require_once "models/User.php";
require_once 'DAO/UsersDAO.php';

class UserManager extends ApiManager {

    public function GetMethod()
    {
        if(isset($_GET['email'])){
            
            $this->GetUserByEmail($_GET['email']);
        }
    }

    public function PostMethod()
    {
        $_POST = json_decode(file_get_contents('php://input'), true);
        if(isset($_POST['context'])){
            switch($_POST['context']){
                case 'login':
                    if(isset($_POST['email']) && isset($_POST['password'])){
                        $result = UsersDAO::Login($_POST['email'], $_POST['password']);
    
                        echo json_encode($result);
                        exit();
                        
                    } else {
    
                        $result = false;
                        echo json_encode($result);
                        exit();
                        
                    }
                    break;
                case 'changePicture':
                    if(isset($_POST['email']) && isset($_POST['image'])){
                        $image = base64_decode($_POST['image']);
                        $result = UsersDAO::ChangeUserPicture($_POST['email'], $image);
    
                        echo json_encode($result);
    
                        exit();
                    }
                    break;
                case 'changePassword':
                    if(isset($_POST['email']) && isset($_POST['currentPassword'])&& isset($_POST['newPassword'])){
                        
                        $validCredentials = UsersDAO::Login($_POST['email'], $_POST['currentPassword']);

                        if($validCredentials){

                            $result = UsersDAO::ChangePassword($_POST['email'], $_POST['newPassword']);

                            echo json_encode($result);
                            exit();

                        } else {
                            $result = false;

                            echo json_encode($result);

                            exit();
                        }
                        
                    }
                    break;
                    default:
                $result = false;
                echo json_encode($result);
                exit();
                

            }
            

        } else {

            if(isset($_POST['email']) &&
                isset($_POST['nombre']) &&
                isset($_POST['apellido_p']) &&
                isset($_POST['contrasena']) ){
                    $user = new UserModel();
                    
                    $user->setEmail($_POST['email']);
                    $user->setName($_POST['nombre']);
                    $user->setLastname($_POST['apellido_p']);
                    $user->setPassword($_POST['contrasena']);
    
                    $user->setPhone($_POST['telefono'] ?? null);
                    $user->setAddress($_POST['direccion'] ?? null);
                    $user->setSecondlastname($_POST['apellido_m'] ?? null);
                    $user->setImage($_POST['imagen'] ?? null);
    
                    $result = UsersDAO::InsertUser($user);
    
    
                    echo json_encode($result);
                    exit();
            } else {
                echo json_encode(false);
                exit();
            }

        }
    }

    public function PutMethod()
    {
        
    }

    public function DeleteMethod()
    {
        
    }

    private function GetUserByEmail($email){

        $result = UsersDAO::GetUserByEmail($email);

        echo json_encode($result);

        exit();

    }

}