<?php
$u_name;
$u_pass;
$result;




if(($u_name=$_POST["username"])&&($u_pass=$_POST["password"]))
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
	echo("denied");
}
function control()
{
	global $conn,$result,$u_pass,$u_name;

	$conn = pg_connect("host=postgredb.ctnfr2pmdvmf.us-west-2.rds.amazonaws.com port=5432 dbname=postgreDB user=postgreuser password=6089qwerty");
	if (!$conn) {
	  echo "An error occurred.\n";
	  exit;
	}

    $result = pg_query($conn, "SELECT USERID FROM PALUSER WHERE USERNAME='$u_name' and PASSWORD='$u_pass'");	
	$row = pg_fetch_row($result);
	
	if($row != "")
	{
		foreach ($row as $r)
		{
			$val=$r;
		}
		return $val;
	}
	else
	{
		return 0;
	}
}	

?>	
 
 