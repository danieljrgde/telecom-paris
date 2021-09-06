# -*- Perl -*- 
#
# Segmente virtuellement un mail à partir du meilleur chemin de Viterbi
# Usage: perl segment.pl mailfn segmfn
#   avec mailfn le nom du fichier de mail
#        segmfn le nom du fichier produit par viterbi (une ligne par état)

$mailfn = $ARGV[0];
$pathfn = $ARGV[1];

open (MAIL, "perl codeur.pl $mailfn |") or die "can' open $mailfn";
open (PATH, "$pathfn") or die "can' open $pathfn";
#print <PATH>;
# Segmente un mail sur la base de la sortie du HMM
while (<MAIL>) {
  $mail .= $_;
}
close(MAIL);

while (<PATH>) {
  #chop;
  #$path .= int($_);
  $path .= $_;
  print $path;
}
close(PATH);

@mail = split(/\n/, $mail);
@path = split(//,   $path);

print $path, "\n";
die "Mismatch between input ($#mail) and segmentation ($#path)" unless ($#mail == $#path);

$segm = "";
foreach $i (0 .. $#mail) {
  if ($path[$i-1] == 1 && $path[$i] == 2) {
    $segm .= "\n========================== coupez ici ==========================\n";
  }
  $segm .= chr($mail[$i]);
}

print $segm;
