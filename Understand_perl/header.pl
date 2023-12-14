# test.pl
use Understand;
use feature qw(say);
use Cwd qw(abs_path);

# Open Understand database
my $udbPath = <STDIN>;
chomp($udbPath);
($db, $status) = Understand::open($udbPath);
if($status){
	print $status;
	exit;
}
die 'Error status: ',$status,"\n" if $status;

my $response = '';

foreach $header ($db->ents('header')) {
$response = $response.'<HEADER>';
$response = $response.$header->name();
$response = $response.'</HEADER>';
}

# foreach $glb ($db->ents('Global Object')) {
#     $response = $response.'<GLOBAL>';
#     # 같은 결과가 나옴.
#     # $response = $response.$mac->type();
#     # $response = $response.$mac->value();
#     $response = $response.$glb->type();
#     $response = $response.':';
#     $response = $response.$glb->name();
#     $response = $response.':';
#     $response = $response.$glb->value();
#     $response = $response.'</GLOBAL>';
# }

# foreach $mac ($db->ents('Macro')) {
#     $response = $response.'<MACRO>';
#     $response = $response.$mac->name();
#     $response = $response.':';
#     $response = $response.$mac->parameters();
#     $response = $response.':';
#     $response = $response.$mac->type();
#     $response = $response.'</MACRO>';
# }

# foreach $mac($db->ents('macro functional')) {  
#     $response = $response.'<MACFUNCTION>';
#     $response = $response.$mac->longname().':';
#     $response = $response.$mac->parameters();
#     $response = $response.'</MACFUNCTION>';
# }

print($response."\n");
