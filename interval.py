class interval:
    def __init__(self, start, end):
        self.start = start
        self.end = end

    def __str__(self):
        return '%d-%d' % (self.start, self.end)
    
    def hourInterval(self, start, end):
        return interval(start, end)

