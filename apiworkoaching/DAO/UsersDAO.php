<?php 

require_once 'utils/DbConnection.php';
require_once 'DAO/UsersDAO.php';
require_once 'models/User.php';

class UsersDAO {

    public static function InsertUser(UserModel $user){

        $query = "insert into usuarios(email, nombre, apellido_p, apellido_m, contrasena, telefono, direccion, imagen)
        values(:email, :name, :lastname, :secondlastname, :password, :phone, :address, :image);";

        $result = false;


        try {
            $statement = DbConnection::Connect()->prepare($query);

            $result = $statement->execute(array(
                ":email" => $user->getEmail(),
                ":name" => $user->getName(),
                ":lastname" => $user->getLastname(),
                ":secondlastname" => $user->getSecondlastname(),
                ":phone" => $user->getPhone(),
                ":address" => $user->getAddress(),
                ":image" => $user->getImage(),
                ":password" => $user->getPassword()
            ));



        } catch(Exception $e){
            
        }

        return $result;
    }

    public static function Login($email, $password){

        $query = "select 1 from usuarios where email = :email and contrasena = :password";

        $userExists = false;

        try {

            $statement = DbConnection::Connect()->prepare($query);

            $success = $statement->execute(array(
                    ":email" => $email,
                    ":password" => $password
            ));

            if($success){

                $result = $statement->fetchColumn(0);
                if($result != null){
                    $userExists = true;
                }
                
            }
        } catch(Exception $e){

        }

        return $userExists;
    }

    public static function GetUserByEmail($email) {

        $query = "
            select id_usuario, email, imagen, direccion, telefono, nombre, apellido_p, apellido_m 
            from usuarios where email = :email
        ";

        $row = null;
        try {
            $statement = DbConnection::Connect()->prepare($query);


            $result = $statement->execute(array(
                ":email" => $email

            ));

            if($result){
                $row = $statement->fetch(PDO::FETCH_ASSOC);
                if($row == false)
                    $row = null;
                else{
                    $row["imagen"] = base64_encode($row["imagen"]);
                }
            }

        } catch(Exception $e){


        }



        return $row;
    }

    public static function ChangeUserPicture($userEmail, $image){
        $query = "update usuarios set usuarios.imagen = :imagen where usuarios.email = :email";

        $result = false;


        try {
            $statement = DbConnection::Connect()->prepare($query);

            $result = $statement->execute(array(
                ":email" => $userEmail,
                ":imagen" => $image
            ));



        } catch(Exception $e){
            
        }

        return $result;

    }

    public static function AddImage($userId, $image){
        $query = "insert into imagenes(id_usuario, imagen)values(:id_usuario, :imagen);";

        $result = null;


        try {
            $con = DbConnection::Connect();
            $statement = $con->prepare($query);

            $result = $statement->execute(array(
                ":id_usuario" => $userId,
                ":imagen" => $image
            ));

            if($result){
                $result = $con->lastInsertId();
            } else {
                $result = 0;
            }

        } catch(Exception $e){
            $result = 0;
        }

        return $result;

    }


    public static function DeleteImage($imageId){
        $query = "delete from imagenes where id_imagen = :id_image";

        $result = false;
        try {
            $con = DbConnection::Connect();
            $statement = $con->prepare($query);

            $result = $statement->execute(array(
                ":id_image" => $imageId
            ));


        } catch(Exception $e){
                
        }

        return $result;
    }

    public static function ChangePassword($userEmail, $newPassword){
        $query = "update usuarios set usuarios.contrasena = :contrasena where usuarios.email = :email";

        $result = false;


        try {
            $statement = DbConnection::Connect()->prepare($query);

            $result = $statement->execute(array(
                ":email" => $userEmail,
                ":contrasena" => $newPassword
            ));


        } catch(Exception $e){
            
        }

        return $result;
    }

    public static function GetUserImagesByEmail($userEmail){
        $query = "
            select imagenes.id_imagen, imagenes.imagen, imagenes.id_usuario 
            from imagenes
            inner join usuarios on usuarios.id_usuario = imagenes.id_usuario
            where usuarios.email = :email
        ";

        $rows = null;
        try {
            $statement = DbConnection::Connect()->prepare($query);


            $result = $statement->execute(array(
                ":email" => $userEmail

            ));

            if($result){
                $rows = $statement->fetchAll(PDO::FETCH_ASSOC);
                if($rows == false)
                    $rows = null;
                else{
                    foreach($rows as &$row){
                        $row["imagen"] = base64_encode($row["imagen"]);
                    }
                }
            }

        } catch(Exception $e){


        }



        return $rows;

    }




}