import random
from datetime import timedelta, datetime

def establishTable(conn):
    qs = ('drop table flights')
    executeSQL(conn, qs)
    conn.commit()

    qs = (
        '''Create table flights
          (id INT,
           src_id INT,
           dest_id INT,
           dt DATETIME,
           price DECIMAL(10,2),
           avail_seats VARCHAR(400),
           bag_price DECIMAL(8,2),
           seat_price DECIMAL(8,2),
           miles INT);'''
    )
    executeSQL(conn, qs)

def generateEntries(conn):
    rows = range(1,20)
    columns = ['A', 'B', 'C', 'D', 'E', 'F']
    months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'June', 'July', 'Aug', 'Sept',
              'Oct', 'Nov', 'Dec']
    seat_list = []
    for r in rows:
        for c in columns:
            this_seat = str(r)+c
            seat_list.append(this_seat)
    seat_list = ','.join(seat_list)

    qs = ("Select id from airports;")
    qr = executeSQL(conn, qs)
    airport_ids = []
    for row in qr:
        airport_ids.append(row[0])


    for i in range(1,100000):
        print (i)
        bag = float(random.randint(1500, 10000))/100
        seat = float(random.randint(1500,10000))/100
        miles = random.randint(100, 5000)
        src_id = random.choice(airport_ids)
        dest_id = random.choice(airport_ids)
        d1 = datetime.strptime('11/01/2015 01:30 PM', '%m/%d/%Y %I:%M %p')
        d2 = datetime.strptime('11/01/2016 04:50 AM', '%m/%d/%Y %I:%M %p')
        dt = random_date(d1, d2)
        price = float(random.randint(10000, 120000))/100
        while dest_id == src_id:
            dest_id = random.choice(airport_ids)
        qs = ('insert into flights '
              '(id, src_id, dest_id, dt, price, avail_seats, bag_price, seat_price, miles) '
              'values '
              '('+str(i)+', '+str(src_id)+', '+str(dest_id)+', "'+str(dt)+'", '
                                                                          ' '+'{:.2f}'.format(price)+', "'+seat_list+'", '+str(bag)+', '+str(seat)+', '+str(miles)+');')
        executeSQL(conn, qs)
    conn.commit()

    qs = ('select * from flights;')
    executeSQL(conn,qs)

def executeSQL(conn, query_string):
    try:
        cursor = conn.cursor()
        cursor.execute(query_string)
    except Exception as e:
        print "Error: "+str(e)

    return cursor

def random_date(start, end):
    """
    This function will return a random datetime between two datetime
    objects.
    """
    delta = end - start
    int_delta = (delta.days * 24 * 60 * 60) + delta.seconds
    random_second = random.randrange(int_delta)
    return start + timedelta(seconds=random_second)
