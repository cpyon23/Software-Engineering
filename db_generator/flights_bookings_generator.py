def establishTable(conn):
    qs = ('drop table flights_bookings;')
    executeSQL(conn, qs)
    conn.commit()

    qs = (
        '''Create table flights_bookings
          (id INTEGER PRIMARY KEY   AUTOINCREMENT,
           flight_id INTEGER,
           booking_id INTEGER,
           FOREIGN KEY (flight_id) REFERENCES flights(id),
           FOREIGN KEY (booking_id) REFERENCES bookings(id));'''
    )
    executeSQL(conn, qs)

def generateEntries(conn):
    qs = ('insert into flights_bookings '
          '(flight_id, booking_id) VALUES '
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
