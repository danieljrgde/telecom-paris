# -*- Perl -*-

# Recode un texte en un vecteur de codes ASCII
while (<>) {
  $mail .= $_;
}

@mail = split(//, $mail);
foreach $c (@mail) {
  print ord($c), "\n";
}
