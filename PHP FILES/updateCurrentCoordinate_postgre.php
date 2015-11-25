<?php
$u_id;
$lat;
$long;


if(($u_id=$_POST["userID"])&&($lat=$_POST["lat"])&&($long=$_POST["long"]))
{
	if($rrr=control())
	{
		echo("$rrr");
	}
	else{
		echo("denied");
	}
}
else
{
	echo("denied");
}
function control()
{
	global $u_id,$lat,$long;

	$conn = pg_connect("host=postgredb.ctnfr2pmdvmf.us-west-2.rds.amazonaws.com port=5432 dbname=postgreDB user=postgreuser password=6089qwerty");
	if (!$conn) {
	  echo "An error occurred.\n";
	  exit;
	}
	ini_set('date.timezone', 'America/Los_Angeles');
	$time = date('H:i:s', time()); 
	$date = date('Y-m-d')." ".$time;
	
    $result = pg_query($conn, "UPDATE PALUSER SET CURRENTPOSITION = ST_GeomFromText('POINT($lat $long)', 4326), LASTUPDATETIME =TIMESTAMP'$date' WHERE USERID = '$u_id';");	
	
	$zoneDeActivateString="UPDATE ALERTZONE SET ZONEACTIVATED=0 WHERE ZONEID IN (SELECT A.ZONEID FROM ALERTZONE AS A, PALUSER AS U ". 
	"WHERE A.USERID='$u_id' AND U.USERID=A.USERID AND NOT ST_CONTAINS(A.ZONECOORDINATES,U.CURRENTPOSITION))";
	
	$result = pg_query($conn, $zoneDeActivateString);
	
	$zoneActivateString="UPDATE ALERTZONE SET ZONEACTIVATED=1 WHERE ZONEID IN (SELECT A.ZONEID FROM ALERTZONE AS A, PALUSER AS U ". 
	"WHERE A.USERID='$u_id' AND U.USERID=A.USERID AND ST_CONTAINS(A.ZONECOORDINATES,U.CURRENTPOSITION))";	
	
	$result = pg_query($conn, $zoneActivateString);
	
	if($result != "")
	{	
		return 1;
	}
	else
	{
		return 0;
	}
}	

?>	
 
 