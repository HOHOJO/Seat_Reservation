<?php
   error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('dbcon.php');

   //  $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");	

    $ID= isset($_GET['ID']) ? $_GET['ID'] : '';    
    $PW= isset($_GET['PW']) ? $_GET['PW'] : ''; 
    $RESERVATION= isset($_GET['RESERVATION']) ? $_GET['RESERVATION'] : ''; 
    $SEAT= isset($_GET['SEAT']) ? $_GET['SEAT'] : ''; 
      $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

if ($ID != "") {
    $sql ="select * from student where stuent_number='$ID' and s_password='$PW' and state=0;"; 
    $stmt = $con->prepare($sql);
    $stmt->execute();
    $sql1 ="update student set state='1', seat='$SEAT' where stuent_number='$ID' and s_password='$PW';"; 
    $stmt1 = $con->prepare($sql1);
    $stmt1->execute();
    $sql2 = "update labtoproom set state='1', seat_student='$ID', reservation='$RESERVATION' where seat_number='$SEAT=';";
    $stmt2 = $con->prepare($sql2);
    $stmt2->execute();

    if ($stmt->rowCount() == 0){
        echo "'";
        echo "'이미 예약중입니다..";
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
         시간: <input type = "text" name = "RESERVATION" />
         자리: <input type = "text" name = "SEAT" />
         <input type = "submit" />
      </form>
   
   </body>
</html>