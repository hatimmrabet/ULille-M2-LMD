from crontab import CronTab

class planner :
    ## a cron job object

    def __init__(self):
        self.task = None
        self.hour = None
        self.minute = None
        self.day = None
        self.month = None
        self.day_of_week = None
        self.cron = CronTab()

    def Task(self, task):
        print('task: %s' % task)
        self.task = task
        return self

    def Hour(self, hour):
        print('hour: %s' % hour)
        self.hour = hour
        return self
    
    def Minute(self, minute):
        print('minute: %s' % minute)
        self.minute = minute
        return self
    
    def Day(self, day):
        print('day: %s' % day)
        self.day = day
        return self
    
    def Month(self, month):
        print('month: %s' % month)
        self.month = month
        return self
    
    def Day_of_week(self, day_of_week):
        print('day_of_week: %s' % day_of_week)
        self.day_of_week = day_of_week
        return self

    def __str__(self):
        return '%s %s %s %s %s %s' % (self.minute, self.hour, self.day, self.month, self.day_of_week, self.task)

    

def main():
    p = planner()
    print("test")
    # 15,45 0-6 */2 * * /usr/local/bin/tache-reguliere.sh
    p.Task('/usr/local/bin/tache-reguliere.sh').Minute('15,45').Hour('0-6').Day('*/2')
    
    print("affichage de p")
    print(p)

if __name__ == "__main__":
    main()