from Model.Range import Range
from Model.Planner import Planner
from datetime import datetime, timedelta

p = Planner()
p.Task('/usr/local/bin/tache-reguliere.sh').Hours(Range(start=0,end=6,step=2)).Minutes([15,45]).Days(Range(start=1,end=15)).Days_of_week(Range(0,2))

# afficher une planification au format attendu par cron
# 15,45 0-6/2 1-15 * 0-2 /usr/local/bin/tache-reguliere.sh
print(p.affichageCron())
# au format textuel
print(p)

# déterminer si une tâche est planifiée ou pas à une date/heure/minute donnée,
mydate = datetime(2020, 12, 1, 4, 45)
print("correct => ",mydate,":",p.isPlannedAt(mydate))
# print(mydate.weekday()) # to check the day of week
wrongdate = datetime(2022, 12, 1, 4, 45)
print("wrong   => ",wrongdate,":",p.isPlannedAt(wrongdate))

# les prochaines dates où la tâche est planifiée
print(p.getPlannedDates(mydate, mydate+timedelta(days=8, hours=6, minutes=10)))
