def establishTable(conn):
    qs = ('drop table bookings;')
    executeSQL(conn, qs)
    conn.commit()

    qs = (
        '''Create table bookings
          (id INTEGER PRIMARY KEY   AUTOINCREMENT,
           seat_numbers VARCHAR(30),
           bags INT,
           ancillary_pkg_id INT,
           created DATETIME DEFAULT CURRENT_TIMESTAMP);'''
    )
    executeSQL(conn, qs)

def generateEntries(conn):
    qs = ('insert into bookings '
          '(seat_numbers, bags, ancillary_pkg_id) VALUES '
          '("1A,1B", 2, 1);')
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
