<?php
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST');
$u_id=$_GET["userID"];
$k_friends=$_GET["kFriends"];
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

$result = pg_query($conn, "SELECT P.USERNAME ,ST_Y(P.CURRENTPOSITION), ST_X(P.CURRENTPOSITION), P.LASTUPDATETIME , ST_Distance(P.CURRENTPOSITION,P2.CURRENTPOSITION) FROM FRIENDLIST F,PALUSER P, PALUSER P2 WHERE F.USERID='$u_id' AND P2.USERID='$u_id' AND P.USERID=F.FRIENDID ORDER BY ST_Distance(P.CURRENTPOSITION,P2.CURRENTPOSITION) ASC Limit '$k_friends';");	
$num = pg_num_rows($result);
	
if (!(0<$num)) {
  echo "An error occurred.\n";
  exit;
}
$j = 0;
$message = '{"numberOfFriendsReturned":"'.$num.'"';

while ($j < $num) {
  $row = pg_fetch_row($result);
  $j++;
  $message = $message.',"name'.$j.'":"'.$row[0].'","lat'.$j.'":"'.$row[1].'","long'.$j.'":"'.$row[2].'","friendUpdateTime'.$j.'":"'.$row[3].'"';
}
$message = $message.'}';
echo $message;

?>	