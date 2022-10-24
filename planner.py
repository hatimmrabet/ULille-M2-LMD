from tabnanny import check
from crontab import CronTab
from interval import interval

class planner :
    ## a cron job object

    def __init__(self):
        self.task = ""
        self.hour = []
        self.minute = []
        self.day = []
        self.month = []
        self.day_of_week = []
        self.cron = CronTab()

    def Task(self, task):
        print('task: %s' % task)
        self.task = task
        return self

    def Hour(self, hourparam):
        
        if isinstance(hourparam, list):
            for hour in hourparam:
                self.Hour(hour)
        elif isinstance(hourparam, interval):
            if hourparam.checkHour():
                self.hour.append(hourparam)
        else:
            if hourparam == '*':
                self.hour.append(hourparam)
            if hourparam<24 and hourparam>=0:
                self.hour.append(hourparam)
        return self
    
    def Minute(self, minuteparam):
        if isinstance(minuteparam, list):
            for hour in minuteparam:
                self.Hour(hour)
        elif isinstance(minuteparam, interval):
            if interval.checkMinute():
                self.minute.append(minuteparam)
        else:
            if minuteparam<60 and minuteparam>=0:
                self.minute.append(minuteparam)
        return self
    
    def Day(self, dayparam):
        if isinstance(dayparam, list):
            for day in dayparam:
                self.Day(day)
        elif isinstance(dayparam, interval):
            if dayparam.checkDay():
                self.day.append(dayparam)
        else:
            if dayparam<31 and dayparam>=0:
                self.day.append(dayparam)
        return self
    
    def Month(self, monthparam):
        if isinstance(monthparam, list):
            for month in monthparam:
                self.Month(month)
        elif isinstance(monthparam, interval):
            if monthparam.checkMonth():
                self.month.append(monthparam)
        else:
            if monthparam<12 and monthparam>=0:
                self.month.append(monthparam)
        return self
        

    def Day_of_week(self, day_of_week):
        if isinstance(day_of_week, list):
            for day in day_of_week:
                self.Day_of_week(day)
        elif isinstance(day_of_week, interval):
            if day_of_week.checkDay_of_week():
                self.day_of_week.append(day_of_week)
        else:
            if day_of_week<7 and day_of_week>=0:
                self.day_of_week.append(day_of_week)
        return self

    def __str__(self):
        if self.hour:
            for hour in self.hour:
                print('hour: %s' % hour)
        if self.minute:
            for minute in self.minute:
                print('minute: %s' % minute)
        if self.day:
            for day in self.day:
                print('day: %s' % day)
        if self.month:
            for month in self.month:
                print('month: %s' % month)
        if self.day_of_week:
            for day_of_week in self.day_of_week:
                print('day_of_week: %s' % day_of_week)
        return self.task


def main():
    p = planner()
    print("test")
    # 15,45 0-6 */2 * * /usr/local/bin/tache-reguliere.sh
    p.Task('/usr/local/bin/tache-reguliere.sh').Minute([0,6]).Hour(interval(0,6)).Day(interval(1,31))
    
    print("affichage de p")
    print(p)

if __name__ == "__main__":
    main()