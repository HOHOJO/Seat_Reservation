<?php
   error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('dbcon.php');

   //  $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");	

    $ID= isset($_GET['ID']) ? $_GET['ID'] : '';    
    $PW= isset($_GET['PW']) ? $_GET['PW'] : ''; 
      $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

if ($ID != "" ){ 
     $sql ="select * from student where stuent_number='$ID' and s_password='$PW';"; 
     $stmt = $con->prepare($sql);
     $stmt->execute();
 
    if ($stmt->rowCount() == 0){
        echo "'";
        echo $ID,", ",$PW;
        echo "'은 찾을 수 없습니다.";
    }else{
   	$data = array(); 
        while($row=$stmt->fetch(PDO::FETCH_ASSOC)){
        	extract($row);
         	echo '1';
        }
        if (!$android) {
            echo "<pre>"; 
            print_r($data); 
            echo '</pre>';
        }else {
            header('Content-Type: application/json; charset=utf8');
            $json = json_encode(array("webnautes"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
            echo $json;
        }
    }
}
else {
    echo ". ";
}
?>


<html>
   <body>
   
      <form action="<?php $_PHP_SELF ?>" method="GET">
         아이디: <input type = "text" name = "ID" />
         비밀번호: <input type = "text" name = "PW" />
         <input type = "submit" />
      </form>
   
   </body>
</html>