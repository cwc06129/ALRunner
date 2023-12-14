# cfg.pl
use Understand;
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

while(1) {
	my $receive = <STDIN>;
	chomp($receive);
	my $response = '';
	
	my @request = split('/', $receive);
	my $option = $request[0];
	my $funcName = $request[1];
	
	if($option eq 'CFG'){
		foreach $func ($db->ents('function')) {
			
			if ($func->kindname ne 'Function') {
				next;
			}
			
			if($func->longname() eq $funcName){
				
				@nodelist = split(';', $func->freetext('CGraph'));

				foreach $fields (@nodelist) {
					$response = $response.'<CFG>CFG: ';
					
					my %node = {}; # perl dictionary
					
					($node{kind}, $node{sline}, $node{scol}, $node{eline}, $node{ecol},
					$node{endstr}, my @children) = split(",", $fields);
					$node{children} = @children;
					
					$response = $response.$node{kind}.':';
					if(!ord($node{sline})){
						$response = $response.'-1'.':';
					}
					else{
						$response = $response.$node{sline}.':';
					}
					
					if(!ord($node{scol})){
						$response = $response.'-1'.':';
					}
					else{
						$response = $response.$node{scol}.':';
					}
					
					if(!ord($node{eline})){
						$response = $response.'-1'.':';
					}
					else{
						$response = $response.$node{eline}.':';
					}
					
					if(!ord($node{ecol})){
						$response = $response.'-1'.':';
					}
					else{
						$response = $response.$node{ecol}.':';
					}
					
					if(!ord($node{endstr})){
						$response = $response.'-1'.':';
					}
					else{
						$response = $response.$node{endstr}.':';
					}
					
					my $child = (join ":", @children);
					if(!ord($child)){
						$response = $response.'-1';
					}
					else{
						$response = $response.$child;
					}
					
					$response = $response.'LEXEME: ';
					
					# get lexeme using code location
					my $l = $func->lexer()->lexeme($node{sline}, $node{scol});
					# iteratively gets lexemes until code region of the node fully explored
					if(!$l){
						$response = $response.'NULL$NULL$NULL@';
					}
					
					while (defined $l) {
						if( $l->line_end() == $node{eline} ){
							if($l->column_end() > $node{ecol}){
								last;
							}
						}
						
						my $lxm;
						if( $l->token() eq 'Newline' ){
							$lxm = '\n$'.'NULL$'.$l->token.'@';
							$response = $response.$lxm;
						}
						else{
							if(!ord($node{scol})){
								$response = $response.'NULL'.'$';
							}
							else{
								$response = $response.$l->text().'$';
							}
							
							
							# kindname = Use, Define, Type ...
							if ( defined $l->ref() ) {
								$response = $response.$l->ref()->kindname().'$';				
							}
							else{
								$response = $response.'NULL'.'$';
							}
							# token = Whitespace, Keyword, Identifier ...
							
							if(!ord($node{scol})){
								$response = $response.'NULL'.'@';
							}
							else{
								$response = $response.$l->token().'@';
							}

						}
						
						
						$l = $l->next();
					}
				
					$response = $response.'</CFG>'
				}

			}
		}
		
		print $response."\n";
	}
	elsif($option eq 'FINISH'){
		exit;
	}
	
	
}