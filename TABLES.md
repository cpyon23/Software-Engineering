###################################################################

## FLIGHTS TABLE ##

sqlite> select * from flights where id < 2;
1|148|70|2016-01-25 18:27:31|107.47|1A,1B,...,19F|78.51|17.75|1721

sqlite> PRAGMA table_info(flights);
0|id|INT|0||0
1|src_id|INT|0||0
2|dest_id|INT|0||0
3|dt|DATETIME|0||0
4|price|DECIMAL(10,2)|0||0
5|avail_seats|VARCHAR(400)|0||0
6|bag_price|DECIMAL(8,2)|0||0
7|seat_price|DECIMAL(8,2)|0||0
8|miles|INT|0||0

###################################################################

## AIRPORTS TABLE ##
sqlite> select * from airports;
1|Birmingham International Airport|BHM

sqlite> PRAGMA table_info(airports);
0|id|INT|0||0
1|long_name|VARCHAR(30)|0||0
2|short_name|VARCHAR(5)|0||0

###################################################################

## ACCOUNTS TABLE ##
sqlite> select * from accounts;
1|dee|fault|demail@gmail.com|default|default|0

sqlite> PRAGMA table_info(accounts);
0|id|INT|0||0
1|fname|VARCHAR(10)|0||0
2|lname|VARCHAR(15)|0||0
3|email|VARCHAR(25)|0||0
4|username|VARCHAR(20)|0||0
5|password|VARCHAR(25)|0||0
6|reward_miles|INT|0||0

###################################################################

## CARDS TABLE ##
sqlite> select * from cards;
1|0000123456789000|999|Visa|Pork T. Twerkins|08/2017

sqlite> PRAGMA table_info(cards);
0|id|INT|0||0
1|num|VARCHAR(20)|0||0
2|cvv|VARCHAR(3)|0||0
3|company|VARCHAR(15)|0||0
4|name_on_card|VARCHAR(30)|0||0
5|expiration|VARCHAR(10)|0||0

###################################################################

## ACCOUNTS_CARDS FK TABLE

sqlite> PRAGMA table_info(accounts_cards);
0|id|INT|0||0
1|account_id|INT|0||0
2|card_id|INT|0||0


sqlite> select * from accounts_cards;
1|1|1
