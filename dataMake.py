import re
fname = ["Zach", "John", "Steve", "Jake", "Samantha", "Balthazar", "Azmodius", "Lucifer","Mike", "Jane"]
lname = ["Smith", "Johnson", "Versi", "Habsburg", "Dinkle", "Square", "Star", "Cheeks", "Krabs", "Tentacles"]
users = []
data = open("Data.sql", "w")
data.write("--profile inserts\n")
for i in fname:
	for j in lname:
		users.append(i+j)
		data.write("INSERT into profile VALUES('"+i+j+"','"+i+" "+j+"','admin',TO_DATE('01-JAN-91'),NULL);\n")
		
data.write("\n")

for i in range(1, 100):
	data.write("INSERT into friends VALUES('"+users[0]+"','"+users[i]+"', TO_DATE('10-NOV-17'),'Hi');\n")
for i in range(2, 100):
	data.write("INSERT into friends VALUES('"+users[1]+"','"+users[i]+"', TO_DATE('10-NOV-17'),'Hi');\n")
for i in range(3, 6):
	data.write("INSERT into friends VALUES('"+users[2]+"','"+users[i]+"', TO_DATE('10-NOV-17'),'Hi');\n")


data.write("\n")
for i in range(0, 10):
	data.write("INSERT into groups VALUES('"+str(i)+"','"+str(i)+"','"+str(i)+"');\n")
	
data.write("\n")
for i in range(0,300):
	data.write("INSERT into messages VALUES('"+str(i)+"','"+users[0]+"','We are the knights who say ni','"+users[1]+"',NULL,TO_DATE('10-NOV-17'));\n")
data.close()