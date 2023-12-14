# function.pl
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

my $receive = <STDIN>;
chomp($receive);
my $response = '<FUNCTION>';

my @request = split('/', $receive);
my $option = $request[0];

if($option eq 'FUNCTION'){
	
	foreach $func ($db->ents('function')) {
		# select all functions
		if ($func->kindname ne 'Function') {
			next;
		}
		$response = $response.$func->longname().';';
		
	}
	
	print $response.'</FUNCTION>'."\n";
	exit;
}
