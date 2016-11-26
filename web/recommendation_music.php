<?php
	
	$check = $_COOKIE['username'];

	if($check == "") {
		echo "<script>alert('please login!');history.go(-1);</script>";
	}
	else {
		$trans = $_GET['music_name'];
		$replace = str_replace(" ", "_", "$trans");
		$music = $_GET['music_name'].".mp3";
		
		echo "<link rel=\"stylesheet\" type=\"text/css\" href=\"./assets/r_style.css\" />";	
		echo "<h3><font color=\"#FFFFFF\">$music</font></h3>";	
		echo "<div>";
		echo "<audio controls>";
		echo "<source src=\"music/$music\" type=\"audio/mpeg\">";
		echo "</audio>";
		echo "</div>";
		echo "<br>";
		
		echo "<div>";
		echo "<form method=\"post\" action=\"done.php?music=$replace\">";
		echo "<div>";
		echo "<input type=\"radio\" name=\"tagg\" value=\"1\" checked=\"\"><font color=\"#FFFFFF\">&nbsp;like&emsp;&emsp;&emsp;</font>";
		echo "<input type=\"radio\" name=\"tagg\" value=\"0\"><font color=\"#FFFFFF\">&nbsp;dislike</font>";
		echo "</div>";
		echo "<br>";
		echo "<div>";
		echo "<input type=\"radio\" name=\"tag\" value=\"0\" checked=\"\"><font color=\"#FFFFFF\">&nbsp;Easy-Listening&emsp;";
		echo "<input type=\"radio\" name=\"tag\" value=\"1\"><font color=\"#FFFFFF\">&nbsp;Sentimental&emsp;</font>";
		echo "<input type=\"radio\" name=\"tag\" value=\"2\"><font color=\"#FFFFFF\">&nbsp;Excited&emsp;</font>";
		echo "<input type=\"radio\" name=\"tag\" value=\"3\"><font color=\"#FFFFFF\">&nbsp;Peaceful&emsp;</font>";
		echo "<input type=\"radio\" name=\"tag\" value=\"4\"><font color=\"#FFFFFF\">&nbsp;Nostalgic</font>";
		echo "</div>";
		echo "<br>";
		echo "<input type=\"submit\" value=\"Done!\" class=\"button\">";
		echo "</form>";
		echo "</div>";
	}
	
?>
