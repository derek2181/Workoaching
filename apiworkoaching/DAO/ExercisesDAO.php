<?php 

require_once "utils/DbConnection.php";



class ExercisesDAO {
    
    public static function GetRoutineExercises($routineId){
        $query = "
            select rutinas_ejercicios.Id_ejercicio, Repeticiones, Series, Nombre, ejercicios.Imagen from rutinas_ejercicios
             inner join ejercicios on ejercicios.id_ejercicio = rutinas_ejercicios.id_ejercicio where id_rutina = :id_rutina; 
        ";

        $rows = null;
        try {

            $statement = DbConnection::Connect()->prepare($query);

            $result = $statement->execute(array(
                ":id_rutina" => $routineId
            ));

            if($result){
                $rows = $statement->fetchAll(PDO::FETCH_ASSOC);
                foreach($rows as &$row){
                    $row['Imagen'] = base64_encode($row['Imagen']);
                }
            }



        } catch(Exception $e){

        }


        return $rows;
    }
    public static function getAllExercises(){
        $query = "
        select Id_ejercicio, Nombre, Imagen,Id_Categoria  from ejercicios;";
    $rows = null;
    try {

        $statement = DbConnection::Connect()->prepare($query);

        $result = $statement->execute();

        if($result){
            $rows = $statement->fetchAll(PDO::FETCH_ASSOC);
            foreach($rows as &$row){
                $row['Imagen'] = base64_encode($row['Imagen']);
            }
        }
    } catch(Exception $e){

    }


    return $rows;

    }


    public static function getExercisesByCategoryId($exerciseId){
        $query = "
        select Id_ejercicio, Nombre, Imagen from ejercicios
        where id_categoria = :id_category;";
    $rows = null;
    try {

        $statement = DbConnection::Connect()->prepare($query);

        $result = $statement->execute(array(
            ":id_category" => $exerciseId
        ));

        if($result){
            $rows = $statement->fetchAll(PDO::FETCH_ASSOC);
            foreach($rows as &$row){
                $row['Imagen'] = base64_encode($row['Imagen']);
            }
        }



    } catch(Exception $e){

    }


    return $rows;
    }
}