<?php
    date_default_timezone_set('Asia/Seoul');
   error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('dbcon.php');

   //  $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");	

    $ID= isset($_GET['ID']) ? $_GET['ID'] : '';    
    $PW= isset($_GET['PW']) ? $_GET['PW'] : '';
      $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");


if ($ID != "") {
    $sql ="select * from student where stuent_number='$ID' and s_password='$PW' and state=1;"; 
    $stmt = $con->prepare($sql);
    $stmt->execute();
    $row = $stmt->fetchAll();
    $SEAT = $row[0]['seat'];

    if ($stmt->rowCount() == 0){
        echo "'";
        echo "'예약없음";
    }else{
   	$data = array(); 
       $sql1 ="select * from labtoproom where seat_student='$ID' and seat_number='$SEAT' and state=1;"; 
       $stmt1 = $con->prepare($sql1);
       $stmt1->execute();
       $row1=$stmt1->fetchAll();
        $RE = $row1[0]['reservation'];
        $T;
        switch($RE){
            case "00:00:10":
                $T = strtotime(date("Y-m-d H:i:s")."+30 Second");
                break;
            case "00:30:00":
                $T = strtotime(date("Y-m-d H:i:s")."+30 Minute");
                break;
            case "01:00:00":
                $T = strtotime(date("Y-m-d H:i:s")."+1 Hour");
                break;
            case "01:30:00":
                $T = strtotime(date("Y-m-d H:i:s")."+12 Second");
                break;
            case "02:00:00":
                $T = strtotime(date("Y-m-d H:i:s")."+12 Second");
                break;
        }
        $TIME = date("Y-m-d H:i:s",$T);
        print_r($TIME);

        $sql2 = "CREATE EVENT room1_start
        ON SCHEDULE
        AT '$TIME'
        COMMENT '시간시작'
        DO
        update labtoproom set state=0,reservation=0,seat_student=0 where seat_number=$SEAT;";
            $stmt2 = $con->prepare($sql2);
            $stmt2->execute();

            $sql3 = "CREATE EVENT room2_start
            ON SCHEDULE
            AT '$TIME'
            COMMENT '시간시작'
            DO
            update student set state=0 ,seat=0 where stuent_number='$ID' and s_password='$PW';";
                $stmt3 = $con->prepare($sql3);
                $stmt3->execute();

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