<?php
$u_id=$_GET["userID"];
// Connects to the postgresql dbservice on the host machine
$conn = pg_connect("host=postgredb.ctnfr2pmdvmf.us-west-2.rds.amazonaws.com port=5432 dbname=postgreDB user=postgreuser password=6089qwerty");
if (!$conn) {
  echo "An error occurred.\n";
  exit;
}


// this section creates a table for alertzone information
$result = pg_query($conn, "SELECT ST_AsGeoJSON(PATHCOORDINATES) FROM PATHPOINTS WHERE USERID='$u_id'");
if (!$result) {
  echo "An error occurred.\n";
  exit;
}

$polyCounts=0;
$polyType=0;
$main=[];
$coordinates=[];
$polytypes=[];

//$polyCounts++;
while ($row = pg_fetch_row($result)) {
  //echo "<tr>";
  //$polyCounts++;
  
  //echo $row;
  foreach ($row as &$rr)
  {
	$polyCounts=1;
	$polyType++;
	//echo $rr;
	if($rr[0]==="{")
	{	
			
			$decodedText = html_entity_decode($rr);
			$r = json_decode($decodedText, true);
			//echo "LONG:".$coord[0]."- LAT:".$coord[1]." / ";	
			//$polyType++;
			array_push($coordinates,$r["coordinates"][0],$r["coordinates"][1]);
			//echo "</td>";	
			
			//$coordinates=[];
	}	
	else
	{
	
		//echo "<td>".$rr."</td>";
	}
  }
  
  
  //echo "<td>polyType:".$polyType."</td>";
  //array_push($polytypes,$polyType);
  //$polyType=0;
 //echo "</tr>";
}
array_push($main,$coordinates);
array_push($polytypes,$polyType);
//echo "</table>";

$message="{\"polys\":{\"polyCounts\":\"".$polyCounts."\"";
for ($i = 0; $i<$polyCounts ; $i++)	
{	

		$message=$message.",\"poly".$i."\":{\"polyType\":\"".$polytypes[$i]."\",\"points\":{";
	
	for ($j = 0; $j<$polytypes[$i]*2 ; )
	{
		$pointno=$j/2;
		if($j==0)
			$message=$message."\"point".$pointno."is\":{\"lat\":\"".$main[$i][$j]."\",\"long\":\"".$main[$i][$j+1]."\"}";
		else
			$message=$message.",\"point".$pointno."is\":{\"lat\":\"".$main[$i][$j]."\",\"long\":\"".$main[$i][$j+1]."\"}";

		$j++;
		$j++;
	
	}
	$message=$message."}}";
}
$message=$message."},\"circles\":{\"circCounts\":\"0\",\"circ0\":{\"centerLat\":\"0\",\"centerLong\":\"0\",\"radius\":\"0\"}}}";
echo $message;


//$result = pg_query($conn, "SELECT ST_AsText(geom) FROM mytable"); 

//$result = pg_query($conn, "SELECT ST_X(geom), ST_Y(geom) FROM mytable");
/*$result = pg_query($conn, "SELECT ST_AsGeoJSON(geom) FROM yourtable"); 
if (!$result) {
  echo "An error occurred.\n";
  exit;
}
echo "<table border=\"1\"><tr>
		<th>X:</th>
		<th>Y:</th><tr>";
while ($row = pg_fetch_row($result)) {
$decodedText = html_entity_decode($row[0]);
$r = json_decode($decodedText, true);

foreach ($r["coordinates"][0] as $rr)

  echo "<tr><td>".$rr[0]."</td><td>".$rr[1]."</td></tr>";
}
echo "</table>";
*/


/*
//user table
$stid = oci_parse($conn, 'select USERID, USERNAME,PASSWORD, LASTUPDATETIME from PALUSER');
//$stid = oci_parse($conn, 'select PALUSER.USERNAME from PALUSER');
oci_execute($stid);
echo "<table border=\"1\"><tr>
		<th>USERID:</th>
		<th>USERNAME:</th>
		<th>PASSWORD:</th>		
		<th>LASTUPDATETIME:</th><tr>";
while(($row = oci_fetch_array($stid,OCI_DEFAULT)) != false) {
    // Use the uppercase column names for the associative array indices
       
			echo "<tr><td>".$row[0]."</td><td>".$row[1]."</td><td>".$row[2]."</td><td>".$row[3]."</td></tr>";
}
echo "</table>";
*/


/*
INSERT INTO mytable (geom,name)VALUES ( ST_GeomFromText('POINT(167.213123 -89.131233123)', 26910),'son');

INSERT INTO yourtable (geom,name)VALUES ( ST_GeomFromText('POLYGON((-71.1776585052917 42.3902909739571,-71.1776820268866 42.3903701743239,
-71.1776063012595 42.3903825660754,-71.1775826583081 42.3903033653531,-71.1776585052917 42.3902909739571))', 26910),'POLYGON');

*/
?>

