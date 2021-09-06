# Decodeur d'un texte représenté comme une suite de code ASCII
while (<>) {
  $code .= $_;
}
print $code, "\n";

@code = split(/\n/, $code);
$mail = "";
foreach $a (@code) {
  $mail .= chr($a);
} 

print $mail;
