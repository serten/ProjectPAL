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
	

	$result = pg_query($conn, "SELECT FRIENDLISTID FROM FRIENDLIST WHERE USERID='$u_id' AND FRIENDID='$input'");	
	
	$row = pg_fetch_row($result);
	
	if($row[0])
	{
		return "ALREADY A FRIEND";		
	}
	else{
	
		if (pg_query($conn, "INSERT INTO FRIENDLIST ( USERID, FRIENDID) VALUES ('$u_id','$input')") == TRUE) 
		{
			if (pg_query($conn, "INSERT INTO FRIENDLIST ( USERID, FRIENDID) VALUES ('$input','$u_id')") == TRUE) 
			{
				return "New record created successfully";
			}
			else
				return "denied. Only one way relationship inserted!";
		}
		else
		{
				return "denied. Insertion query error!";
		}	
	
		
	
	}
}	

?>	
 
 