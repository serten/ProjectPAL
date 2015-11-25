<?php
$u_id=$_GET["userID"];
// Connects to the postgresql dbservice on the host machine
$conn = pg_connect("host=postgredb.ctnfr2pmdvmf.us-west-2.rds.amazonaws.com port=5432 dbname=postgreDB user=postgreuser password=6089qwerty");
if (!$conn) {
  echo "An error occurred.\n";
  exit;
}
	ini_set('date.timezone', 'America/Los_Angeles');
	$time = date('H:i:s', time()); 
	$date = date('Y-m-d')." ".$time;

// this section creates a table for alertzone information
$result = pg_query($conn, "SELECT ST_AsGeoJSON(PATHCOORDINATES) FROM PATHPOINTS WHERE USERID='$u_id' AND PATHCREATETIME >= (TIMESTAMP'$date' - interval '2 minute') order by PATHCREATETIME asc");
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
/*
$url = "http://maps.googleapis.com/maps/api/directions/json?origin=34.0276479374902,-118.286682197115&destination=34.0214124,-118.2843956&sensor=false";
$obj = json_decode(file_get_contents($url), true);

$bounds =  json_encode($obj['routes'][0]);
$newbounds = json_decode($bounds,true);

$newLat =$newbounds['bounds']['northeast']['lat'];
$newLong=$newbounds['bounds']['northeast']['lng'];
*/

?>

