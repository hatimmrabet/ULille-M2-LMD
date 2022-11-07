from .Checker import *

def affichageText(param, field):
    """
    Affichage du parametre en une chaine de characteres
    param: the field to display
    field: the field name
    """
    if param:
        ret=""
        if isinstance(param,Interval):
            if(param.step==1):
                ret+= "every "+field+" from "+ str(param.start)+" to "+str(param.end)
            else:
                ret+= "every "+str(param.step)+" "+field+" from "+ str(param.start)+" to "+str(param.end)
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
        if isinstance(param,Interval):
            if param.start==min and param.end==max:
                ret+="*"
            else:
                ret+= str(param.start)+"-"+str(param.end)
            if param.step!=1:
                ret+="/"+str(param.step)
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
