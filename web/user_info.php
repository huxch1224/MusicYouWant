<?php

	$db_server = "eel6761.csqklbqdis6c.us-east-1.rds.amazonaws.com";
	$db_user = "eel6761_oracle";
	$db_pass = "huxc901224";
	$db_sid = "ORCL";

	$conn=OCILogon($db_user,$db_pass,"eel6761.csqklbqdis6c.us-east-1.rds.amazonaws.com:1521/ORCL");

	$sql_uid ="select user_id from user_info where user_name = :uname";
	$stmt_uid = OCI_Parse($conn,$sql_uid);
	oci_bind_by_name($stmt_uid, ":uname", $_COOKIE['username']);
	oci_execute($stmt_uid,OCI_DEFAULT);
	$r_uid = oci_fetch_row($stmt_uid);

	$sql_uname ="select user_name from user_info where user_name = :uname";
	$stmt_uname = OCI_Parse($conn,$sql_uname);
	oci_bind_by_name($stmt_uname, ":uname", $_COOKIE['username']);
	oci_execute($stmt_uname,OCI_DEFAULT);
	$r_uname = oci_fetch_row($stmt_uname);

	$sql_psw ="select user_password from user_info where user_name = :uname";
	$stmt_psw = OCI_Parse($conn,$sql_psw);
	oci_bind_by_name($stmt_psw, ":uname", $_COOKIE['username']);
	oci_execute($stmt_psw,OCI_DEFAULT);
	$r_psw = oci_fetch_row($stmt_psw);

	$nrows = oci_fetch_all($stmt_uid, $results);
	echo "<table border = '0'>";
	echo "<tr align=\"left\"> ";

	foreach ($results as $key => $val) {
		echo "<td><font color=\"#FFFFFF\">UserID:</font></td> ";
	}
	
	echo "<td>&emsp;&emsp;</td>";

	for($i = 0; $i < count($r_uid); $i++) {
		echo "<td><font color=\"#FFFFFF\">$r_uid[$i]</font></td> ";
	}
	echo "</tr> ";

	$nrowss = oci_fetch_all($stmt_uname, $resultss);
	echo "<tr align=\"left\"> ";

	foreach ($resultss as $key => $val) {
        	echo "<td><font color=\"#FFFFFF\">User Name:</font></td> ";
	}

	echo "<td>&emsp;&emsp;</td>";

	for($i = 0; $i < count($r_uname); $i++) {
        echo "<td><font color=\"#FFFFFF\">$r_uname[$i]</font></td> ";
	}

	echo "</tr> ";

	$nrowsss = oci_fetch_all($stmt_psw, $resultsss);
	echo "<tr align=\"left\"> ";

	foreach ($resultsss as $key => $val) {
        	echo "<td><font color=\"#FFFFFF\">Password:&emsp;&emsp;</font></td> ";
	}

	echo "<td>&emsp;&emsp;</td>";

	for($i = 0; $i < count($r_psw); $i++) {
        echo "<td><font color=\"#FFFFFF\">$r_psw[$i]</font></td> ";
	}

	echo "</tr> ";
	echo "</table> ";
	echo "</table> ";

?>
