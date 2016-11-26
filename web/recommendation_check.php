<?php
	//setcookie('username','1');//localhost
	$check = $_COOKIE['username'];

	if($check == "") {
		echo "<script>alert('please login!');";
		echo "</script>";
		echo "<meta http-equiv=\"refresh\" content=\"0; url=index.html\">";
	}
	else {
		//echo "<script>alert('coming soon!'); history.go(-1);</script>";
		echo "<meta http-equiv=\"refresh\" content=\"0; url=recommendation.html\">";
	}
	
?>