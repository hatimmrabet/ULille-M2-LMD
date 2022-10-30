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
            if check(hourparam,0,23):
                self.hour=hourparam
        else:
            if hourparam == '*':
                self.hour=hourparam
            elif hourparam<24 and hourparam>=0:
                self.hour=hourparam
        return self

    def Minute(self, minuteparam):
        if isinstance(minuteparam, list) or isinstance(minuteparam, tuple):
            if check(minuteparam, 0, 59):
                self.minute=minuteparam
        else:
            if minuteparam == '*':
                self.minute=minuteparam
            elif minuteparam<=60 and minuteparam>0:
                self.minute=minuteparam
        return self
    
    def Day(self, dayparam):
        if isinstance(dayparam, list) or isinstance(dayparam, tuple):
            if check(dayparam, 1, 31):
                self.day=dayparam
        else:
            if dayparam == '*':
                self.day=dayparam
            elif dayparam<=31 and dayparam>0:
                self.day=dayparam
        return self
    
    def Month(self, monthparam):
        if isinstance(monthparam, list) or isinstance(monthparam, tuple):
            if check(monthparam,1,12):
                self.month=monthparam
        else:
            if monthparam == '*':
                self.month=monthparam
            elif monthparam<=12 and monthparam>0:
                self.month=monthparam
        return self

    def Day_of_week(self, day_of_week):
        if isinstance(day_of_week, list) or isinstance(day_of_week, tuple):
            if check(day_of_week,0,6):
                self.day_of_week=day_of_week
        else:
            if day_of_week == '*':
                self.day_of_week=day_of_week
            elif day_of_week<=7 and day_of_week>0:
                self.day_of_week=day_of_week
        return self

    def affichageCron(self):
        ret = affichageCron(self.minute,"minute")+" "
        ret += affichageCron(self.hour,"hour")+" "
        ret += affichageCron(self.day,"day")+" "
        ret += affichageCron(self.month,"month")+" "
        ret += affichageCron(self.day_of_week,"day_of_week")+" "
        ret += self.task
        return ret
        
    def __str__(self):
        hour_string = affichageText(self.hour, "hour")
        minute_string = affichageText(self.minute, "minute")
        day_string = affichageText(self.day, "day")
        month_string = affichageText(self.month, "month")
        day_of_week_string = affichageText(self.day_of_week, "day of week")
        return "Task: %s %s %s %s %s %s" % (self.task, hour_string, minute_string, day_string, month_string, day_of_week_string)

def check(param, min, max):
    if(param[0]<param[1]):
        if param[0]>=min and param[1]<=max:
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
            ret+= str(param)
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
    # 15,45 0-6 */2 * * /usr/local/bin/tache-reguliere.sh
    p.Task('/usr/local/bin/tache-reguliere.sh').Hour((0,22)).Minute((6,16)).Day_of_week(1).Month("*").Day((1,31))
    print(p)
    print(p.affichageCron())