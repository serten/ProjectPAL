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
	echo("Denied: Username or/and Password Missing!");
}



function insert(){

	global $conn,$result,$u_pass,$u_name;

	$conn = pg_connect("host=postgredb.ctnfr2pmdvmf.us-west-2.rds.amazonaws.com port=5432 dbname=postgreDB user=postgreuser password=6089qwerty");
	if (!$conn) {
	  echo "Denied: An error occurred about Connection to DB.\n";
	  exit;
	}

	$result=pg_query($conn, "SELECT count(*) FROM PALUSER WHERE USERNAME = '$u_name'");
	$row = pg_fetch_row($result);
	$exist = $row[0];
	if($exist >0)
	{
		echo "Denied: This user name is exist in the System. Please try another username";
	}
	else
	{
		ini_set('date.timezone', 'America/Los_Angeles');
		$time = date('H:i:s', time()); 
		$date = date('Y-m-d')." ".$time;
		
		if (pg_query($conn, "INSERT INTO PALUSER ( USERNAME, PASSWORD,LASTUPDATETIME) VALUES ('$u_name','$u_pass',TIMESTAMP'$date')") == TRUE) {

			echo "New record created successfully";
		}
		else
		{
			echo "Denied: Error about query";
		}	
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
 
 
