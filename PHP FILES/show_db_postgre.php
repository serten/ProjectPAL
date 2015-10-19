<?php

// Connects to the postgresql dbservice on the host machine
$conn = pg_connect("host=postgredb.ctnfr2pmdvmf.us-west-2.rds.amazonaws.com port=5432 dbname=postgreDB user=postgreuser password=6089qwerty");
if (!$conn) {
  echo "An error occurred.\n";
  exit;
}


// this section creates a table for user information
$result = pg_query($conn, "SELECT USERID, USERNAME, PASSWORD, LASTUPDATETIME,ST_X(CURRENTPOSITION), ST_Y(CURRENTPOSITION) FROM PALUSER");
if (!$result) {
  echo "An error occurred.\n";
  exit;
}
echo "<p>USER INFORMATION TABLE</p><table border=\"1\"><tr>
		<th>USERID:</th>
		<th>USERNAME:</th>
		<th>PASSWORD:</th>
		<th>LASTUPDATETIME:</th>
		<th>LAT:</th>
		<th>LONG:</th></tr>";
while ($row = pg_fetch_row($result)) {
  echo "<tr>";
  foreach ($row as &$rr)
  {
	echo "<td>".$rr."</td>";
  }
 echo "</tr>";
}
echo "</table>";

// this section creates a table for friendlist information
$result = pg_query($conn, "SELECT FRIENDLISTID, USERID, FRIENDID, FRIENDLISTTIME FROM FRIENDLIST");
if (!$result) {
  echo "An error occurred.\n";
  exit;
}

echo "<hr><p>FRIENDLIST TABLE</p><table border=\"1\"><tr>
		<th>FRIENDLISTID:</th>
		<th>USERID:</th>
		<th>FRIENDID:</th>
		<th>FRIENDLISTTIME:</th></tr>";
while ($row = pg_fetch_row($result)) {
  echo "<tr>";
 foreach ($row as &$rr)
  {
	echo "<td>".$rr."</td>";
  }
 echo "</tr>";
}
echo "</table>";


// this section creates a table for alertzone information
$result = pg_query($conn, "SELECT ZONEID, USERID, ZONECREATETIME, ZONEACTIVATED,ST_AsGeoJSON(ZONECOORDINATES) FROM ALERTZONE");
if (!$result) {
  echo "An error occurred.\n";
  exit;
}
echo "<hr><p>ALERTZONE TABLE</p><table border=\"1\"><tr>
		<th>ZONEID:</th>
		<th>USERID:</th>
		<th>ZONECREATETIME:</th>
		<th>ZONEACTIVATED:</th>
		<th>ZONECOORDINATES-X</th></tr>";
while ($row = pg_fetch_row($result)) {
  echo "<tr>";
  foreach ($row as &$rr)
  {
	if($rr[0]==="{")
	{	
			$decodedText = html_entity_decode($rr);
			$r = json_decode($decodedText, true);
			echo "<td>";
			foreach ($r["coordinates"][0] as $coord)
				echo "LONG:".$coord[0]."- LAT:".$coord[1]." / ";	
			echo "</td>";				
	}	
	else
	
		echo "<td>".$rr."</td>";
  }
 echo "</tr>";
}
echo "</table>";

// this section creates a table for path information
$result = pg_query($conn, "SELECT PATHID, USERID, PATHCREATETIME,ST_X(PATHCOORDINATES), ST_Y(PATHCOORDINATES) FROM PATHPOINTS");
if (!$result) {
  echo "An error occurred.\n";
  exit;
}
echo "<hr><p>PATH INFORMATION TABLE</p><table border=\"1\"><tr>
		<th>PATHID:</th>
		<th>USERID:</th>
		<th>PATHCREATETIME:</th>
		<th>PATH - LAT:</th>
		<th>PATH - LONG:</th></tr>";
while ($row = pg_fetch_row($result)) {
  echo "<tr>";
  foreach ($row as &$rr)
  {
	echo "<td>".$rr."</td>";
  }
 echo "</tr>";
}
echo "</table>";



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

