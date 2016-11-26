<?php
	
	$temp = $_GET['music_name'];
	$music = "music/".$temp.".mp3";

	if(!file_exists($music)) {
		echo "<script>alert('sorry!');";
		echo "history.go(-1);";
		echo "</script>";
	}

	$file = fopen("/usr/local/hadoop/bin/songlist.txt", "r");

	while(!feof($file)) {
		$line = fgets($file);
		$split = explode(" ", $line);
		$replaceA = str_replace("_", " ", "$split[1]");
		$replaceB = str_replace("_", " ", "$split[2]");
		$replaceC = str_replace("_", " ", "$split[3]");

		if($temp == $replaceA) {
			echo "<h3><font color=\"#FFFFFF\">$temp</font><br>";
			echo "<font color=\"#FFFFFF\">$replaceB</font><br>";
			echo "<font color=\"#FFFFFF\">$replaceC</font></h3>";
			break;
		}
	}

	echo "<audio controls>";
	echo "<source src=\"$music\" type=\"audio/mpeg\">";
	echo "</audio>";
	
?>
