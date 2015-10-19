<?php
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST');
 if(isset($_GET['lat']) && isset($_GET['long']) && isset($_GET['user']) ){  //php code can be used almost everywhere in you file if it is "*.php" file
				
				$userName = $_GET['user'].'-mapsloc.txt';
				//$userName = $_GET['user'].'-mapsloctime.txt';
				echo $userName;
				if (!file_exists ($userName)){
					fopen($userName);
				}
				$data = file_get_contents('mapsnopoints.txt');
				$data = strval(((int) $data)+1);
				//file_put_contents('mapsUserInUse.txt', '');
				file_put_contents('mapsUserInUse.txt',$userName);
				fclose('mapsUserInUse.txt');
					//file_put_contents('mapsloc.txt', '');
				$alllatlong = file_get_contents($userName);
				$arr = explode("\n", $alllatlong);
				if (count($arr)>5){
					array_shift($arr);
					$alllatlong = implode("\n", $arr);
					file_put_contents($userName, '');
					file_put_contents($userName, $alllatlong);
					fclose($userName);
				}
				file_put_contents('mapsnopoints.txt', $data);
				fclose('mapsnopoints.txt');
				
				$lat = $_GET['lat'];
				$long = $_GET['long'];
				$filename = $userName;
				
				
				$fp = fopen($filename, "a+","\r\n");
				
				date_default_timezone_set('America/Los_Angeles');

				$note = $data.'- At Time: '. date('l-d-M-Y H:i:s', time()).'  - Latitude: '.$lat." - Longitude:".$long."\n";
				$write = fputs($fp, $note);
 }
 
 /* <!DOCTYPE html>
<html>
	<body>
	
	<div id="p1"></div>
		<script src="//code.jquery.com/jquery-1.11.3.min.js"></script>
<script src="//code.jquery.com/jquery-migrate-1.2.1.min.js"></script>
	<script type="text/javascript">
	
	var fileInUse;
	$(document).ready(function(){
			
			$.get('/mapsUserInUse.txt', function(data) {    
				fileInUse = '/'+data;
				fclose('mapsUserInUse.txt');
			});
			
			// LOAD file and split line by line and append divs
			$.get(fileInUse, function(data) {    
				var lines = data.split("\n");

				$.each(lines, function(n, elem) {
				   $('#p1').append('<div>' + elem + '</div>');
				});
				fclose($fileInUse);
			});
	});
	var myVar = setInterval(myTimer, 3500);

	function myTimer() {
		$.get('/mapsUserInUse.txt', function(data) {    
				fileInUse = '/'+data;
			fclose('mapsUserInUse.txt');
			});
		$.get(fileInUse, function(data) {    
			var lines = data.split("\n");
			$('#p1').html('');
			$('#p1').append('<div>' + fileInUse + '</div>');
			$.each(lines, function(n, elem) {
			   $('#p1').append('<div>' + elem + '</div>');
			});
			fclose($fileInUse);
		});
			
	}
	
	
	</script>
	
		<NOSCRIPT>	
	</body>
</html>  */
?>