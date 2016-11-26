<?php
	
	$check = $_COOKIE['username'];

	if($check == "") {
		echo "<script>alert('please login!');history.go(-1);</script>";
	}
	else {
		$temp = htmlspecialchars($_POST['music_name']);

		if($temp == "") {
			echo "<script>alert('check input!');";
			echo "history.go(-1);";
			echo "</script>";
		}

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
				echo "<h3><font color=\"#FFFFFF\">Name:&emsp;$temp<br>";
				echo "Artist:&emsp;$replaceB<br>";
				echo "Album:&emsp;$replaceC</font></h3>";
				break;
			}
		}

		echo "<audio controls>";
		echo "<source src=\"$music\" type=\"audio/mpeg\">";
		echo "</audio>";
	}
	
?>
