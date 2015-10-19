<?php
$input;
$result;

if($input=$_POST["inputString"])
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
	echo("Xdenied");
}


function control()
{
	global $result,$input;

	$conn = pg_connect("host=postgredb.ctnfr2pmdvmf.us-west-2.rds.amazonaws.com port=5432 dbname=postgreDB user=postgreuser password=6089qwerty");
	if (!$conn) {
	  echo "An error occurred.\n";
	  exit;
	}	
	

	$query="SELECT USERNAME,USERID FROM PALUSER WHERE USERNAME LIKE '$input%'";
	
    $result = pg_query($conn, $query);	
	
	if (!$result) {
		echo "An error occurred about query.\n";
		return 0;
	}
	
	$names="";
	$ids="";
	$first=true;
	while ($row = pg_fetch_row($result)) {
	

			if($first){
				$sum =$row[0];
				$ids =$row[1];
				$first=false;
			}
			else{
				$sum =$sum." ".$row[0];
				$ids=$ids." ".$row[1];
			}	
				

	
	}
		 
	return $sum." ".$ids;
}	

?>	
 
 