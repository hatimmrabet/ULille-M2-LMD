class interval:
    def __init__(self, start, end):
        self.start = start
        self.end = end

    def __str__(self):
        return '%d to %d' % (self.start, self.end)
    
    def hourInterval(self, start, end):
        return interval(start, end)

    def checkHour(self):
        if self.start<self.end and self.start>=0 and self.end<24:
            return True
        else:
            return False
    
    def checkMinute(self):
        if self.start<self.end and self.start>=0 and self.end<60:
            return True
        else:
            return False
    
    def checkDay(self):
        if self.start<self.end and self.start>=0 and self.end<31:
            return True
        else:
            return False
    
    def checkMonth(self):
        if self.start<self.end and self.start>=0 and self.end<12:
            return True
        else:
            return False
    
    def checkDay_of_week(self):
        if self.start<self.end and self.start>=0 and self.end<7:
            return True
        else:
            return False
    