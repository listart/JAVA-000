grammar SQL;
dql : 'select' COLUMNS 'from' TABLE ;
COLUMNS : '*' ;
TABLE : [a-zA-Z][a-zA-Z1-9]+ ;
WS : [ \t\r\n]+ -> skip ;
