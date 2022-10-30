from tabnanny import check
from crontab import CronTab


class planner :
    ## a cron job object

    def __init__(self):
        self.task = ""
        self.hour = None
        self.minute = None
        self.day = None
        self.month = None
        self.day_of_week = None
        self.cron = CronTab()

    def Task(self, task):
        self.task = task
        return self

    def Hour(self, hourparam):
        if isinstance(hourparam, list) or isinstance(hourparam, tuple):
            if checkHour(hourparam):
                self.hour=hourparam
        else:
            if hourparam == '*':
                self.hour=hourparam
            elif hourparam<24 and hourparam>=0:
                self.hour=hourparam
        return self
    
    def Minute(self, minuteparam):
        if isinstance(minuteparam, list) or isinstance(minuteparam, tuple):
            if checkMinute(minuteparam):
                self.minute=minuteparam
        else:
            if minuteparam == '*':
                self.minute=minuteparam
            elif minuteparam<=60 and minuteparam>0:
                self.minute=minuteparam
        return self
    
    def Day(self, dayparam):
        print(isinstance(dayparam, tuple))
        if isinstance(dayparam, list) or isinstance(dayparam, tuple):
            if checkDay(dayparam):
                self.day=dayparam
                print(self.day)
        else:
            if dayparam == '*':
                self.day=dayparam
            elif dayparam<=31 and dayparam>0:
                self.day=dayparam
        return self
    
    def Month(self, monthparam):
        if isinstance(monthparam, list) or isinstance(monthparam, tuple):
            if checkMonth(monthparam):
                self.month=monthparam
        else:
            if monthparam == '*':
                self.month=monthparam
            elif monthparam<=12 and monthparam>0:
                self.month=monthparam
        return self
        

    def Day_of_week(self, day_of_week):
        if isinstance(day_of_week, list) or isinstance(day_of_week, tuple):
            if checkDay_of_week(day_of_week):
                
                self.day_of_week=day_of_week
        else:
            if day_of_week == '*':
                self.day_of_week=day_of_week
            elif day_of_week<=7 and day_of_week>0:
                self.day_of_week=day_of_week
        return self

    def __str__(self):
        hour_string = ""
        minute_string = ""
        day_string = ""
        month_string = ""
        day_of_week_string = ""
        if self.minute:
            
            minute_string=""
            if isinstance(self.minute,tuple):
                minute_string+= "every minute from "+ str(self.minute[0])+" to "+str(self.minute[1])
            elif isinstance(self.minute,list):
                minute_string+= "at minutes "
                for i in self.minute:
                    minute_string+=str(i)+","
                minute_string=minute_string[:-1]
            elif isinstance(self.minute,int):
                minute_string+= "at minute "+ str(self.minute)
            else:
                minute_string="every minute"

        if self.hour:
        
            hour_string=""
            if isinstance(self.hour,tuple):
                
                hour_string+= "every hour from "+ str(self.hour[0]) + " to "+ str(self.hour[1])
            elif isinstance(self.hour,list):
                hour_string+= "at hours "
                for i in self.hour:
                    hour_string+=str(i)+","
                hour_string=hour_string[:-1]
            elif isinstance(self.hour,int):
               
                hour_string+= "at hour "+ str(self.hour)
            else:
                hour_string="every hour"

      
        if self.day:
            day_string=""
            
            if isinstance(self.day,tuple):
                day_string+= "every day from "+ str(self.day[0]) + " to "+ str(self.day[1])
            elif isinstance(self.day,list):
                day_string+= "at days "
                for i in self.day:
                    day_string+=str(i)+","
                day_string=day_string[:-1]
            elif isinstance(self.day,int):
                day_string+= "at day "+ str(self.day)
            else:
                day_string="every day"

        if self.month:
            month_string=""
            if isinstance(self.month,tuple):
                month_string+= "every month from "+ str(self.month[0]) + " to "+ str(self.month[1])
            elif isinstance(self.month,list):
                month_string+= "at months "
                for i in self.month:
                    month_string+=str(i)+","
                month_string=month_string[:-1]
            elif isinstance(self.month,int):
                month_string+= "at month "+ str(self.month)
            else:
                month_string="every month"

        if self.day_of_week:
            day_of_week_string=""
            
            if isinstance(self.day_of_week,tuple):
                
                day_of_week_string+= "every day_of_week from "+ str(self.day_of_week[0]) + " to "+ str(self.day_of_week[1])
            elif isinstance(self.day_of_week,list):
                day_of_week_string+= "at days_of_week "
                for i in self.day_of_week:
                    day_of_week_string+=str(i)+","
                day_of_week_string=day_of_week_string[:-1]
            elif isinstance(self.day_of_week,int):
                day_of_week_string+= "at day_of_week "+ str(self.day_of_week)
            else:
                day_of_week_string="every day_of_week"
        return "Task: %s %s %s %s %s %s" % (self.task, hour_string, minute_string, day_string, month_string, day_of_week_string)

def checkHour(hourparam):
    if hourparam[0]<hourparam[1]:
        if hourparam[0]>=0 and hourparam[1]<24:
            return True
    else:
        return False

def checkMinute(minuteparam):
    if minuteparam[0]<minuteparam[1]:
        if minuteparam[0]>=0 and minuteparam[1]<60:
            return True
    else:
        return False

def checkDay(dayparam):
    if dayparam[0]<dayparam[1]:
        if dayparam[0]>0 and dayparam[1]<=31:
            return True
    else:
        return False

def checkMonth(monthparam):
    if monthparam[0]<monthparam[1]:
        if monthparam[0]>=0 and monthparam[1]<=12:
            return True
    else:
        return False

def checkDay_of_week(day_of_weekparam):
    if day_of_weekparam[0]<day_of_weekparam[1]:
        if day_of_weekparam[0]>=0 and day_of_weekparam[1]<7:
            return True
    else:
        return False
    

def main():
    p = planner()
   
    # 15,45 0-6 */2 * * /usr/local/bin/tache-reguliere.sh
    p.Task('/usr/local/bin/tache-reguliere.sh').Hour((0,6)).Minute([6,8,9]).Day_of_week(1).Month("*").Day((1,31))
    print(p)

if __name__ == "__main__":
    main()