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
	

	$query="SELECT USERNAME FROM PALUSER WHERE USERNAME LIKE '$input%'";

	
    $result = pg_query($conn, $query);	
	
	if (!$result) {
		echo "An error occurred about query.\n";
		return 0;
	}
	
	$sum="";
	while ($row = pg_fetch_row($result)) {
	
		foreach ($row as &$rr)
		{
			
			$sum =$sum.$rr." ";
		}
	
	}
		 
	return $sum;
}	

?>	
 
 