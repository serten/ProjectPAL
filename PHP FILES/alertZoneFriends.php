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
$result = pg_query($conn, "SELECT PALUSER.USERNAME ,ST_X(PALUSER.CURRENTPOSITION) ,ST_Y(PALUSER.CURRENTPOSITION), PALUSER.LASTUPDATETIME, ALERTZONE.ZONENAME, FRIENDLIST.FRIENDLISTID  FROM ALERTZONE,FRIENDLIST,PALUSER  WHERE ALERTZONE.USERID='$u_id' and ALERTZONE.USERID=FRIENDLIST.USERID and FRIENDLIST.FRIENDID=PALUSER.USERID and ST_Contains(ALERTZONE.ZONECOORDINATES, PALUSER.CURRENTPOSITION) ORDER BY FRIENDLISTID;");
$userIdPersonResult = pg_query($conn, "SELECT PALUSER.USERNAME ,ST_X(PALUSER.CURRENTPOSITION) ,ST_Y(PALUSER.CURRENTPOSITION), PALUSER.LASTUPDATETIME, ALERTZONE.ZONENAME  FROM ALERTZONE,PALUSER  WHERE ALERTZONE.USERID='$u_id' and ALERTZONE.USERID=PALUSER.USERID and ST_Contains(ALERTZONE.ZONECOORDINATES, PALUSER.CURRENTPOSITION) LIMIT 1;");
$res = pg_query($conn, "SELECT FRIENDLISTID FROM FRIENDLIST WHERE FRIENDLIST.USERID='$u_id';");
$numberOfFriends = pg_num_rows($res);
//echo $result;
//$numberFriendsInsideZones = 0;
$j = 0;
//We count the number of rows which basically is the number of friends
$num = pg_num_rows($result)+pg_num_rows($userIdPersonResult);
$num1 = pg_num_rows($result);
$num2 = pg_num_rows($userIdPersonResult);
if (!(0<$num)) {
  echo "An error occurred.\n";
  exit;
}
$message = '{"numberOfFriends":"'.$numberOfFriends.'","peopleInsideZoneNumber":"'.$num.'","friendsInsideZoneNumber":"'.$num1.'"';

if (0<$num){
	$message = $message.',';
}
//Making the file being sent back to the app
while ($j < $num1-1) {

  $row = pg_fetch_row($result);
  $j++;
  $message = $message.'"row'.$j.'":{"name":"'.$row[0].'","lat":"'.$row[1].'","long":"'.$row[2].'","friendUpdateTime":"'.$row[3].'","zoneName":"'.$row[4].'","friendListID":"'.$row[5].'"},';
}
//Closing the file being sent back to the app

if (0<$num1){
	$row = pg_fetch_row($result);
	$j++;
	$message = $message.'"row'.$j.'":{"name":"'.$row[0].'","lat":"'.$row[1].'","long":"'.$row[2].'","friendUpdateTime":"'.$row[3].'","zoneName":"'.$row[4].'","friendListID":"'.$row[5].'"}';
	if (0<$num2){
		$message = $message.',';
	}
}
if (0<$num2) {
	$j++;
	$row = pg_fetch_row($userIdPersonResult);
	$message = $message.'"row'.$j.'":{"name":"'.$row[0].'","lat":"'.$row[1].'","long":"'.$row[2].'","friendUpdateTime":"'.$row[3].'","zoneName":"'.$row[4].'","friendListID":"0"}';
}
$message = $message.'}';
//File is sent to the app
echo $message;
?>