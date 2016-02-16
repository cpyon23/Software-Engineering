def establishTable(conn):
    qs = ('drop table accounts_cards;')
    executeSQL(conn, qs)
    conn.commit()

    qs = (
        '''Create table accounts_cards
          (id INTEGER PRIMARY KEY   AUTOINCREMENT,
           account_id INTEGER,
           card_id INTEGER,
           FOREIGN KEY (account_id) REFERENCES accounts(id),
           FOREIGN KEY (card_id) REFERENCES cards(id));'''
    )
    executeSQL(conn, qs)

def generateEntries(conn):
    qs = ('insert into accounts_cards '
          '(account_id, card_id) VALUES '
          '(1, 1);')
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
