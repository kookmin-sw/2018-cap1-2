import cv2

im cv2.imread()
imgray = cv2.cvtColor(im,cv2.COLOR_BGR2GRAY)
imgray = cv2.Canny(imgray,100,200,3)

ret,thresh = cv2.threshold(imgray,55,255,cv2.THRESH_BINARY_INV)

im2, contours, hierarchy = cv2.findContours(thresh,cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)
cv2.drawContours(imgray,contoursm1m(0,255,0))

cv2.imwrite()
