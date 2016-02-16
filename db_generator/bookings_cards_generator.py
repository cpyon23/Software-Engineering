def establishTable(conn):
    qs = ('drop table bookings_cards;')
    executeSQL(conn, qs)
    conn.commit()

    qs = (
        '''Create table bookings_cards
          (id INTEGER PRIMARY KEY   AUTOINCREMENT,
           booking_id INTEGER,
           card_id INTEGER,
           FOREIGN KEY (booking_id) REFERENCES bookings(id),
           FOREIGN KEY (card_id) REFERENCES cards(id));'''
    )
    executeSQL(conn, qs)

def generateEntries(conn):
    qs = ('insert into bookings_cards '
          '(booking_id, card_id) VALUES '
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
