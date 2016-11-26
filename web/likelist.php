<?php
	
	$username = $_COOKIE['username'];
	$exist = "/usr/local/hadoop/bin/$username/likelist.txt";

	if(!file_exists($exist)) {
		echo "<font color=\"#FFFFFF\">null</font>";
	}
	else {
		$file = fopen("/usr/local/hadoop/bin/$username/likelist.txt", "r");

		echo "<table>";

		while(!feof($file))
		{
			$line = fgets($file);
			$split = explode(" ", $line);
			$replaceA = str_replace("_", " ", "$split[0]");
			$replaceB = str_replace("_", " ", "$split[1]");
			$replaceC = str_replace("_", " ", "$split[2]");
			echo "<tr align=\"left\">";
			echo "<td><a href='select_music.php?music_name=$replaceA'><font color=white>$replaceA</font></a></td>";
			echo "<td>&emsp;&emsp;</td>";
			echo "<td><font color=\"#FFFFFF\">$replaceB</font></td>";
			echo "<td>&emsp;&emsp;</td>";
			echo "<td><font color=\"#FFFFFF\">$replaceC</font></td>";
			echo "</tr>";
		}

		echo "</table>";
		fclose($file);
	}

?>
