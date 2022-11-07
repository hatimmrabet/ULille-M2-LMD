from Model.Range import Range

def check(param, min, max):
    if isinstance(param, list):
        if(param[0]<param[1]):
            if param[0]>=min and param[1]<=max:
                return True
    elif isinstance(param, Range):
        if param.start < param.end:
            if param.start >= min and param.end <= max:
                return True
    elif isinstance(param, int):
        if param>=min and param<=max:
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
    elif isinstance(param, Range):
        return value >= param.start and value <= param.end and (value - param.start) % param.step == 0
    elif isinstance(param, int):
        return value == param
    return False
    
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

