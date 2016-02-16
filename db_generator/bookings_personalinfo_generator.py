def establishTable(conn):
    qs = ('drop table bookings_personalinfo;')
    executeSQL(conn, qs)
    conn.commit()

    qs = (
        '''Create table bookings_personalinfo
          (id INTEGER PRIMARY KEY   AUTOINCREMENT,
           personal_info_id INTEGER,
           booking_id INTEGER,
           FOREIGN KEY (personal_info_id) REFERENCES personal_info(id),
           FOREIGN KEY (booking_id) REFERENCES bookings(id));'''
    )
    executeSQL(conn, qs)

def generateEntries(conn):
    qs = ('insert into bookings_personalinfo '
          '(id, personal_info_id, booking_id) VALUES '
          '( 1, 1, 1);')
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
