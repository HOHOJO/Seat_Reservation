<?php
   error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('dbcon.php');

   //  $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");	

      $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

     $sql ="select * from labtoproom where state=0;"; 
     $stmt = $con->prepare($sql);
     $stmt->execute();

$rowCut = $stmt->rowCount();

$arr = array();
$arr[0] = $rowCut;

$jsonData = json_encode($arr);
echo "$jsonData";

?>