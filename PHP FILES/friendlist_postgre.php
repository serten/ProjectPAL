<?php
$u_id;
$result;




if($u_id=$_POST["userID"])
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
	global $conn,$result,$u_id;

	$conn = pg_connect("host=postgredb.ctnfr2pmdvmf.us-west-2.rds.amazonaws.com port=5432 dbname=postgreDB user=postgreuser password=6089qwerty");
	if (!$conn) {
	  echo "An error occurred.\n";
	  exit;
	}

    $result = pg_query($conn, "SELECT P.USERNAME,P.USERID FROM FRIENDLIST F,PALUSER P WHERE F.USERID='$u_id' AND P.USERID=F.FRIENDID");	
	
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
 
 