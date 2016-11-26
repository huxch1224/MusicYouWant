<?php
	//setcookie('username','1');//localhost
	$check = $_COOKIE['username'];

	if($check == "") {
		echo "<meta http-equiv=\"refresh\" content=\"0; url=index.html\">";
	}
	else {
		echo "<meta http-equiv=\"refresh\" content=\"0; url=home.html\">";
	}
	
?>