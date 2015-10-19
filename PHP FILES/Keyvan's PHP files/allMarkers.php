<?php
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST');
 if(isset($_GET['user'])){  //php code can be used almost everywhere in you file if it is "*.php" file
				
				
				$filename = $_GET['user'].'-allMarkers.txt';
				
				if (!file_exists ($filename)){
					fopen($filename);
				}
				$fp = fopen($filename, "a+","\r\n");
				
				
				//for the polys
				if (isset($_GET['counterPoly'])){
					
					$counterPoly = $_GET['counterPoly'];
					$Polytype = $_GET['Polytype'];
					
					if ($_GET['start']=="true"){
						$polyCounts = $_GET['polyCounts'];
						file_put_contents($filename, '');
						fclose($filename);
						$note = '{"polys":{"polyCounts":"'.$polyCounts.'",';
						$write = fputs($fp, $note);
					}
					
					echo "hi-0";
					
					if ($_GET['start']=="false"){
						$write = fputs($fp, ',');
					}
					
					$note = '"poly'.$counterPoly.'":{"polyType":"'.$Polytype.'","points":{';
					$write = fputs($fp, $note);
					switch ($Polytype) {
						case "3":
							$Points = '"point0is":{"lat":"'.$_GET['point0lat'].'","long":"'.$_GET['point0long'].'"},"point1is":{"lat":"'.$_GET['point1lat'].'","long":"'.$_GET['point1long'].'"},"point2is":{"lat":"'.$_GET['point2lat'].'","long":"'.$_GET['point2long'].'"}}}';
							break;
						case "4":
							$Points = '"point0is":{"lat":"'.$_GET['point0lat'].'","long":"'.$_GET['point0long'].'"},"point1is":{"lat":"'.$_GET['point1lat'].'","long":"'.$_GET['point1long'].'"},"point2is":{"lat":"'.$_GET['point2lat'].'","long":"'.$_GET['point2long'].'"},"point3is":{"lat":"'.$_GET['point3lat'].'","long":"'.$_GET['point3long'].'"}}}';
							break;
						case "5":
							$Points = '"point0is":{"lat":"'.$_GET['point0lat'].'","long":"'.$_GET['point0long'].'"},"point1is":{"lat":"'.$_GET['point1lat'].'","long":"'.$_GET['point1long'].'"},"point2is":{"lat":"'.$_GET['point2lat'].'","long":"'.$_GET['point2long'].'"},"point3is":{"lat":"'.$_GET['point3lat'].'","long":"'.$_GET['point3long'].'"},"point4is":{"lat":"'.$_GET['point4lat'].'","long":"'.$_GET['point4long'].'"}}}';
							break;
						case "6":
							$Points = '"point0is":{"lat":"'.$_GET['point0lat'].'","long":"'.$_GET['point0long'].'"},"point1is":{"lat":"'.$_GET['point1lat'].'","long":"'.$_GET['point1long'].'"},"point2is":{"lat":"'.$_GET['point2lat'].'","long":"'.$_GET['point2long'].'"},"point3is":{"lat":"'.$_GET['point3lat'].'","long":"'.$_GET['point3long'].'"},"point4is":{"lat":"'.$_GET['point4lat'].'","long":"'.$_GET['point4long'].'"},"point5is":{"lat":"'.$_GET['point5lat'].'","long":"'.$_GET['point5long'].'"}}}';
							break;
						default:
							$Points = '}}';
							break;
					}
					echo "hi-3";
					$write = fputs($fp, $Points);
				}
				
				
				
				
				
				//for the circles
				if (isset($_GET['counterCirc'])){
					
					
					$counterCirc = $_GET['counterCirc'];
					$centerLat = $_GET['centerLat'];
					$centerLong = $_GET['centerLong'];
					$radius = $_GET['radius'];
					
					if ($_GET['start']=="true"){
						$circCounts = $_GET['circCounts'];
						$note = '},"circles":{"circCounts":"'.$circCounts.'",';
						$write = fputs($fp, $note);
					}
					
					if ($_GET['start']=="false"){
						$write = fputs($fp, ',');
					}
					
					
					$note = '"circ'.$counterCirc.'":{"centerLat":"'.$centerLat.'","centerLong":"'.$centerLong.'","radius":"'.$radius.'"}';
					$write = fputs($fp, $note);
				}

				
				if (isset($_GET['end']) && $_GET['end']=="true"){
					$write = fputs($fp, '}}');
				}
				
				echo "hi-4";
 }
?>