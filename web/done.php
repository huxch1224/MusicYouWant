<?php
	
	$username = $_COOKIE['username'];
	$music = $_GET['music'];
	$tag1 = htmlspecialchars($_POST['tagg']);
	$tag2 = htmlspecialchars($_POST['tag']);

	$f1=fopen("/usr/local/hadoop/bin/$username/recommendationlist.txt",'r');
	$f2=fopen("/usr/local/hadoop/bin/$username/temp.txt",'w');

	while(!feof($f1)) {	
		$line = fgets($f1);
		$split = explode(" ", $line);

		if($music != $split[0]) {
			fputs($f2, $line);
		}
	}
	
	fclose($f1);
	fclose($f2);
	unlink("/usr/local/hadoop/bin/$username/recommendationlist.txt");

	$f6=fopen("/usr/local/hadoop/bin/$username/temp.txt",'r');
        $f7=fopen("/usr/local/hadoop/bin/$username/recommendationlist.txt",'w');

        while(!feof($f6)) {
                $line = fgets($f6);
                $split = explode(" ", $line);

                if($music != $split[0]) {
                        fputs($f7, $line);
                }
        }

        fclose($f6);
        fclose($f7);
        unlink("/usr/local/hadoop/bin/$username/temp.txt");
	
	$f3 = fopen("/usr/local/hadoop/bin/songlist.txt", 'r');
	$f4 = fopen("/usr/local/hadoop/bin/$username/user_info.txt", 'a');
	$f5 = fopen("/usr/local/hadoop/bin/$username/new_info.txt", 'a');
	$f8 = fopen("/usr/local/hadoop/bin/$username/likelist.txt", 'a');

	while(!feof($f3)) {
		$line = fgets($f3);
		$split = explode(" ", $line);
		
		if($music == $split[1]) {
			$writeA = "*"."\t".$split[0]." ".$tag1."\n";
			$temp = "0"." "."0"." "."0"." "."0"." "."0";
			$s = explode(" ", $temp);
			$s[$tag2] = 10;
			$writeB = "*"." ".$split[0]." ".$s[0]." ".$s[1]." ".$s[2]." ".$s[3]." ".$s[4]."\n";
			$writeC = $split[1]." ".$split[2]." ".$split[3];
			fwrite($f4, $writeA);
			fwrite($f5, $writeB);
			fwrite($f8, $writeC);
			break;
		}
	}

	fclose($f3);
	fclose($f4);
	fclose($f5);
	fclose($f8);

	$file = fopen("/usr/local/hadoop/bin/$username/recommendationlist.txt", "r");

	echo "<table>";

	for($i = 0; $i < 4; $i++) {
		$check = feof($file);

		if($check == 1) {
			break;
		}
		
		$line = fgets($file);
		$split = explode(" ", $line);
		$replaceA = str_replace("_", " ", "$split[0]");
		$replaceB = str_replace("_", " ", "$split[1]");
		$replaceC = str_replace("_", " ", "$split[2]");
		echo "<tr>";
		echo "<td><a href='recommendation_music.php?music_name=$replaceA'><font color=white>$replaceA</font></a></td>";
		echo "<td><font color=\"#FFFFFF\">$replaceB</font></td>";
		echo "<td><font color=\"#FFFFFF\">$replaceC</font></td>";
		echo "</tr>";
	}

	echo "</table>";
	
	fclose($file);
	
?>
