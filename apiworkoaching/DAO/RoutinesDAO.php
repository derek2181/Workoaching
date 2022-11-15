<?php 
require_once "utils/DbConnection.php";
require_once "DAO/ExercisesDAO.php";
class RoutinesDAO {

    public static function GetRoutineById($id){
        $query = "select rutinas.*, 
        concat(usuarios.Nombre,' ', usuarios.Apellido_P) 'Autor',
        usuarios.Email,
        usuarios.Imagen
        from rutinas
        inner join usuarios on usuarios.id_usuario = rutinas.id_usuario where rutinas.id_rutina = :id";
        

        $rows = null;
        try {
            $statement = DbConnection::Connect()->prepare($query);

            $result = $statement->execute(array(
                ":id" => $id
            ));
             
            if($result){
                $rows = $statement->fetch(PDO::FETCH_ASSOC);
                if($rows != false){
                    $rows["Imagen"] = base64_encode($rows["Imagen"]);
                    $rows["Ejercicios"] = ExercisesDAO::GetRoutineExercises($id);
                } else {
                    $rows = null;
                }
            }

        } catch(Exception $e){

        }

        return $rows;


    }

    public static function GetAllRoutines(){

        $query = "select rutinas.*, 
        concat(usuarios.Nombre,' ', usuarios.Apellido_P) 'Autor', 
        GetCategories(rutinas.Id_Rutina) as 'Categorias' 
        from rutinas 
        inner join usuarios on usuarios.id_usuario = rutinas.id_usuario";
        

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

    public static function GetRecentRoutines(){

        $query = "select rutinas.*, 
        concat(usuarios.Nombre,' ', usuarios.Apellido_P) 'Autor', 
        GetCategories(rutinas.Id_Rutina) as 'Categorias',
        usuarios.Email 
        from rutinas 
        inner join usuarios on usuarios.id_usuario = rutinas.id_usuario
        where rutinas.activo = 1 
        order by rutinas.Id_Rutina desc limit 0, 8";
        

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

    public static function GetPopularRoutines(){
        $query = "select rutinas.*, 
        concat(usuarios.Nombre,' ', usuarios.Apellido_P) 'Autor', 
        GetCategories(rutinas.Id_Rutina) as 'Categorias', count(rutinas_usuarios.id_usuario) as Cantidad_Usuarios,
        usuarios.Email 
        from rutinas 
        inner join usuarios on usuarios.id_usuario = rutinas.id_usuario 
        inner join rutinas_usuarios on rutinas_usuarios.id_rutina = rutinas.id_rutina
        where rutinas.activo = 1 
        group by rutinas_usuarios.id_rutina 
        order by Cantidad_Usuarios desc limit 0, 8";

        $rows = null;

        try {
            $statement = DbConnection::Connect()->prepare($query);

            $result = $statement->execute();

            if($result){
                $rows = $statement->fetchAll(PDO::FETCH_ASSOC);
            }
            

        }catch(Exception $e){

        }


        return $rows;
    }

    public static function GetCategoryRoutines($categoryId){
        $query = "select rutinas.*, 
        concat(usuarios.Nombre,' ', usuarios.Apellido_P) 'Autor', 
        GetCategories(rutinas.Id_Rutina) as 'Categorias',
        usuarios.Email  
        from rutinas 
        inner join usuarios on usuarios.id_usuario = rutinas.id_usuario 
        inner join rutinas_ejercicios on rutinas_ejercicios.Id_Rutina = Rutinas.Id_Rutina 
		inner join ejercicios on ejercicios.Id_Ejercicio = rutinas_ejercicios.Id_Ejercicio
		inner join categorias on categorias.Id_Categoria = ejercicios.Id_Categoria
        where categorias.id_categoria = :id_categoria and rutinas.activo =  1 group by rutinas.Id_Rutina";

        $rows = null;

        try {
            $statement = DbConnection::Connect()->prepare($query);

            $result = $statement->execute(array(
                ":id_categoria" => $categoryId
            ));

            if($result){
                $rows = $statement->fetchAll(PDO::FETCH_ASSOC);
            }
            

        }catch(Exception $e){

        }


        return $rows;
    }


    public static function GetUserRoutinesByEmail($userEmail){
        $query = "  select rutinas.*, 
        concat(usuarios.Nombre,' ', usuarios.Apellido_P) 'Autor', 
        GetCategories(rutinas.Id_Rutina) as 'Categorias',
        usuarios.Email 
        from rutinas 
        inner join usuarios on usuarios.id_usuario = rutinas.id_usuario where usuarios.email = :email and rutinas.activo = 1
         order by rutinas.Id_Rutina desc ;";

        $rows = null;

        try {
            $statement = DbConnection::Connect()->prepare($query);

            $result = $statement->execute(array(
                ":email" => $userEmail
            ));

            if($result){
                $rows = $statement->fetchAll(PDO::FETCH_ASSOC);
            }
            

        }catch(Exception $e){

        }


        return $rows;

    }

    public static function GetUserFavoriteRoutinesByEmail($userEmail){
        $query = "  select rutinas.*,
        concat(userowner.Nombre,' ', userowner.Apellido_P) 'Autor', 
       GetCategories(rutinas.Id_Rutina) as 'Categorias',
       userowner.Email 
       from rutinas_usuarios
       inner join usuarios as userwhoadd on userwhoadd.id_usuario = rutinas_usuarios.id_usuario
       inner join rutinas on rutinas.id_rutina = rutinas_usuarios.id_rutina
       inner join usuarios as userowner on userowner.id_usuario = rutinas.id_usuario
       where userwhoadd.email = :email and rutinas.activo = 1 order by rutinas.Id_Rutina desc ;";

        $rows = null;

        try {
            $statement = DbConnection::Connect()->prepare($query);

            $result = $statement->execute(array(
                ":email" => $userEmail
            ));

            if($result){
                $rows = $statement->fetchAll(PDO::FETCH_ASSOC);
            }
            

        }catch(Exception $e){

        }


        return $rows;
        
    }

    public static function InsertRoutine($name, $userId, $exercises){

        $query = "insert into rutinas(nombre, id_usuario)values(:name, :userId)";
        

        $result = false;
        try {
            $con = DbConnection::Connect();
            $statement = $con->prepare($query);

            $result = $statement->execute(array(
                ':name' => $name,
                ':userId' => $userId
            ));
            
            if($result){
                // $statement = DbConnection::Connect()->prepare("select last_insert_id()");
    
                // $statement->execute();   
    
                $routineId = $con->lastInsertId();
                
                
                foreach($exercises as $exercise){
                    
                    self::InsertRoutineExercises($exercise["Id_ejercicio"], $routineId, $exercise["Series"], $exercise["Repeticiones"]);

                }
            }


            
            

        } catch(Exception $e){
            
        }

        return $result;

    }

    public static function UpdateRoutine($routineId, $name, $exercises){
        $query = "update rutinas set nombre = :name where id_rutina = :routineId";
        
        $result = false;
        try {
            $con = DbConnection::Connect();
            $statement = $con->prepare($query);

            $result = $statement->execute(array(
                ':name' => $name,
                ':routineId' => $routineId
            ));
            
            

            if($result){
                
                $query = "delete from rutinas_ejercicios where id_rutina = :routineId";
                $statement = $con->prepare($query);

                $result = $statement->execute(array(
                    ':routineId' => $routineId
                ));
                
                
                if($result){
                    foreach($exercises as $exercise){
                        
                        self::InsertRoutineExercises($exercise["Id_ejercicio"], $routineId, $exercise["Series"], $exercise["Repeticiones"]);
    
                    }
                }
            
            }


            
            

        } catch(Exception $e){
            
        }

        return $result;
    }


    public static function InsertRoutineExercises($exerciseId, $routineId, $series,$reps){

        $query = "insert into rutinas_ejercicios(id_rutina, id_ejercicio, repeticiones, series)
        values(:routineId, :exerciseId, :reps, :series)";

        $result = false;
        try {
            $statement = DbConnection::Connect()->prepare($query);

            $result = $statement->execute(array(
                ':routineId' => $routineId,
                ':exerciseId' => $exerciseId,
                ':reps' => $reps,
                ':series' => $series
            ));
             
            

        } catch(Exception $e){
            
        }

        return $result;


    }

    public static function DeleteRoutineById($routineId){
        $query = "
            update rutinas set rutinas.activo = 0 where rutinas.id_rutina = :id_rutina
        ";

        $result = false;
        try {
            $statement = DbConnection::Connect()->prepare($query);

            $result = $statement->execute(array(
                ':id_rutina' => $routineId
            ));
             
            

        } catch(Exception $e){
            
        }

        return $result;



    }

    public static function IsRoutineAddedByUser($email, $routineId){

        $query = "select 1 from rutinas_usuarios inner join usuarios on usuarios.id_usuario = rutinas_usuarios.id_usuario 
        where usuarios.email = :email and rutinas_usuarios.id_rutina = :id_rutina;";

        $userExists = false;

        try {

            $statement = DbConnection::Connect()->prepare($query);

            $success = $statement->execute(array(
                    ":email" => $email,
                    ":id_rutina" => $routineId
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

    public static function AddFavoriteRoutine($routineId, $userId){
        $query = "
            insert into rutinas_usuarios(id_rutina, id_usuario)values(:id_rutina, :id_usuario);
        ";

        $result = false;
        try {
            $statement = DbConnection::Connect()->prepare($query);

            $result = $statement->execute(array(
                ':id_rutina' => $routineId,
                ':id_usuario' => $userId
            ));
             
            

        } catch(Exception $e){
            
        }

        return $result;
        
    }

    public static function RemoveFavoriteRoutine($routineId, $userId){
        $query = "
            delete from rutinas_usuarios where id_rutina = :id_rutina and id_usuario = :id_usuario;
        ";

        $result = false;
        try {
            $statement = DbConnection::Connect()->prepare($query);

            $result = $statement->execute(array(
                ':id_rutina' => $routineId,
                ':id_usuario' => $userId
            ));
             
            

        } catch(Exception $e){
            
        }

        return $result;
        
    }


}