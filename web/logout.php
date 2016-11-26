<?php

	$username = $_COOKIE['username'];

	if(!file_exists("/usr/local/hadoop/bin/$username/new_info.txt")) {
		echo "12";
	}
	else {
		echo "123";
		shell_exec("/usr/local/hadoop/bin/$username/logout.sh");
	}

	setcookie("username","");
	echo "<meta http-equiv=\"refresh\" content=\"0; url=index.html\">";
	
?>
