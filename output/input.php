<?php
$file = "data".".csv"; //filename

if (!file_exists($file)) {   
$fp = fopen($file, 'w');
fputcsv($fp, array_keys($_POST),"|");//write headers (key of the $_POST array (id,username,password,etc)
}
else
{
$fp = fopen($file, 'a');
}
fputcsv($fp, $_POST,"|");
fclose($fp);

$empfaenger = "gigabit@bra.nrw.de";
$betreff = "Formulardaten";
$from = "From: Automailer <automailer@bezreg-arnsberg.nrw.de>\r\n";
$from .= "Reply-To: noreply@bra.nrw.de\r\n";
$from .= "Content-Type: text/plain\r\n";
$text = "";
foreach ($_POST as $key => $value)
    $text .= htmlspecialchars($value)."|";
 
//mail($empfaenger, $betreff, $text, $from);

echo "OK";
?>