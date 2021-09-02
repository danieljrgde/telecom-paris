<?php
    $chaine = gethostbyname($_SERVER['REMOTE_ADDR']) ;
    $chaine .=  " - " . $_GET['phrase'] . "\n";
    $fp = fopen("chatlog.txt", 'a');
    if ($fp == false) {
        echo "Permission error on chatlog.txt: do 'chmod a+w chatlog.txt'";
    } else {
        fwrite($fp, $chaine);
        fclose($fp);
        echo "Success";
    }
?>