<?php
$userID;
$input;
$result;

if(($input=$_POST["inputString"])&&($userID=$_POST["userID"]))
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
	echo("denied, input string is missing");
}


function control()
{
	global $result,$input,$userID;

	$conn = pg_connect("host=postgredb.ctnfr2pmdvmf.us-west-2.rds.amazonaws.com port=5432 dbname=postgreDB user=postgreuser password=6089qwerty");
	if (!$conn) {
	  echo "denied, an error occurred about connection.\n";
	  exit;
	}	
	

	$query="SELECT USERNAME,USERID FROM PALUSER WHERE USERNAME LIKE '$input%' AND USERID != '$userID'";
	
    $result = pg_query($conn, $query);	
	
	if (!$result) {
		echo "denied, an error occurred about query.\n";
		return 0;
	}
	
	$names="";
	$ids="";
	$first=true;
	while ($row = pg_fetch_row($result)) {
	

			if($first){
				$names =$row[0];
				$ids =$row[1];
				$first=false;
			}
			else{
				$names =$names." ".$row[0];
				$ids=$ids." ".$row[1];
			}	
				

	
	}
	if ($ids == "")
	{
		echo "denied, empty result";
	}
		 
	return $names." ".$ids;
}	

?>	
 
 