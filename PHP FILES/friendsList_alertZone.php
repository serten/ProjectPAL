<?php
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST');
$u_id=$_GET["userID"];
//$polyType=$_GET["Polytype"];
//$zoneName=$_GET["zoneName"];

$conn = pg_connect("host=postgredb.ctnfr2pmdvmf.us-west-2.rds.amazonaws.com port=5432 dbname=postgreDB user=postgreuser password=6089qwerty");
if (!$conn) {
  echo "An error occurred.\n";
  exit;
}
ini_set('date.timezone', 'America/Los_Angeles');
$time = date('H:i:s', time()); 
$date = date('Y-m-d')." ".$time;
// this section creates a table for alertzone information
if (!isset($_GET["friendName"])) {
	$result = pg_query($conn, "SELECT P.USERNAME, P.LASTUPDATETIME FROM FRIENDLIST F,PALUSER P WHERE F.USERID='$u_id' AND P.USERID=F.FRIENDID");	
	$num = pg_num_rows($result);
	
	if (!(0<$num)) {
	  echo "An error occurred.\n";
	  exit;
	}

	$message = '{"numberOfFriends":"'.$num.'"';

	while ($j < $num) {
	  $row = pg_fetch_row($result);
	  $j++;
	  $message = $message.',"row'.$j.'":"'.$row[0].'","friendUpdateTime":"'.$row[1].'"';
	}
	$message = $message.'}';
	echo $message;
}else{
	$friendName=$_GET["friendName"];
	$result2 = pg_query($conn, "SELECT ST_Y(P.CURRENTPOSITION),ST_X(P.CURRENTPOSITION) , P.LASTUPDATETIME FROM FRIENDLIST F,PALUSER P WHERE P.USERNAME='$friendName' AND F.USERID='$u_id' AND P.USERID=F.FRIENDID");	
	$num2 = pg_num_rows($result2);
	$row2 = pg_fetch_row($result2);
	if ($num2!=1) {
		echo "An error occurredcd-.".$num2.$row2[2].'"lat":"'.$row2[0].'","long":"'.$row2[1];
		exit;
	}
	
	$message2 = '{"lat":"'.$row2[0].'","long":"'.$row2[1].'","friendUpdateTime":"'.$row2[2].'"}';
	
	echo $message2;
}

?>	