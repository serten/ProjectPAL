<?php
$u_name;
$u_pass;
$result;


if(($u_name=$_POST["username"])&&($u_pass=$_POST["password"]))
{
	insert();
	
}
else
{
	echo("registration denied");
}



function insert(){

	global $conn,$result,$u_pass,$u_name;

	$conn = pg_connect("host=postgredb.ctnfr2pmdvmf.us-west-2.rds.amazonaws.com port=5432 dbname=postgreDB user=postgreuser password=6089qwerty");
	if (!$conn) {
	  echo "An error occurred.\n";
	  exit;
	}


	
	if (pg_query($conn, "INSERT INTO PALUSER ( USERNAME, PASSWORD) VALUES ('$u_name','$u_pass')") == TRUE) {

			echo "New record created successfully";
	}
	else
	{
			echo "Error: <br>";
	}	
	//$conn->query("INSERT INTO $tablename VALUES ('2','$u_name','$u_pass')");
/*	
	$result = $conn->query("SELECT username FROM $tablename WHERE username='$u_name' and password='$u_pass'"); 
	$resultuname;
	$resultpass;
	if($result->num_rows>0)
	{
		foreach ($result as $r)
		{
			$resultuname=$r["username"];
			$resultpass=$r["password"];
		}

		if($resultname==$u_name)&&($resultpass==$u_pass)
		{
			echo("Registration Completed");
		}
	}
	else
	{
		echo("no saved result");
	}
*/

	
}


?>	
 
 
