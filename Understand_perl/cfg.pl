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
			
			# if ($func->kindname ne 'Function') {
			# 	next;
			# }
			
			if($func->longname() eq $funcName){
								
				@nodelist = split(';', $func->freetext('CGraph'));

				foreach $fields (@nodelist) {
					$response = $response.'<Statement><CFG>';
					
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
					
					$response = $response.'</CFG><Lexemes>';
					
					# get lexeme using code location
					# my $l = $func->lexer(1,0,1,1)->lexeme($node{sline}, $node{scol});
					
					my $l = $func->lexer(1,8,1,1)->lexeme($node{sline}, $node{scol});
					# iteratively gets lexemes until code region of the node fully explored
					if(!$l){
						$response = $response.'<Lexeme>';
						$response = $response.'<text>NULL</text>';
						$response = $response.'<tokenkind>NULL</tokenkind>';
						$response = $response.'<refkind>NULL</refkind>';
						$response = $response.'<file>NULL</file>';
						$response = $response.'<scope>NULL</scope>';
						$response = $response.'<type>NULL</type>';
						$response = $response.'</Lexeme>';
					}
					
					while (defined $l) {
						if( $l->line_end() == $node{eline} ){
							if($l->column_end() > $node{ecol}){
								last;
							}
						}

						# if($l->token() eq "Comment") {
						# 	$l = $l->next();
						# }					
						
						# text = name
						if(!ord($node{scol})){
							$response = $response.'<Lexeme><text>NULL</text>';
						}
						else{
							if($l->token() eq "Newline"){
								$response = $response.'<Lexeme><text>\n</text>';
							}
							else{
								$response = $response.'<Lexeme><text>'.$l->text().'</text>';
							}
						}
						
						# token = Whitespace, Keyword, Identifier ..
						if(!ord($node{scol})){
							$response = $response.'<tokenkind>NULL</tokenkind>';
						}
						else{
							$response = $response.'<tokenkind>'.$l->token().'</tokenkind>';
						}
						
						# kindname = Use, Define, Type ...
						if (defined $l->ref()) {
							$response = $response.'<refkind>'.$l->ref()->kindname().'</refkind>';
							$response = $response.'<file>'.$l->ref()->file()->name().'</file>';
						} 
						else{
							$response = $response.'<refkind>NULL</refkind>';
							$response = $response.'<file>NULL</file>';
						}
						
						# scope and type
						if(defined $l->ent()){
							$response = $response.'<scope>'.$l->ent()->kindname().'</scope>';
							if(defined $l->ent()->type()){
								$response = $response.'<type>'.$l->ent()->type().'</type>';
							}
							else{
								$response = $response.'<type>NULL</type>';
							}
						}
						else{
							$response = $response.'<scope>NULL</scope>';
							$response = $response.'<type>NULL</type>';
						}
						$response = $response.'</Lexeme>';
						
						$l = $l->next();
					}
				
					$response = $response.'</Lexemes></Statement>';
				}

			}
		}
		
		print $response."\n";
	}
	elsif($option eq 'FINISH'){
		exit;
	}
	
	
}