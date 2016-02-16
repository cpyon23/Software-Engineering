import os
def establishTable(conn):
    qs = ('Drop table airports')
    executeSQL(conn, qs)
    conn.commit()

    qs = ( 'Create table airports(id INTEGER PRIMARY KEY  AUTOINCREMENT,long_name VARCHAR(30),short_name VARCHAR(5));')
    executeSQL(conn, qs)

def generateEntries(conn):
    dir_path = os.path.dirname(__file__)
    fh = open(dir_path+'/airports.txt').read()
    i = 1
    for line in fh.split('\n'):
        if line:
            line = line.split('|')
            long = line[0].replace(',','').strip()
            short = line[1].strip()
            qs = ("insert into airports(long_name, short_name) "+
                    "VALUES ('"+long+"', '"+short+"');")
            executeSQL(conn,qs)
        i+=1
    conn.commit()


def executeSQL(conn, query_string):
    try:
        cursor = conn.cursor()
        cursor.execute(query_string)
    except Exception as e:
        print "Error: "+str(e)
    return cursor
