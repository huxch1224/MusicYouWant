<?php
	//setcookie('username','1');//localhost
	$check = $_COOKIE['username'];
	
        $ff1 = fopen("/usr/local/hadoop/bin/$check/temp.txt", 'r');
        $ff3 = fopen("/usr/local/hadoop/bin/$check/recommendationlist.txt", 'w');
        
        while(!feof($ff1)) {
		$line1 = fgets($ff1);
		$split1 = explode("\t", $line1);
                $ff2 = fopen("/usr/local/hadoop/bin/songlist.txt", 'r');
        
               	while(!feof($ff2)) {
			$line2 = fgets($ff2);
			$split2 = explode(" ", $line2);
                        $cc = $split2[0]."\n";
			
			if($split1[1] == null || $cc == null) {
				break;
			}

                        if($split1[1] == $cc) {
				fwrite($ff3, $split2[1]." ".$split2[2]." ".$split2[3]);
                                break;
                        }
                
		}
		fclose($ff2);
	}

	fclose($ff1);
        fclose($ff3);
	
	unlink("/usr/local/hadoop/bin/$check/temp.txt");

	if($check == "") {
		echo "<meta http-equiv=\"refresh\" content=\"0; url=index.html\">";
	}
	else {
		echo "<meta http-equiv=\"refresh\" content=\"0; url=home.html\">";
	}
	
?>
