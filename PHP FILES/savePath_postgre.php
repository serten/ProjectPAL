<?php
$u_id;
$lat;
$long;


if(($u_id=$_GET["userID"])&&($lat=$_GET["lat"])&&($long=$_GET["long"]))
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
		
    $result = pg_query($conn, "INSERT INTO PATHPOINTS (USERID, PATHCREATETIME,PATHCOORDINATES)VALUES ('$u_id',TIMESTAMP'$date',ST_GeomFromText('POINT($lat $long)', 4326))");	
	$result = pg_query($conn, "DELETE FROM PATHPOINTS  WHERE USERID='$u_id' AND PATHCREATETIME < (TIMESTAMP'$date' - interval '2 minute') ");	
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
 
 