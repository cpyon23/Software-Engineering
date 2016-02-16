import random
from datetime import timedelta, datetime

def establishTable(conn):
    qs = ('drop table accounts')
    executeSQL(conn, qs)
    conn.commit()

    qs = (
        '''Create table accounts
          (id INTEGER PRIMARY KEY   AUTOINCREMENT,
           fname VARCHAR(10),
           lname VARCHAR(15),
           email VARCHAR(25),
           username VARCHAR(20),
           password VARCHAR(25),
           reward_miles INT,
           created DATETIME DEFAULT CURRENT_TIMESTAMP );'''
    )
    executeSQL(conn, qs)

def generateEntries(conn):
    qs = ('insert into accounts '
          '(fname, lname, email, username, password, reward_miles) VALUES '
          '("guest", "guest", "guest@travlr.com", "admin", "admin", 0);')
    executeSQL(conn, qs);
    conn.commit();
    qs = ('insert into accounts '
          '(fname, lname, email, username, password, reward_miles) VALUES '
          '("dee", "fault", "demail@gmail.com", "default", "default", 0);')
    executeSQL(conn, qs)
    conn.commit()

def executeSQL(conn, query_string):
    try:
        cursor = conn.cursor()
        cursor.execute(query_string)
    except Exception as e:
        print "Error: "+str(e)

    return cursor
