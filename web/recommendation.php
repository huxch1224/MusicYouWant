<?php

	$username = $_COOKIE['username'];	
	$file = fopen("/usr/local/hadoop/bin/$username/recommendationlist.txt", "r");

	echo "<table>";

	for($i = 0; $i < 4; $i++) {
		$check = feof($file);

		if($check == 1) {
			break;
		}
		
		$line = fgets($file);
		$split = explode(" ", $line);
		$replaceA = str_replace("_", " ", "$split[0]");
		$replaceB = str_replace("_", " ", "$split[1]");
		$replaceC = str_replace("_", " ", "$split[2]");
		echo "<tr align=\"left\">";
		echo "<td><a href='recommendation_music.php?music_name=$replaceA'><font color=white>$replaceA</font></a></td>";
		echo "<td>&emsp;&emsp;</td>";
		echo "<td><font color=\"#FFFFFF\">$replaceB</font></td>";
		echo "<td>&emsp;&emsp;</td>";
		echo "<td><font color=\"#FFFFFF\">$replaceC</font></td>";
		echo "</tr>";
	}

	echo "</table>";
	
	fclose($file);

?>
