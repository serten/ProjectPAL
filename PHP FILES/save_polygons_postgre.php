<?php
$u_id=$_GET["userID"];
$polyType=$_GET["Polytype"];
$zoneName=$_GET["zoneName"];

/*
INSERT INTO ALERTZONE ( USERID, ZONECREATETIME, ZONEACTIVATED, ZONECOORDINATES)VALUES (
'1',
TIMESTAMP '1997-01-31 09:26:50.12',
'0',
ST_GeomFromText('POLYGON((34.018665999999996 -118.282337, 34.018114 -118.291551, 34.025272 -118.285748, 34.018665999999996 -118.282337))', 4326) --note begin and end point should be same
);*/
$polygon=[];

for ($j = 0; $j<$polyType ;$j++ )
{
	$coordinates=[];
	$lat = $_GET["point".$j."lat"];	
	$long = $_GET["point".$j."long"];
	array_push($coordinates,$lat,$long);	
	
	array_push($polygon,$coordinates);			
}

ini_set('date.timezone', 'America/Los_Angeles');
$time = date('H:i:s', time()); 
$date = date('Y-m-d')." ".$time;

$queryStr="INSERT INTO ALERTZONE ( USERID, ZONECREATETIME, ZONEACTIVATED, ZONECOORDINATES , ZONENAME)VALUES('$u_id',TIMESTAMP'$date','0',ST_GeomFromText('POLYGON((";
	
for($i=0;$i<count($polygon);$i++)
{	
    $queryStr=$queryStr.$polygon[$i][0]." ".$polygon[$i][1].",";
}
$queryStr=$queryStr.$polygon[0][0]." ".$polygon[0][1]."))', 4326),'$zoneName')";

$conn = pg_connect("host=postgredb.ctnfr2pmdvmf.us-west-2.rds.amazonaws.com port=5432 dbname=postgreDB user=postgreuser password=6089qwerty");
if (!$conn) {
  echo "An error occurred.\n";
  exit;
}

$result = pg_query($conn, $queryStr);
if (!$result) {
  echo "An error occurred.\n";
  exit;
}


?>