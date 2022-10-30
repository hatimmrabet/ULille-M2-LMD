from datetime import datetime
from crontab import CronTab

class planner :
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

    def __str__(self):
        hour_string = affichageText(self.hour, "hour")
        minute_string = affichageText(self.minute, "minute")
        day_string = affichageText(self.day, "day")
        month_string = affichageText(self.month, "month")
        day_of_week_string = affichageText(self.day_of_week, "day of week")
        return "Task: %s %s %s %s %s %s" % (self.task, hour_string, minute_string, day_string, month_string, day_of_week_string)

def check(param, min, max):
    if isinstance(param, list) or isinstance(param, tuple):
        if(param[0]<param[1]):
            if param[0]>=min and param[1]<=max:
                return True
    elif isinstance(param, int):
        if param>=min and param<=max:
            return True
    elif param =="*":
        return True
    return False

def checkIsPlannedAt(param, value):
    """
    param: the field to check
    value: the value to compare
    field: the field name
    """
    if isinstance(param, list):
        return value in param
    elif isinstance(param, tuple):
        return value >= param[0] and value <= param[1]
    elif isinstance(param, int):
        return value == param
    elif param =="*":
        return True
    return False
    

def affichageText(param, field):
    """
    Affichage du parametre en une chaine de characteres
    param: the field to display
    field: the field name
    """
    if param:
        ret=""
        if isinstance(param,tuple):
            ret+= "every "+field+" from "+ str(param[0])+" to "+str(param[1])
        elif isinstance(param,list):
            ret+= "at "+field+"s "
            for i in param:
                ret+=str(i)+","
            ret=ret[:-1]
        elif isinstance(param,int):
            ret+= "at "+field+" "+ str(param)
        else:
            ret="every "+field
        return ret
    return ""

def affichageCron(param, field):
    """
    Affichage du parametre en une chaine de characteres de cron
    param: the field to display
    """
    ret=""
    if param:
        max = getMaxMin(field)[1]
        min = getMaxMin(field)[0]
        if isinstance(param,tuple):
            if param[0]==min and param[1]==max:
                ret+="*"
            else:
                ret+= str(param[0])+"-"+str(param[1])
        elif isinstance(param,list):
            for i in param:
                ret+=str(i)+","
            ret=ret[:-1]
        elif isinstance(param,int):
            ret+=str(param)
        elif param=="*":
            ret+="*"
        ret+=" "
    return ret

def getMaxMin(field):
    """
    Retourne le max et le min pour un champ
    param: the field to display
    """
    if field == "hour":
        return (0,23)
    elif field == "minute":
        return (0,59)
    elif field == "day":
        return (1,31)
    elif field == "month":
        return (1,12)
    elif field == "day_of_week":
        return (0,6)
    return None


if __name__ == "__main__":
    p = planner()
    # 15,45 0-6 * * 0-2 /usr/local/bin/tache-reguliere.sh
    p.Task('/usr/local/bin/tache-reguliere.sh').Hour((0,6)).Minute([15,45]).Day((1,31)).Month("*").Day_of_week((0,2))
    print(p)
    print(p.affichageCron())
    mydate = datetime(2020, 12, 1, 4, 45)
    print(mydate,":",p.isPlannedAt(mydate))
    # print(mydate.weekday()) # to check the day of week
    wrongdate = datetime(2022, 12, 1, 4, 45)
    print(wrongdate,":",p.isPlannedAt(wrongdate))
