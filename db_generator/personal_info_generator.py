def establishTable(conn):
    qs = ('drop table personal_info;')
    executeSQL(conn, qs)
    conn.commit()

    qs = (
        '''Create table personal_info
          (id INTEGER PRIMARY KEY   AUTOINCREMENT,
           fullname VARCHAR(40),
           DOB VARCHAR(10),
           gender VARCHAR(8),
           address1 VARCHAR(20),
           address2 VARCHAR(20),
           city VARCHAR(20),
           state VARCHAR(20),
           zipcode VARCHAR(12),
           phone VARCHAR(15),
           email VARCHAR(30));'''
    )
    executeSQL(conn, qs)

def generateEntries(conn):
    qs = ('insert into personal_info '
          '(fullname, DOB, gender, address1, address2, city, state, zipcode, phone, email) VALUES '
          '("Porky P Twerkins", "05/15/1993", "female", "141 Piedmont Avenue",'
          ' "Apt. 2252", "Atlanta", "Georgia", "30303", "404-710-9222", "porkyp@gmail.com");')
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
