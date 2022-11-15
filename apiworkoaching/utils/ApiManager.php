<?php 

abstract class ApiManager {

    public function HandleRequest(){

        switch($_SERVER["REQUEST_METHOD"]){
            case 'GET':
                $this->GetMethod();
                break;
            case 'POST':
                $this->PostMethod();
                break;
            case 'PUT':
                $this->PutMethod();
                break;
            case 'DELETE':
                $this->DeleteMethod();
                break;
        }

    }

    abstract public  function GetMethod();


    abstract public  function PostMethod();

    abstract public  function PutMethod();
    
    abstract public  function DeleteMethod();

    


}
