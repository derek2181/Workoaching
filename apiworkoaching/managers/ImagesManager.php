<?php 

require_once "utils/ApiManager.php";
require_once 'DAO/UsersDAO.php';


class ImagesManager extends ApiManager {

    public function GetMethod()
    {
        if(isset($_GET['email']) ){
            $images = UsersDAO::GetUserImagesByEmail($_GET['email']);
            echo json_encode($images);
            exit();

        }
    }

    public function PostMethod()
    {
        $_POST = json_decode(file_get_contents('php://input'), true);
        if(isset($_POST['context'])){
            switch($_POST['context']){
                case 'addImage':
                    $image = base64_decode($_POST['image']);
                    $this->AddImage($_POST['userId'], $image);
                    break;
                case 'deleteImage':
                    if(isset($_POST['id_image'])){
                        $this->DeleteImage($_POST['id_image']);
                    } else {
                        echo json_encode(false);
                    }
                    break;
            }

        }

        
    }

    public function PutMethod()
    {
        
    }

    public function DeleteMethod()
    {
        
    }

    public function AddImage($userId, $image){
        $result = UsersDAO::AddImage($userId, $image);

        echo json_encode($result);

        exit();
    }

    public function DeleteImage($imageId){
        $result = UsersDAO::DeleteImage($imageId);

        echo json_encode($result);

        exit();
    }
}