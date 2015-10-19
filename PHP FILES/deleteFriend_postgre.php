<?php
$input;
$u_id;
$result;

if(($input=$_POST["inputString"])&&($u_id=$_POST["userID"]))
{
	if($rrr=control())
	{
		echo("$rrr");
	}
	else{
		echo("deniedforentry");
	}
}
else
{
	echo("Xdenied");
}


function control()
{
	global $result,$input,$u_id;

	$conn = pg_connect("host=postgredb.ctnfr2pmdvmf.us-west-2.rds.amazonaws.com port=5432 dbname=postgreDB user=postgreuser password=6089qwerty");
	if (!$conn) {
	  echo "An error occurred.\n";
	  exit;
	}	
	
	if (pg_query($conn, "DELETE FROM FRIENDLIST WHERE USERID='$u_id' AND FRIENDID='$input'") == TRUE) 
	{
		if (pg_query($conn, "DELETE FROM FRIENDLIST WHERE USERID='$input' AND FRIENDID='$u_id'") == TRUE) 
		{
			return "Record deleted successfully";
		}
		else
				return "denied. Only one way relationship deleted!";
		
	}
	else
	{
			return "denied. Deletion query error!";
	}	
	
}	

?>	
 
 