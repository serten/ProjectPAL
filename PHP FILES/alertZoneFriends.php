<?php
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
$result = pg_query($conn, "SELECT PALUSER.USERNAME ,ST_X(PALUSER.CURRENTPOSITION) ,ST_Y(PALUSER.CURRENTPOSITION) FROM ALERTZONE,FRIENDLIST,PALUSER  WHERE ALERTZONE.USERID='$u_id' and ALERTZONE.USERID=FRIENDLIST.USERID and FRIENDLIST.FRIENDID=PALUSER.USERID and ST_Contains(ALERTZONE.ZONECOORDINATES, PALUSER.CURRENTPOSITION)");
if (!$result) {
  echo "An error occurred.\n";
  exit;
}
//echo $result;
//$numberFriendsInsideZones = 0;
$j = 0;
$num = pg_num_rows($result);

$message = '{"friendsNumber":"'.$num.'"';

if (0<$num){
	$message = $message.',';
}

while ($j < $num-1) {

  $row = pg_fetch_row($result);
  $j++;
  $message = $message.'"row'.$j.'":{"name":"'.$row[0].'","lat":"'.$row[1].'","long":"'.$row[2].'"},';

}
if (0<$num){
	$row = pg_fetch_row($result);
	$j++;
	$message = $message.'"row'.$j.'":{"name":"'.$row[0].'","lat":"'.$row[1].'","long":"'.$row[2].'"}';
}
$message = $message.'}';

echo $message;

?>