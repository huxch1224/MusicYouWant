<?php

	if(!isset($_POST['submit'])) {
		exit('failed');
	}

	$username = htmlspecialchars($_POST["username"]);
	$password = $_POST["password"];
	$psw_confirm = $_POST["psw_confirm"];
	
	if($username == "" || $password == "" || $psw_confirm == "") {
		echo "<script>alert('check input!'); history.go(-1);</script>";
	}

	else {

		if($password == $psw_confirm) {
			$db_user = "eel6761_oracle";
			$db_pass = "huxc901224";
			
			$conn=OCILogon($db_user,$db_pass,"eel6761.csqklbqdis6c.us-east-1.rds.amazonaws.com:1521/ORCL"); 
			
			$sql ="select * from user_info where user_name = '$username'";
			$statement = OCI_Parse($conn,$sql);
			oci_execute($statement,OCI_DEFAULT);
			$r=oci_fetch_row($statement);
			
			if($r != FALSE) {
				echo "$username already exist!";
			}
			else {
				echo 'connection succeed!</br>';
				$id="select max(user_id) from user_info";
				$testid = OCIParse($conn,$id);
				$addid = oci_execute($testid,OCI_COMMIT_ON_SUCCESS);
				$addd = oci_fetch_row($testid);
				$addd[0]=$addd[0]+1;
				$sql_insert = "insert into user_info (user_name,user_password,user_id) values (:username,:password,:addd)";
				$res_insert = OCIParse($conn,$sql_insert);
				oci_bind_by_name($res_insert, ":username", $username);
				oci_bind_by_name($res_insert, ":password", $password);
				oci_bind_by_name($res_insert, ":addd", $addd[0]);
				$s_insert=oci_execute($res_insert,OCI_COMMIT_ON_SUCCESS);
				
				if($s_insert) {
					setcookie("username",$username,time()+3600);
					mkdir("/usr/local/hadoop/bin/$username");
					copy("/usr/local/hadoop/bin/scripts/login.sh", "/usr/local/hadoop/bin/$username/login.sh");
					chmod("/usr/local/hadoop/bin/$username/login.sh", 0777);
					copy("/usr/local/hadoop/bin/scripts/reg.sh", "/usr/local/hadoop/bin/$username/reg.sh");
					chmod("/usr/local/hadoop/bin/$username/reg.sh", 0777);
					copy("/usr/local/hadoop/bin/scripts/logout.sh", "/usr/local/hadoop/bin/$username/logout.sh");
					chmod("/usr/local/hadoop/bin/$username/logout.sh", 0777);
					$file = fopen("/usr/local/hadoop/bin/$username/user_info.txt", 'w');
					$write = $username."\t"."10"." "."0"." "."0"." "."0"." "."0"."\n";
					fwrite($file, $write);
					fclose($file);
					echo 'registration succeed</br>';
					shell_exec("/usr/local/hadoop/bin/$username/reg.sh");
					echo "<a href='home_check_r.php' target='change'>go to your home page</a>";
				}
				else {
					OCIFreeStatement($res_insert);
					OCILogoff($conn);
				}
       			 }
		}
		else {
			echo "<script>alert('please check your password!');";
			echo "history.go(-1);";
			echo "</script>";
		}
	}
	
?>
