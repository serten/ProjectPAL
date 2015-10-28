<?php
$u_id=$_GET["userID"];
// Connects to the postgresql dbservice on the host machine
$conn = pg_connect("host=postgredb.ctnfr2pmdvmf.us-west-2.rds.amazonaws.com port=5432 dbname=postgreDB user=postgreuser password=6089qwerty");
if (!$conn) {
  echo "An error occurred.\n";
  exit;
}


// this section creates a table for alertzone information
$result = pg_query($conn, "DELETE FROM ALERTZONE WHERE USERID='$u_id'");
if (!$result) {
  echo "An error occurred.\n";
  exit;
}

?>

