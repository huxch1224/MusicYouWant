<?php

	if(!isset($_POST['submit'])) {
		exit('failed');
	}

	$username = htmlspecialchars($_POST['username']);
	$password = $_POST['password'];

	if($username == "" || $password == "") {
		echo "<script>alert('check input!'); history.go(-1);</script>";
	}
	else {
		$db_server = "eel6761.csqklbqdis6c.us-east-1.rds.amazonaws.com";
		$db_user = "eel6761_oracle";
		$db_pass = "huxc901224";
		$db_sid = "ORCL";

		$conn=OCILogon($db_user,$db_pass,"eel6761.csqklbqdis6c.us-east-1.rds.amazonaws.com:1521/ORCL"); 

		$sql ="select user_name from user_info where user_name='$username' and user_password='$password'";
		$result = OCI_Parse($conn,$sql);
		$s=oci_execute($result,OCI_DEFAULT);
		$r=oci_fetch_row($result);

		if($s&&$r>0) {
			$output = shell_exec("/usr/local/hadoop/bin/$username/login.sh");
               		//echo "$output<br>";
			echo "successed</br> <a href='home_check_l.php' target='change'>go to your home page</a>";
			setcookie("username",'');
			setcookie("username",$username,time()+3600);

			$sql_user_id ="select user_id from user_info where user_name='$username'";
			$result_user_id = OCI_Parse($conn,$sql_user_id);
			$s_user_id=oci_execute($result_user_id,OCI_DEFAULT);
			$r_user_id=oci_fetch_row($result_user_id);
			setcookie("uid",$r_user_id[0],OCI_DEFAULT);
		}	
		else if($s&&(!$r)) {
			echo "<script>alert('$username does not exist or wrong password!'); history.go(-1);</script>";
		}
		else {
			echo "<script>alert('connection failed!'); history.go(-1);</script>";
		}
	}
	
?>
