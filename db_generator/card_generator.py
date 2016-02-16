def establishTable(conn):
    qs = ('drop table cards;')
    executeSQL(conn, qs)
    conn.commit()

    qs = (
        '''Create table cards
          (id INTEGER PRIMARY KEY   AUTOINCREMENT,
           num VARCHAR(20),
           cvv VARCHAR(3),
           company VARCHAR(15),
           name_on_card VARCHAR(30),
           expiration VARCHAR(10));'''
    )
    executeSQL(conn, qs)

def generateEntries(conn):
    qs = ('insert into cards '
          '(num, cvv, company, name_on_card, expiration) VALUES '
          '("0000123456789000", "999", "Visa", "Pork T. Twerkins", "08/2017");')
    executeSQL(conn, qs)
    conn.commit()

def executeSQL(conn, query_string):
    try:
        cursor = conn.cursor()
        cursor.execute(query_string)
    except Exception as e:
        print query_string
        print "Error: "+str(e)

    return cursor
