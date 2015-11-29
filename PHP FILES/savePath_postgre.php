<?php
$u_id;
$lat;
$long;
$useOnlyRoads;


if(($u_id=$_GET["userID"])&&($lat=$_GET["lat"])&&($long=$_GET["long"])&&($useOnlyRoads=$_GET["useOnlyRoads"]))
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
	global $u_id,$lat,$long,$useOnlyRoads;
	
	/////////////////////// getting road coordinates from google

	if($useOnlyRoads=="1")
	{
		$url = "http://maps.googleapis.com/maps/api/directions/json?origin=$lat,$long&destination=$lat,$long&sensor=false";
		$obj = json_decode(file_get_contents($url), true);

		$bounds =  json_encode($obj['routes'][0]);
		$newbounds = json_decode($bounds,true);

		$newLat =$newbounds['bounds']['northeast']['lat'];
		$newLong=$newbounds['bounds']['northeast']['lng'];
		
		$lat=$newLat;
		$long=$newLong;
	}
	
	////////////////////////

	$conn = pg_connect("host=postgredb.ctnfr2pmdvmf.us-west-2.rds.amazonaws.com port=5432 dbname=postgreDB user=postgreuser password=6089qwerty");
	if (!$conn) {
	  echo "An error occurred.\n";
	  exit;
	}
	ini_set('date.timezone', 'America/Los_Angeles');
	$time = date('H:i:s', time()); 
	$date = date('Y-m-d')." ".$time;
	
	$result = pg_query($conn, "UPDATE PALUSER SET CURRENTPOSITION = ST_GeomFromText('POINT($long $lat)', 4326), LASTUPDATETIME =TIMESTAMP'$date' WHERE USERID = '$u_id';");
    $result = pg_query($conn, "INSERT INTO PATHPOINTS (USERID, PATHCREATETIME,PATHCOORDINATES)VALUES ('$u_id',TIMESTAMP'$date',ST_GeomFromText('POINT($long $lat)', 4326))");	
	$result = pg_query($conn, "DELETE FROM PATHPOINTS  WHERE USERID='$u_id' AND PATHCREATETIME < (TIMESTAMP'$date' - interval '2 minute') ");

	$zoneDeActivateString="UPDATE ALERTZONE SET ZONEACTIVATED=0 WHERE ZONEID IN (SELECT A.ZONEID FROM ALERTZONE AS A, PALUSER AS U ". 
	"WHERE A.USERID='$u_id' AND U.USERID=A.USERID AND NOT ST_CONTAINS(A.ZONECOORDINATES,U.CURRENTPOSITION))";	
	$result = pg_query($conn, $zoneDeActivateString);	
	$zoneActivateString="UPDATE ALERTZONE SET ZONEACTIVATED=1 WHERE ZONEID IN (SELECT A.ZONEID FROM ALERTZONE AS A, PALUSER AS U ". 
	"WHERE A.USERID='$u_id' AND U.USERID=A.USERID AND ST_CONTAINS(A.ZONECOORDINATES,U.CURRENTPOSITION))";	
	/*$result = pg_query($conn, "SELECT  TIMESTAMP'$date',PATHCREATETIME,ST_AsGeoJSON(PATHCOORDINATES) FROM PATHPOINTS  WHERE USERID='$u_id' AND PATHCREATETIME >= (TIMESTAMP'$date' - interval '2 minute') order by PATHCREATETIME asc");	
	
	while ($row = pg_fetch_row($result)) {
	
		foreach ($row as &$rr)
		{

				if ($rr[0]=="{")
				{
					$decodedText = html_entity_decode($rr);
					$r = json_decode($decodedText, true);

					echo "LONG:".$r["coordinates"][0]."- LAT:".$r["coordinates"][1]."\n";	
				}		
				else
				{
					echo " ".$rr."  ";
				}
					
		}
		
	}*/
	if ($result!=null)
		return 1;
	else
		return 0;
	
}	

?>	
 
 