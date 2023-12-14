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
my $response = '';

my @request = split('/', $receive);
my $option = $request[0];

if($option eq 'FUNCTION'){
	
	foreach $func (($db->ents('function'))) {
		$response = $response.'<FUNCTION>';
		
		# return type
		$response = $response.$func->type().':';
		
		# func name
		$response = $response.$func->longname().':';
		
		# parameters
		if(defined $func->ents('Define','Parameter')){
			$first = 1;
			foreach $param ($func->ents("Define","Parameter")) {
				if(!$first){
					$response = $response.',';
				}
				$response = $response.$param->type().' '.$param->name();
				$first = 0;
			}
		}
		else{
			$response = $response.'void';
		}
		
		# start/end line
		my $sline = 0;
		my $eline = 0;
		foreach $ref ($func->refs()){
			if($ref->kindname() eq "Define" && $ref->ent()->name() eq $ref->file()->name()){
				$sline = $ref->line();
			}
			if($ref->kindname() eq "End" && $ref->ent()->name() eq $func->longname()){
				$eline = $ref->line();
			}
		}
		$response = $response.':'.$sline.':'.$eline.'</FUNCTION>';
	}

	# foreach $func (($db->ents('macro functional'))) {
	# 	$response = $response.'<FUNCTION>';
		
	# 	# return type
	# 	$response = $response.':';
		
	# 	# func name
	# 	$response = $response.$func->longname().':';
		
	# 	if($func->parameters) {
	# 		$response = $response.$func->parameters;
	# 	}
	# 	else{
	# 		$response = $response.'void';
	# 	}
		
	# 	# start/end line
	# 	my $sline = 0;
	# 	my $eline = 0;
	# 	foreach $ref ($func->refs()){
	# 		if($ref->kindname() eq "Define" && $ref->ent()->name() eq $ref->file()->name()){
	# 			$sline = $ref->line();
	# 		}
	# 		if($ref->kindname() eq "End" && $ref->ent()->name() eq $func->longname()){
	# 			$eline = $ref->line();
	# 		}
	# 	}
	# 	$response = $response.':'.$sline.':'.$eline.'</FUNCTION>';
	# }
	
	print($response);
	
	exit;
}
