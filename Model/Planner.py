from datetime import datetime, timedelta
from Utils.Checker import *
from Utils.Printer import *

class Planner :
    ## a cron job object
    def __init__(self):
        self.task = ""
        self.minute = None
        self.hour = None
        self.day = None
        self.month = None
        self.day_of_week = None

    def Task(self, task):
        self.task = task
        return self

    def Hour(self, param):
        if check(param,getMaxMin("hour")[0],getMaxMin("hour")[1]):
            self.hour=param
        return self

    def Minute(self, param):
        if check(param,getMaxMin("minute")[0],getMaxMin("minute")[1]):
            self.minute = param
        return self
    
    def Day(self, param):
        if check(param,getMaxMin("day")[0],getMaxMin("day")[1]):
            self.day = param
        return self
    
    def Month(self, param):
        if check(param,getMaxMin("month")[0],getMaxMin("month")[1]):
            self.month = param
        return self

    def Day_of_week(self, param):
        if check(param,getMaxMin("day_of_week")[0],getMaxMin("day_of_week")[1]):
            self.day_of_week = param
        return self

    def affichageCron(self):
        ret = affichageCron(self.minute,"minute")
        ret += affichageCron(self.hour,"hour")
        ret += affichageCron(self.day,"day")
        ret += affichageCron(self.month,"month")
        ret += affichageCron(self.day_of_week,"day_of_week")
        ret += self.task
        return ret
        
    def isPlannedAt(self, date : datetime):
        if self.minute is not None and not checkIsPlannedAt(self.minute, date.minute):
            return False
        if self.hour is not None and not checkIsPlannedAt(self.hour, date.hour):
            return False
        if self.day is not None and not checkIsPlannedAt(self.day, date.day):
            return False
        if self.month is not None and not checkIsPlannedAt(self.month, date.month):
            return False
        if self.day_of_week is not None and not checkIsPlannedAt(self.day_of_week, date.weekday()):
            return False
        return True

    def getPlannedDates(self, start : datetime, end : datetime):
        dates = []
        date = start
        while date < end :
            if self.isPlannedAt(date):
                dates.append(date.strftime("%Y-%m-%d %H:%M"))
            date = date + timedelta(minutes=1)
        return dates

    def __str__(self):
        hour_string = affichageText(self.hour, "hour")
        minute_string = affichageText(self.minute, "minute")
        day_string = affichageText(self.day, "day")
        month_string = affichageText(self.month, "month")
        day_of_week_string = affichageText(self.day_of_week, "day of week")
        return "Task: %s %s %s %s %s %s" % (self.task, hour_string, minute_string, day_string, month_string, day_of_week_string)