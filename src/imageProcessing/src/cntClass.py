class contours:
    x, y, w, h = 0, 0, 0, 0
    line = 0
    draw = False

    def __init__(self):
        self.x = 0
        self.y = 0
        self.w = 0
        self.h = 0
        self.line = 0

    def setData(self, x, y, w, h):
        self.x = x
        self.y = y
        self.w = w
        self.h = h

    def getX(self):
        return self.x

    def getY(self):
        return self.y

    def getW(self):
        return self.w

    def getH(self):
        return self.h

    def getCenterX(self):
        return self.x+(self.w  * 0.5)

    def getCenterY(self):
        return self.y + self.h * 0.5

    def setLine(self,line):
        self.line = line

    def getLine(self):
        return self.line

    def setDraw(self):
        self.draw = True

    def getDraw(self):
        return self.draw