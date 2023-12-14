# macro.pl
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
my $response = '';

my @request = split('/', $receive);
my $option = $request[0];

if($option eq 'MACRO') {
    foreach $mac($db->ents('macro')) {
        $response = $response.'<MACRO>';
        # 같은 결과가 나옴.
        # $response = $response.$mac->type();
        # $response = $response.$mac->value();
        $response = $response.$mac->longname();
        $response = $response.'</MACRO>';
    }

    # foreach $mac($db->ents('macro functional')) {  
    #     $response = $response.'<MACFUNCTION>';
    #     $response = $response.$mac->longname().':';
    #     $response = $response.$mac->parameters();
    #     $response = $response.'</MACFUNCTION>';
    # }

    print($response);

    exit;
}